package com.shadee.Veeta.controller;

import com.shadee.Veeta.dto.UserInfo;
import com.shadee.Veeta.modelclass.Student;
import com.shadee.Veeta.service.StudentRegisterService;
import com.shadee.Veeta.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/admin/{id}")
    public ResponseEntity<UserInfo> getAdminDataById(@PathVariable String id){
        UserInfo userInfo = userService.getAdminDataById(id);

        return ResponseEntity.ok(userInfo);
    }

    @GetMapping("/student/{id}")
    public ResponseEntity<Student> getStudentDataById(@PathVariable String id) {
        Student student = userService.getStudentFromId(id);
        return ResponseEntity.ok(student);
    }
}
