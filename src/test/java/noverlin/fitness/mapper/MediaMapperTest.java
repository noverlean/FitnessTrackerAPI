package noverlin.fitness.mapper;

import noverlin.fitness.dto.media.MediaResponse;
import noverlin.fitness.model.Media;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class MediaMapperTest {

    private final MediaMapper mediaMapper = Mappers.getMapper(MediaMapper.class);

    @Test
    void mediaMapper_toDto_ShouldMapCorrectly() {
        Media media = new Media()
                .setId(1L)
                .setTitle("After Run")
                .setPath("uploads/testuser/photo.jpg")
                .setUploadedAt(Instant.now());

        MediaResponse dto = mediaMapper.toDto(media);

        assertEquals(media.getId(), dto.getId());
        assertEquals(media.getTitle(), dto.getTitle());
        assertEquals(media.getPath(), dto.getPath());
        assertEquals(media.getUploadedAt(), dto.getUploadedAt());
    }
}
