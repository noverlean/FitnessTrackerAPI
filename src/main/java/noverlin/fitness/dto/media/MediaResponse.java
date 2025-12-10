package noverlin.fitness.dto.media;

import lombok.Data;

import java.time.Instant;

@Data
public class MediaResponse {
    private Long id;
    private String title;
    private String path;
    private Instant uploadedAt;
}
