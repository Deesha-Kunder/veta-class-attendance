package com.shadee.Veeta.controller;

import com.shadee.Veeta.dto.UploadResponse;
import com.shadee.Veeta.service.MaterialService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MaterialController {

    private MaterialService service;

    @PostMapping("/admin/materials/upload")
    public ResponseEntity<UploadResponse> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("uploadedBy") String uploadedBy
    ) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new UploadResponse(null, null, null, null, "File is empty", null));
            }
            String contentType = file.getContentType();
            if (contentType == null || !contentType.equals("application/pdf")) {
                return ResponseEntity.badRequest()
                        .body(new UploadResponse(null, null, null, null, "Only PDFs are allowed", null));
            }
            UploadResponse response = service.uploadAndSave(file, uploadedBy);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new UploadResponse(null, null, null, null, "Upload failed", null));
        }
    }


    @GetMapping("/materials")
    public ResponseEntity<?> getFiles(){
        try{
            return ResponseEntity.ok(service.getAllFiles());
        }catch (Exception e){
            return ResponseEntity.internalServerError().body("Error fetching files");
        }
    }
    @GetMapping("/student/materials")
    public ResponseEntity<?> getFiless(){
        try{
            return ResponseEntity.ok(service.getAllFiles());
        }catch (Exception e){
            return ResponseEntity.internalServerError().body("Error fetching files");
        }
    }


}
