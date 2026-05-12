package com.shadee.Veeta.controller;

import com.shadee.Veeta.dto.UserInfo;
import com.shadee.Veeta.modelclass.Student;
import com.shadee.Veeta.security.CustomUserDetails;
import com.shadee.Veeta.service.StudentRegisterService;
import com.shadee.Veeta.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/onboarding")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private StudentRegisterService studentRegisterService;

    @GetMapping("/admin")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<UserInfo> getAdminDataById(Authentication authentication) {
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        String userId = customUserDetails.getId();
        UserInfo userInfo = userService.getAdminDataById(userId);

        return ResponseEntity.ok(userInfo);
    }

    @GetMapping("/student")
    @PreAuthorize("hasAuthority('STUDENT')")
    public ResponseEntity<Student> getStudentDataById(Authentication authentication) {
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        String userId = customUserDetails.getId();
        Student student = userService.getStudentFromId(userId);
        return ResponseEntity.ok(student);
    }
}
