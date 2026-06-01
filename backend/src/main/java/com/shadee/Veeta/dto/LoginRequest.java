package com.shadee.Veeta.dto;

import com.shadee.Veeta.modelclass.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
    String email;
    String password;
    Role role;
}
