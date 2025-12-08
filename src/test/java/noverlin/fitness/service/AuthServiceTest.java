package noverlin.fitness.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import noverlin.fitness.TestDataFactory;
import noverlin.fitness.dto.TokenPair;
import noverlin.fitness.exceptions.custom.ConflictException;
import noverlin.fitness.jwt.JwtTokenProvider;
import noverlin.fitness.jwt.TokenHash;
import noverlin.fitness.model.RefreshToken;
import noverlin.fitness.model.User;
import noverlin.fitness.repository.RefreshTokenRepository;
import noverlin.fitness.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private TokenHash tokenHash;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    private User user;
    private RefreshToken refreshToken;
    private Claims claims;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        ReflectionTestUtils.setField(authService, "refreshExpMillis", 1_296_000_000L);

        user = TestDataFactory.createValidUser();
        refreshToken = TestDataFactory.createValidRefreshToken(user);

        claims = Jwts.claims();
        claims.setSubject("1");
        claims.put("deviceId", "device-id");
    }

    @Test
    void register_success() {
        when(userRepository.findByUsername(any())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(any())).thenReturn("password-hash");

        when(userRepository.save(any())).thenReturn(user);
        when(jwtTokenProvider.generateAccessToken(any())).thenReturn("access-token");
        when(jwtTokenProvider.generateRefreshToken(any(), eq("device-id"))).thenReturn("refresh-token");
        when(tokenHash.hash("refresh")).thenReturn("hashedRefresh");

        TokenPair tokenPair = authService.register("username", "rawPassword", "device-id");

        Assertions.assertNotNull(tokenPair);
        Assertions.assertEquals("access-token", tokenPair.getAccessToken());
        Assertions.assertEquals("refresh-token", tokenPair.getRefreshToken());
        verify(refreshTokenRepository).save(any());
    }

    @Test
    void register_userExists_throwsConflict() {
        when(userRepository.findByUsername("username")).thenReturn(Optional.of(new User()));

        Assertions.assertThrows(ConflictException.class, () -> authService.register("username", "secret", "device-id"));
    }

    @Test
    void login_success() {
        when(userRepository.findByUsername(any())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(any(), any())).thenReturn(true);
        when(jwtTokenProvider.generateAccessToken(any())).thenReturn("access-token");
        when(jwtTokenProvider.generateRefreshToken(any(), eq("device-id"))).thenReturn("refresh-token");
        when(tokenHash.hash(any())).thenReturn("password-hash");

        TokenPair tokenPair = authService.login("username", "raw-password", "device-id");

        Assertions.assertNotNull(tokenPair);
        Assertions.assertEquals("access-token", tokenPair.getAccessToken());
        Assertions.assertEquals("refresh-token", tokenPair.getRefreshToken());
        verify(refreshTokenRepository).save(any());
    }

    @Test
    void login_userWithUsernameNotFound_throwsBadCredantials() {
        when(userRepository.findByUsername("bad-username")).thenReturn(Optional.empty());

        Assertions.assertThrows(BadCredentialsException.class, () -> authService.login("username", "raw-password", "device-id"));
    }

    @Test
    void login_userWithIncorrectPassword_throwsBadCredantials() {
        when(userRepository.findByUsername("username")).thenReturn(Optional.of(user));

        Assertions.assertThrows(BadCredentialsException.class, () -> authService.login("username", "incorrect-rawPassword", "device-id"));
    }

    @Test
    void refresh_success() {
        Jws<Claims> jws = mock(Jws.class);
        when(jws.getBody()).thenReturn(claims);

        when(jwtTokenProvider.parse(any())).thenReturn(jws);
        when(tokenHash.hash(any())).thenReturn("hashed-refresh");
        when(refreshTokenRepository.findByToken("hashed-refresh")).thenReturn(Optional.of(refreshToken));

        TokenPair tokenPair = authService.refresh("refresh-token");

        Assertions.assertNotNull(tokenPair);
        verify(refreshTokenRepository).save(refreshToken);
        Assertions.assertTrue(refreshToken.isRevoked());
    }

    @Test
    void refresh_notFoundToken_throwsBadCredantials() {
        Jws<Claims> jws = mock(Jws.class);
        when(jws.getBody()).thenReturn(claims);

        when(jwtTokenProvider.parse(any())).thenReturn(jws);
        when(tokenHash.hash(any())).thenReturn("hashed-refresh");
        when(refreshTokenRepository.findByToken("hashed-refresh")).thenReturn(Optional.empty());

        Assertions.assertThrows(BadCredentialsException.class, () -> authService.refresh("refresh-token"));
    }

    @Test
    void refresh_Revoked_throwsBadCredantials() {
        Jws<Claims> jws = mock(Jws.class);
        when(jws.getBody()).thenReturn(claims);

        refreshToken.setRevoked(true);

        when(jwtTokenProvider.parse(any())).thenReturn(jws);
        when(tokenHash.hash(any())).thenReturn("hashed-refresh");
        when(refreshTokenRepository.findByToken(any())).thenReturn(Optional.of(refreshToken));

        Assertions.assertThrows(BadCredentialsException.class, () -> authService.refresh("refresh-token"));
    }

    @Test
    void refresh_Expired_throwsBadCredantials() {
        Jws<Claims> jws = mock(Jws.class);
        when(jws.getBody()).thenReturn(claims);

        refreshToken.setExpiresAt(Instant.now().minusMillis(1000));

        when(jwtTokenProvider.parse(any())).thenReturn(jws);
        when(tokenHash.hash(any())).thenReturn("hashed-refresh");
        when(refreshTokenRepository.findByToken(any())).thenReturn(Optional.of(refreshToken));

        Assertions.assertThrows(BadCredentialsException.class, () -> authService.refresh("refresh-token"));
    }

    @Test
    void refresh_mismatchedUser_throwsBadCredentials() {
        claims.setSubject("99");

        Jws<Claims> jws = mock(Jws.class);
        when(jws.getBody()).thenReturn(claims);

        when(jwtTokenProvider.parse(any())).thenReturn(jws);
        when(tokenHash.hash(any())).thenReturn("hashed-refresh");
        when(refreshTokenRepository.findByToken(any())).thenReturn(Optional.of(refreshToken));

        Assertions.assertThrows(BadCredentialsException.class, () -> authService.refresh("refresh-token"));
    }

    @Test
    void refresh_mismatchedDevice_throwsBadCredentials() {
        claims.put("deviceId", "another-device");

        Jws<Claims> jws = mock(Jws.class);
        when(jws.getBody()).thenReturn(claims);

        when(jwtTokenProvider.parse(any())).thenReturn(jws);
        when(tokenHash.hash(any())).thenReturn("hashed-refresh");
        when(refreshTokenRepository.findByToken(any())).thenReturn(Optional.of(refreshToken));

        Assertions.assertThrows(BadCredentialsException.class, () -> authService.refresh("refresh-token"));
    }

    @Test
    void logout_success() {
        when(tokenHash.hash(any())).thenReturn("hashed-refresh");
        when(refreshTokenRepository.findByToken(any())).thenReturn(Optional.of(refreshToken));

        authService.logout("refresh-token");

        Assertions.assertTrue(refreshToken.isRevoked());
        Assertions.assertNotNull(refreshToken.getReplacedAt());
        verify(refreshTokenRepository).save(refreshToken);
    }

    @Test
    void logout_tokenNotFound_noSave() {
        when(tokenHash.hash(any())).thenReturn("hashed-refresh");
        when(refreshTokenRepository.findByToken(any())).thenReturn(Optional.empty());

        authService.logout("refresh-token");

        verify(refreshTokenRepository, never()).save(any());
    }

    @Test
    void getTokenPair_success() {
        when(jwtTokenProvider.generateAccessToken(user)).thenReturn("access-token");
        when(jwtTokenProvider.generateRefreshToken(user, "device-id")).thenReturn("refresh-token");
        when(tokenHash.hash(any())).thenReturn("hashed-refresh");

        TokenPair tokenPair = ReflectionTestUtils.invokeMethod(authService, "getTokenPair", "device-id", user);

        Assertions.assertEquals("access-token", tokenPair.getAccessToken());
        Assertions.assertEquals("refresh-token", tokenPair.getRefreshToken());

        ArgumentCaptor<RefreshToken> captor = ArgumentCaptor.forClass(RefreshToken.class);
        verify(refreshTokenRepository).save(captor.capture());

        RefreshToken saved = captor.getValue();
        Assertions.assertEquals("hashed-refresh", saved.getToken());
        Assertions.assertEquals(user, saved.getUser());
        Assertions.assertEquals("device-id", saved.getDeviceId());
        Assertions.assertFalse(saved.isRevoked());
        Assertions.assertNotNull(saved.getCreatedAt());
        Assertions.assertNotNull(saved.getExpiresAt());
        Assertions.assertTrue(saved.getExpiresAt().isAfter(saved.getCreatedAt()));
    }
}
