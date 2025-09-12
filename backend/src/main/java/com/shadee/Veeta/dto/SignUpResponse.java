package com.shadee.Veeta.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SignUpResponse {
    private String message;
    private UserInfo userInfo;
}
