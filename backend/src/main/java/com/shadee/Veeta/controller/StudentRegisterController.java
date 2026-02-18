package com.shadee.Veeta.controller;

import com.shadee.Veeta.dto.StudentRegisterRequest;
import com.shadee.Veeta.modelclass.Student;
import com.shadee.Veeta.service.StudentRegisterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/admin")
@RequiredArgsConstructor
public class StudentRegisterController {

    private final StudentRegisterService service;

    @PostMapping("/register-student")
    public ResponseEntity<?> registerStudent(@RequestBody StudentRegisterRequest request){
        try{
            Student student = service.RegisterStudent(request);
            Map<String,Object> response = new HashMap<>();
            response.put("success",true);
            response.put("message","Student registered successfully");
            response.put("user_data",student);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }catch (Exception e){
            Map<String,Object> errorResponse = new HashMap<>();
            errorResponse.put("success",false);
            errorResponse.put("message","Failed to register");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

    }
}
