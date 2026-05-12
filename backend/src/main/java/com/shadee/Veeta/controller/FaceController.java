package com.shadee.Veeta.controller;

import com.shadee.Veeta.dto.FaceRegisterRequest;
import com.shadee.Veeta.dto.RecognizeRequest;
import com.shadee.Veeta.modelclass.AttendanceSession;
import com.shadee.Veeta.service.AttendanceSessionService;
import com.shadee.Veeta.service.FaceService;
import com.shadee.Veeta.utils.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/face")
public class FaceController {
    @Autowired
    private FaceService service;

    @Autowired
    private JwtUtils jw;

    @Autowired
    private AttendanceSessionService attendanceSessionService;

    @PostMapping("/register")
    public ResponseEntity<?> registerFace(
            @RequestBody FaceRegisterRequest request) {
        boolean isNew = service.registerFace(request);
        return ResponseEntity.ok(
                Map.of(
                        "success", true,
                        "message", isNew ? "Face registered" : "Face updated"
                )
        );
    }

    @PostMapping("/recognize")
    public ResponseEntity<?> recognizeFace(
            @RequestBody RecognizeRequest request,
            HttpServletRequest httpServletRequest
    ) {
        System.out.println("reached recognize");
        String authHeader = httpServletRequest.getHeader("Authorization");
        String token = authHeader.substring(7);

        String studentId = jw.getUserIdFromToken(token);

        try {
            //Face Verification
            System.out.println("trying to recognize face");
            Map<String, Object> faceResult = service.recognizeFace(studentId, request);

            // Mark Attendance
            System.out.println("marking attendance session");
            Map<String, Object> attendanceResult = attendanceSessionService.markAttendance(studentId, 1);

            return ResponseEntity.ok(
                    Map.of(
                            "faceResult",faceResult,
                            "AttendanceResult", attendanceResult
                    )
            );
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(
                    Map.of(
                            "message", e.getMessage()
                    )
            );
        }
    }
}
