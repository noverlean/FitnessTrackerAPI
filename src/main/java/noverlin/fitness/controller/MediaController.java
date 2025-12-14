package noverlin.fitness.controller;

import lombok.RequiredArgsConstructor;
import noverlin.fitness.api.MediaApi;
import noverlin.fitness.dto.media.MediaResponse;
import noverlin.fitness.jwt.CurrentUserProvider;
import noverlin.fitness.service.MediaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
public class MediaController implements MediaApi {

    private final MediaService mediaService;

    @Override
    public ResponseEntity<MediaResponse> uploadMedia(
            @RequestParam("file") MultipartFile file
    ) throws IOException {
        MediaResponse response = mediaService.save(file);
        return ResponseEntity.ok(response);
    }
}
