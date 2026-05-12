package com.shadee.Veeta.service;

import com.shadee.Veeta.dto.FaceRegisterRequest;
import com.shadee.Veeta.dto.RecognizeRequest;
import com.shadee.Veeta.modelclass.FaceEmbedding;
import com.shadee.Veeta.modelclass.Users;
import com.shadee.Veeta.repository.FaceEmbeddingRepository;
import com.shadee.Veeta.repository.UserRepository;
import jakarta.persistence.Embeddable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
public class FaceService {
    @Autowired
    private FaceEmbeddingRepository repository;

    @Autowired
    private UserRepository userRepository;

    public Boolean registerFace(FaceRegisterRequest request){
        Optional<FaceEmbedding> optional = repository.findByStudentId(request.getStudentId());
        boolean isNew = optional.isEmpty();

        FaceEmbedding entity = optional.orElse(new FaceEmbedding());

        entity.setStudentId(request.getStudentId());
        entity.setName(request.getName());
        entity.setEmbedding(request.getEmbedding());

        repository.save(entity);

        Users user = userRepository.findById(request.getStudentId())
                .orElseThrow(()->new RuntimeException("User not found"));

        user.setFaceRegistered(true);
        userRepository.save(user);
        return isNew;
    }
    public Map<String, Object> recognizeFace(String studentId, RecognizeRequest request){

        FaceEmbedding stored = repository.findByStudentId(studentId)
                .orElseThrow(()->new RuntimeException("Face not registered"));

        System.out.println("calculating score");
        float score = cosineSimilarity(
                stored.getEmbedding(),
                request.getEmbedding()
        );
        if (score > 0.7f) {
            System.out.println("found now returning the result");
            return Map.of(
                    "studentId",stored.getStudentId(),
                    "studentName",stored.getName(),
                    "confidence",score,
                    "status","VERIFIED"
            );
        }
        throw new RuntimeException("Face does not Recognized");
    }
    private float cosineSimilarity(List<Float> a, List<Float> b){
        float dot = 0f;
        float normA = 0f;
        float normB = 0f;

        for(int i = 0; i < a.size(); i++){
            dot += a.get(i) * b.get(i);
            normA += a.get(i) * a.get(i);
            normB += b.get(i) * b.get(i);
        }
        return dot / ((float) (Math.sqrt(normA) * Math.sqrt(normB)));
    }
}
