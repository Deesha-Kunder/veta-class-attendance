package com.shadee.Veeta.dto;

import com.shadee.Veeta.modelclass.Role;
import lombok.Data;

@Data
public class SignUpRequest {
    private String username;
    private String email;
    private String password;
    private Role role;
}
