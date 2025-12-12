package noverlin.fitness.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import noverlin.fitness.dto.auth.TokenPair;
import noverlin.fitness.exceptions.custom.ConflictException;
import noverlin.fitness.exceptions.custom.token.InvalidRefreshTokenException;
import noverlin.fitness.exceptions.custom.notFound.UserNotFoundException;
import noverlin.fitness.jwt.JwtTokenProvider;
import noverlin.fitness.jwt.TokenHash;
import noverlin.fitness.model.RefreshToken;
import noverlin.fitness.model.User;
import noverlin.fitness.repository.RefreshTokenRepository;
import noverlin.fitness.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Set;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final TokenHash tokenHash;
    private final PasswordEncoder passwordEncoder;

    @Value(value = "${jwt.refresh.expiration}")
    private Long refreshExpMillis;

    public AuthService(UserRepository userRepository,
                       RefreshTokenRepository refreshTokenRepository,
                       JwtTokenProvider jwtTokenProvider,
                       TokenHash tokenHash,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.tokenHash = tokenHash;
        this.passwordEncoder = passwordEncoder;
    }

    public TokenPair register(String username, String rawPassword, String deviceId) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new ConflictException("Username already exists");
        }

        User user = new User();
        user.setUsername(username);
        user.setPasswordHash(passwordEncoder.encode(rawPassword));
        user.setRoles(Set.of("USER"));
        userRepository.save(user);

        return getTokenPair(deviceId, user);
    }

    public TokenPair login(String username, String rawPassword, String deviceId) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(UserNotFoundException::new);

        if (!passwordEncoder.matches(rawPassword, user.getPasswordHash()))
            throw new BadCredentialsException("Invalid credentials");

        return getTokenPair(deviceId, user);
    }

    public TokenPair refresh(String refreshToken) {
        Jws<Claims> jws = jwtTokenProvider.parse(refreshToken);
        Claims claims = jws.getBody();
        Long userId = Long.valueOf(claims.getSubject());
        String deviceId = claims.get("deviceId", String.class);

        String hash = tokenHash.hash(refreshToken);
        RefreshToken stored = refreshTokenRepository.findByToken(hash)
                .orElseThrow(() -> new InvalidRefreshTokenException("Refresh token not found"));

        if (stored.isRevoked() || stored.getExpiresAt().isBefore(Instant.now()))
            throw new InvalidRefreshTokenException();

        if (!stored.getUser().getId().equals(userId) || !stored.getDeviceId().equals(deviceId))
            throw new InvalidRefreshTokenException("Refresh token mismatched");

        stored.setRevoked(true);
        stored.setReplacedAt(Instant.now());
        refreshTokenRepository.save(stored);

        User user = stored.getUser();
        return getTokenPair(deviceId, user);
    }

    public void logout(String refreshToken) {
        String hash = tokenHash.hash(refreshToken);
        refreshTokenRepository.findByToken(hash).ifPresent(rt -> {
            rt.setRevoked(true);
            rt.setReplacedAt(Instant.now());
            refreshTokenRepository.save(rt);
        });
    }

    private TokenPair getTokenPair(String deviceId, User user) {
        String access = jwtTokenProvider.generateAccessToken(user);
        String refresh = jwtTokenProvider.generateRefreshToken(user, deviceId);

        RefreshToken entity = new RefreshToken()
                .setToken(tokenHash.hash(refresh))
                .setUser(user)
                .setDeviceId(deviceId)
                .setCreatedAt(Instant.now())
                .setExpiresAt(
                        Instant.now().plusMillis(refreshExpMillis)
                )
                .setRevoked(false);
        refreshTokenRepository.save(entity);

        return new TokenPair(access, refresh);
    }
}
