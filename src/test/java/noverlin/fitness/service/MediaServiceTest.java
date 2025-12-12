package noverlin.fitness.service;

import noverlin.fitness.dto.media.MediaResponse;
import noverlin.fitness.exceptions.custom.notFound.UserNotFoundException;
import noverlin.fitness.mapper.MediaMapper;
import noverlin.fitness.model.Media;
import noverlin.fitness.model.User;
import noverlin.fitness.repository.MediaRepository;
import noverlin.fitness.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MediaServiceTest {

    @Mock
    private MediaRepository mediaRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private MediaMapper mediaMapper;

    @Mock
    private MultipartFile multipartFile;

    @InjectMocks
    private MediaService mediaService;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setId(1L);
        user.setUsername("testuser");
    }

    @Test
    void save_ShouldSaveMediaAndReturnResponse() throws IOException {
        String filename = "progress.jpg";
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(multipartFile.getOriginalFilename()).thenReturn(filename);

        Media media = new Media()
                .setUser(user)
                .setTitle("progress")
                .setPath("uploads/testuser/" + filename);

        MediaResponse expectedResponse = new MediaResponse()
                .setId(1L)
                .setTitle("progress")
                .setPath("uploads/testuser/" + filename);

        when(mediaRepository.save(any(Media.class))).thenReturn(media);
        when(mediaMapper.toDto(any(Media.class))).thenReturn(expectedResponse);

        MediaResponse response = mediaService.save(multipartFile, "testuser");

        assertNotNull(response);
        assertEquals("progress", response.getTitle());
        assertEquals("uploads/testuser/" + filename, response.getPath());

        verify(userRepository).findByUsername("testuser");
        verify(mediaRepository).save(any(Media.class));
        verify(mediaMapper).toDto(any(Media.class));
        verify(multipartFile).transferTo(Path.of("uploads/testuser/" + filename));
    }

    @Test
    void save_ShouldThrowUserNotFoundException_WhenUserDoesNotExist() {
        when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> mediaService.save(multipartFile, "unknown"));

        verify(userRepository).findByUsername("unknown");
        verifyNoInteractions(mediaRepository, mediaMapper);
    }

    @Test
    void save_ShouldThrowIOException_WhenFileTransferFails() throws IOException {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(multipartFile.getOriginalFilename()).thenReturn("progress.jpg");
        doThrow(new IOException("transfer failed"))
                .when(multipartFile).transferTo(any(Path.class));

        assertThrows(IOException.class,
                () -> mediaService.save(multipartFile, "testuser"));

        verify(userRepository).findByUsername("testuser");
        verify(multipartFile).transferTo(any(Path.class));
        verifyNoInteractions(mediaRepository, mediaMapper);
    }
}

