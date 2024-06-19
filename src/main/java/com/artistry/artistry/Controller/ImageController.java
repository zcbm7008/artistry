package com.artistry.artistry.Controller;

import com.artistry.artistry.Domain.member.Member;
import com.artistry.artistry.Service.S3ImageService;
import com.artistry.artistry.auth.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequestMapping(value = "/api/images")
@RestController
public class ImageController {

    @Autowired
    private S3ImageService imageService;

    @PostMapping("/upload/profile")
    public ResponseEntity<String> uploadImage(@Authorization Member member,
                                              @RequestParam("file") Object file,
                                              @RequestParam("x") int x,
                                              @RequestParam("y") int y,
                                              @RequestParam("width") int width,
                                              @RequestParam("height") int height) {
        try {
            if (file instanceof MultipartFile multipartFile) {
                String response = imageService.saveThumbnailImage(multipartFile, member, x, y, width, height);
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(400).body("Invalid file type");
            }
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Failed to process image");
        }
    }
}
