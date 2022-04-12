package ru.hogwarts.school.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.service.AvatarService;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

@RestController
@RequestMapping("avatars")
public class AvatarController {

    private final AvatarService service;

    public AvatarController(AvatarService service) {
        this.service = service;
    }

    @PostMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadAvatar(@PathVariable Long id, @RequestParam MultipartFile avatar) throws IOException {
        if(avatar.getSize() >= 1024 * 100) {
            return ResponseEntity.badRequest().body("File is too big");
        }

        service.uploadAvatar(id, avatar);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/repository")
    public ResponseEntity<byte[]> downloadAvatarFromRepository(@PathVariable Long id) {
        Avatar avatar = service.findAvatar(id);
        if(avatar == null)
            return ResponseEntity.notFound().build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(avatar.getMediaType()));
        headers.setContentLength(avatar.getFileSize());

        return ResponseEntity.ok().headers(headers).body(avatar.getData());
    }

    @GetMapping("/{id}/disk")
    public ResponseEntity<String> downloadAvatarFromDisk(@PathVariable Long id, HttpServletResponse response) throws IOException {
        Avatar avatar = service.findAvatar(id);
        if(avatar == null)
            return ResponseEntity.badRequest().body("Avatar is not found");

        Path path = Path.of(avatar.getFilePath());
        try(InputStream is = Files.newInputStream(path);
            OutputStream os = response.getOutputStream();
            BufferedInputStream bis = new BufferedInputStream(is, 1024);
            BufferedOutputStream bos = new BufferedOutputStream(os, 1024);
        ) {
            response.setContentType(avatar.getMediaType());
            response.setContentLength((int) avatar.getFileSize());
            bis.transferTo(bos);
        }

        return ResponseEntity.ok().build();
    }
}
