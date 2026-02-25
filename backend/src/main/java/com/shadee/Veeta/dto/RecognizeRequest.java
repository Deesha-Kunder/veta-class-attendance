package com.shadee.Veeta.dto;

import lombok.Data;

import java.util.List;

@Data
public class RecognizeRequest {
    private List<Float> embedding;
}
