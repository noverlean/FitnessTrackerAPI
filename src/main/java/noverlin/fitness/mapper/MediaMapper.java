package noverlin.fitness.mapper;

import noverlin.fitness.dto.media.MediaResponse;
import noverlin.fitness.model.Media;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MediaMapper {
    MediaResponse toDto(Media media);
}
