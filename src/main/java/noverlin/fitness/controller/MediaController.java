package noverlin.fitness.controller;

import lombok.AllArgsConstructor;
import noverlin.fitness.dto.media.MediaResponse;
import noverlin.fitness.service.MediaService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@AllArgsConstructor
@RestController
@RequestMapping("/media")
public class MediaController {

    MediaService mediaService;

    @PostMapping
    public ResponseEntity<MediaResponse> uploadMedia(
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal UserDetails userDetails
    ) throws IOException {
        MediaResponse response = mediaService.save(file, userDetails.getUsername());
        return ResponseEntity.ok(response);
    }
}
