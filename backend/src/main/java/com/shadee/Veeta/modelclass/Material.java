package com.shadee.Veeta.modelclass;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "materials")
public class Material {
    @Id
    @Column(name = "file_id", unique = true, nullable = false)
    private String fileId;
    @Column(name = "file_name",nullable = false)
    private String filename;
    @Column(name = "file_Path",nullable = false)
    private String filePath;
    @Column(name = "uploaded_by",nullable = false)
    private String uploadedBy;
    @Column(name = "added_at",nullable = false)
    private String addedAt;
}
