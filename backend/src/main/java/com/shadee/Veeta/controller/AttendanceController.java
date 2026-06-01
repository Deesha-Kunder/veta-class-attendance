package com.shadee.Veeta.controller;

import com.shadee.Veeta.modelclass.AttendanceSession;
import com.shadee.Veeta.security.CustomUserDetails;
import com.shadee.Veeta.service.AttendanceSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("api/attendance")
public class AttendanceController {
    @Autowired
    private AttendanceSessionService attendanceSessionService;

    @GetMapping("/my-session")
    public ResponseEntity<?> getSessions(Authentication authentication){
        System.out.println("Current Time = " + LocalDateTime.now());
        System.out.println("Zone = " + java.time.ZoneId.systemDefault());
        try{
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            String id = customUserDetails.getId();
            return ResponseEntity.ok(
                    attendanceSessionService.getAttendanceSession(id)
            );
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    Map.of(
                            "Message",
                            e.getMessage()
                    )
            );
        }

    }
    @GetMapping("/session/{student_id}")
    public ResponseEntity<?> getSessions(@PathVariable("student_id") String studentId,
            Authentication authentication){
        try{
            return ResponseEntity.ok(
                    attendanceSessionService.getAttendanceSession(studentId)
            );
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    Map.of(
                            "Message",
                            e.getMessage()
                    )
            );
        }

    }
}
