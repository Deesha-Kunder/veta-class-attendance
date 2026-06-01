package com.shadee.Veeta.service;

import com.shadee.Veeta.dto.UploadResponse;
import com.shadee.Veeta.modelclass.Material;
import com.shadee.Veeta.modelclass.Student;
import com.shadee.Veeta.repository.MaterialRepository;
import com.shadee.Veeta.repository.StudentRegisterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MaterialService {
    private final MaterialRepository repository;
    private final SupabaseStorageService supabaseService;
    private final StudentRegisterRepository studentRepository;

    public UploadResponse uploadAndSave(MultipartFile file, String uploadedBy) throws IOException, InterruptedException {
        String fileId = UUID.randomUUID().toString();
        String originalFileName = file.getOriginalFilename();
        String filePath = null;
        filePath = supabaseService.uploadFile(file, fileId, originalFileName);

        Material material = new Material();
        material.setFileId(fileId);
        material.setFilename(originalFileName);
        material.setUploadedBy(uploadedBy);
        material.setFilePath(filePath);
        material.setAddedAt(LocalDateTime.now().toString());
        Material savedMaterial = repository.save(material);

        UploadResponse response = new UploadResponse(
                savedMaterial.getFileId(),
                savedMaterial.getFilename(),
                savedMaterial.getFilePath(),
                savedMaterial.getUploadedBy(),
                savedMaterial.getAddedAt(),
                "File Uploaded successfully"
        );
        return response;
    }

    public List<Material> getAllFilesUploadedByAdmin(String uploadedBy) {
        System.out.println(repository.findByUploadedByOrderByAddedAtDesc(uploadedBy));
        return repository.findByUploadedByOrderByAddedAtDesc(uploadedBy);
    }

    public String getParticularFile(String fileId) throws IOException, InterruptedException {
        Material material = repository.findByFileId(fileId)
                .orElseThrow(() -> new RuntimeException("File not found"));
        return supabaseService.generateSignedURL(material.getFilePath(), 1800);
    }
    public List<Material> getAllFiles(String email){
        Student student =  studentRepository.findByEmail(email)
                .orElseThrow(()-> new RuntimeException("Student not found"));
        String registeredBy = student.getRegisteredBy();

        return repository.findByUploadedByOrderByAddedAtDesc(registeredBy);
    }
}
