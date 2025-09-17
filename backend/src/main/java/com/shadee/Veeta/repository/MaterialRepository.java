package com.shadee.Veeta.repository;

import com.shadee.Veeta.modelclass.Material;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface MaterialRepository extends JpaRepository<Material,String> {
    List<Material> findByUploadedByOrderByAddedAtDesc(String uploadedBy);
    Optional<Material> findByFileId(String fileId);
    List<Material> findAllByOrderByAddedAtDesc();
}
