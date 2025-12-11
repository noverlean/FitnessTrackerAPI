package noverlin.fitness.dto.media;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.Instant;

@Data
@Accessors(chain = true)
public class MediaResponse {

    @Schema(description = "Идентификатор медиа", example = "23")
    private Long id;

    @Schema(description = "Название/подпись для отображения на клиенте", example = "Я после пробежки)")
    private String title;

    @Schema(description = "Путь медиа в директории сервера для загрузки клиентом", example = "uploads\\\\new user 1\\\\icons8-щит-96.png")
    private String path;

    @Schema(description = "Момент времени, в который фото было загружено", example = "2025-12-10T16:56:21.189766300Z")
    private Instant uploadedAt;
}
