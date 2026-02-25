package com.shadee.Veeta.modelclass;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "face_embeddings")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FaceEmbedding {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String studentId;

    private String name;

    @ElementCollection
    @CollectionTable(name = "embedding_value",
    joinColumns = @JoinColumn(name = "face_id"))
    @Column(name = "value")
    private List<Float> embedding;
}
