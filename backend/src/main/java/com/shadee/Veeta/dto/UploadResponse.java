package com.shadee.Veeta.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UploadResponse {
    private String fileId;
    private String fileName;
    private String filePath;
    private String uploadedBy;
    private String addedAt;
    private String message;
}
