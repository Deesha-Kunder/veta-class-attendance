package com.shadee.Veeta.repository;

import com.shadee.Veeta.modelclass.FaceEmbedding;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FaceEmbeddingRepository extends JpaRepository<FaceEmbedding,Long> {
    
}
