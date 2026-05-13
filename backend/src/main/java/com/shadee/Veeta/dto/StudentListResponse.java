package com.shadee.Veeta.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StudentListResponse {
    private String name;
    private String email;
    private Double remainingHour;
    private Double totalCompletedHours;
}
