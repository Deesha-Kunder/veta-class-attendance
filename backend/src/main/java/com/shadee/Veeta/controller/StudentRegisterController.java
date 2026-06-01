package com.shadee.Veeta.controller;

import com.shadee.Veeta.dto.StudentListResponse;
import com.shadee.Veeta.dto.StudentRegisterRequest;
import com.shadee.Veeta.modelclass.Student;
import com.shadee.Veeta.security.CustomUserDetails;
import com.shadee.Veeta.service.StudentRegisterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/admin")
@RequiredArgsConstructor
public class StudentRegisterController {

    private final StudentRegisterService service;

    @PostMapping("/register-student")
    public ResponseEntity<?> registerStudent(@RequestBody StudentRegisterRequest request, Authentication authentication){
        CustomUserDetails customUserDetails =(CustomUserDetails) authentication.getPrincipal();
        String adminId = customUserDetails.getId();
        try{
            Student student = service.RegisterStudent(request, adminId);
            Map<String,Object> response = new HashMap<>();
            response.put("success",true);
            response.put("message","Student registration successful");
            response.put("user_data",student);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }catch (RuntimeException e){
            Map<String,Object> errorResponse = new HashMap<>();
            errorResponse.put("success",false);
            errorResponse.put("message",e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }catch (Exception e) {

            Map<String,Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Student registration failed");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(response);
        }

    }

    //gets the students only registered by admin
    @GetMapping("/registered-students")
    public ResponseEntity<List<StudentListResponse>> getRegisteredStudents(Authentication authentication){
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        String userId = customUserDetails.getId();
        List<StudentListResponse> students = service.getRegisteredStudents(userId);
        return ResponseEntity.ok(students);
    }
}
