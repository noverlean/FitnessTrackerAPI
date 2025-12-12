package noverlin.fitness.service;

import lombok.RequiredArgsConstructor;
import noverlin.fitness.dto.media.MediaResponse;
import noverlin.fitness.exceptions.custom.notFound.UserNotFoundException;
import noverlin.fitness.jwt.CurrentUserProvider;
import noverlin.fitness.mapper.MediaMapper;
import noverlin.fitness.model.Media;
import noverlin.fitness.model.User;
import noverlin.fitness.repository.MediaRepository;
import noverlin.fitness.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class MediaService {
    private final MediaRepository mediaRepository;
    private final UserRepository userRepository;
    private final MediaMapper mediaMapper;

    public MediaResponse save(MultipartFile file) throws IOException {
        String username = CurrentUserProvider.getCurrentUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(UserNotFoundException::new);

        String uploadDir = "uploads/" + username;
        Files.createDirectories(Path.of(uploadDir));

        Path path = Path.of(uploadDir, Objects.requireNonNull(file.getOriginalFilename()));
        file.transferTo(path);

        String title = file.getOriginalFilename().split("\\.(?=[^.]+$)")[0];

        Media media = new Media()
                .setUser(user)
                .setTitle(title)
                .setPath(path.toString())
                .setUploadedAt(Instant.now());

        mediaRepository.save(media);

        return mediaMapper.toDto(media);
    }
}

