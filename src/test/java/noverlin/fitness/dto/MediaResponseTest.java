package noverlin.fitness.dto;

import noverlin.fitness.dto.media.MediaResponse;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class MediaResponseTest {

    @Test
    void gettersAndSetters_ShouldWorkCorrectly() {
        Instant now = Instant.now();
        MediaResponse media = new MediaResponse()
                .setId(1L)
                .setTitle("After Run")
                .setPath("uploads/testuser/photo.jpg")
                .setUploadedAt(now);

        assertEquals(1L, media.getId());
        assertEquals("After Run", media.getTitle());
        assertEquals("uploads/testuser/photo.jpg", media.getPath());
        assertEquals(now, media.getUploadedAt());
    }
}
