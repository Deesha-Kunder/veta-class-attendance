package com.shadee.Veeta.repository;

import com.shadee.Veeta.modelclass.FaceEmbedding;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FaceEmbeddingRepository extends JpaRepository<FaceEmbedding,Long> {
    Optional<FaceEmbedding> findByStudentId(String studentId);
}
