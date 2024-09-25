package com.quanle.quan.controller;

import com.quanle.quan.models.response.FileResponse;
import com.quanle.quan.service.CloudinaryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;


@RestController
@RequestMapping("/api/files")
public class FileUploadController {

    private final CloudinaryService cloudinaryService;

    public FileUploadController(CloudinaryService cloudinaryService) {
        this.cloudinaryService = cloudinaryService;
    }

    @PostMapping("/upload/image")
    public ResponseEntity<?> uploadImage(@RequestParam("upload") MultipartFile file) throws IOException {
        String dirtyHtml = "<div><h1>Title</h1><script>alert('Hacked!');</script><p>This is a <strong>test</strong> paragraph.</p></div>";

        // Làm sạch nội dung HTML
        String cleanDescription = Jsoup.clean(dirtyHtml, Safelist.basic());

        // In kết quả
        System.out.println("Cleaned HTML: " + cleanDescription);
        String url = cloudinaryService.uploadFile(file).get("url").toString();
        System.out.println("updating...." + url);
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("url", url);
        responseData.put("uploaded", true);
        return ResponseEntity.ok(responseData); // Trả về định dạng Map với key là "url" để CKEditor sử dụng
    }
    @PostMapping("/upload/video")
    public ResponseEntity<?> uploadVideo(@RequestParam("file") MultipartFile file) throws IOException {
        return ResponseEntity.ok(cloudinaryService.uploadVideo(file));
    }
}