package com.shadee.Veeta.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentRegisterRequest {
    String name;
    String email;
    int courseHour;
    String batch;
    String profession;
    LocalDate joinedDate;
}
