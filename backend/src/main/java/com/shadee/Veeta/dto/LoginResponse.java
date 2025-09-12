package com.shadee.Veeta.dto;

import com.shadee.Veeta.modelclass.Users;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class LoginResponse {
    private String jwtToken;
    private UserInfo userInfo;

}
