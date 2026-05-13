package com.shadee.Veeta.controller;

import com.shadee.Veeta.service.StatusService;
import com.shadee.Veeta.utils.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
public class StatusController {

    @Autowired
    private JwtUtils jw;

    @Autowired
    private StatusService statusService;

    @GetMapping("/status")
    public ResponseEntity<?> getStatus(HttpServletRequest request) {

        String token = request.getHeader("Authorization").substring(7);
        String studentId = jw.getUserIdFromToken(token);

        return ResponseEntity.ok(statusService.getStudentStatus(studentId));
    }
}
