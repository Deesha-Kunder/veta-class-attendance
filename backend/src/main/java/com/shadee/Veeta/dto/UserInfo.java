package com.shadee.Veeta.dto;

import com.shadee.Veeta.modelclass.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserInfo {
    private Long id;
    private String username;
    private String email;
    private Role role;
}
