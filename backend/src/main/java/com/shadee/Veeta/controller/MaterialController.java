package com.shadee.Veeta.controller;

import com.shadee.Veeta.dto.UploadResponse;
import com.shadee.Veeta.security.CustomUserDetails;
import com.shadee.Veeta.service.MaterialService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MaterialController {

    private final MaterialService service;

    @PostMapping("/admin/materials/upload")
    public ResponseEntity<UploadResponse> uploadFile(
            @RequestPart("file") MultipartFile file,
            @RequestPart("uploaded_by") String uploadedBy
    ) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new UploadResponse(null, null, null, null,null,"File is empty"));
            }
            String contentType = file.getContentType();
            if (contentType == null || !contentType.equals("application/pdf")) {
                return ResponseEntity.badRequest()
                        .body(new UploadResponse(null, null, null, null,  null,"Only PDFs are allowed"));
            }
            UploadResponse response = service.uploadAndSave(file, uploadedBy);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new UploadResponse(null, null, null, null, null,"Upload failed"+e.getMessage()));
        }
    }


    @GetMapping("/admin/{admin_id}/materials")
    public ResponseEntity<?> getFilesUploadedByAdmin(@PathVariable("admin_id") String adminID){
        try{
            return ResponseEntity.ok(service.getAllFilesUploadedByAdmin(adminID));
        }catch (Exception e){
            return ResponseEntity.internalServerError().body("Error fetching files");
        }
    }
    @GetMapping("/admin/materials/{file_id}")
    public ResponseEntity<Map<String,String>> getFileByIdFromAdminPage(@PathVariable("file_id") String fileId){
        try{
            String signedUrl = service.getParticularFile(fileId);
            return ResponseEntity.ok(Map.of("url",signedUrl));
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(Map.of("Error","Error fetching file"));
        }
    }
    @GetMapping("/student/materials")
    public ResponseEntity<?> getFiles(Authentication authentication){
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String email = userDetails.getEmail();
        try{
            return ResponseEntity.ok(service.getAllFiles(email));
        }catch (Exception e){
            return ResponseEntity.internalServerError().body("Error fetching files");
        }
    }
    @GetMapping("/student/materials/{file_id}")
    public ResponseEntity<Map<String,String>> getFileByIdFromStudentPage(@PathVariable("file_id") String fileId){
        try{
            String signedUrl = service.getParticularFile(fileId);
            return ResponseEntity.ok(Map.of("url",signedUrl));
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(Map.of("Error","Error fetching file"));
        }
    }


}
