package com.shadee.Veeta.dto;

import lombok.Data;

import java.util.List;

@Data
public class FaceRegisterRequest {
    private String studentId;
    private String name;
    private List<Float> embedding;
}
