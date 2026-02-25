package com.shadee.Veeta.service;

import com.shadee.Veeta.dto.FaceRegisterRequest;
import com.shadee.Veeta.dto.RecognizeRequest;
import com.shadee.Veeta.modelclass.FaceEmbedding;
import com.shadee.Veeta.repository.FaceEmbeddingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class FaceService {
    @Autowired
    private FaceEmbeddingRepository repository;

    public void registerFace(FaceRegisterRequest request){
        FaceEmbedding entity = new FaceEmbedding();
        entity.setStudentId(request.getStudentId());
        entity.setName(request.getName());
        entity.setEmbedding(request.getEmbedding());

        repository.save(entity);
    }
    public Map<String, Object> recognizeFace(RecognizeRequest request){
        float bestScore = 0f;
        FaceEmbedding bestMatch = null;
        List<FaceEmbedding> all = repository.findAll();
        for(FaceEmbedding stored :all){
            float score = cosineSimilarity(
                    stored.getEmbedding(),
                    request.getEmbedding()
            );
            if(score > bestScore){
                bestScore = score;
                bestMatch = stored;
            }
        }
        if(bestMatch != null && bestScore > 0.7f){
            return Map.of(
                    "studentId",bestMatch.getStudentId(),
                    "studentName",bestMatch.getName(),
                    "confidence",bestScore
            );
        }
        throw new RuntimeException("Not Recognized");
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
