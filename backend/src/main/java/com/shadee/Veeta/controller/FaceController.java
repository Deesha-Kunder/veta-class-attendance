package com.shadee.Veeta.controller;

import com.shadee.Veeta.dto.FaceRegisterRequest;
import com.shadee.Veeta.dto.RecognizeRequest;
import com.shadee.Veeta.service.FaceService;
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

    @PostMapping("/register")
    public ResponseEntity<?> registerFace(
            @RequestBody FaceRegisterRequest request) {
        service.registerFace(request);
        return ResponseEntity.ok(
                Map.of(
                        "success", true,
                        "message", "Registered"
                )
        );
    }

    @PostMapping("/recognize")
    public ResponseEntity<?> recognizeFace(
            @RequestBody RecognizeRequest request
    ) {
        try {
            return ResponseEntity.ok(service.recognizeFace(request));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(
                    Map.of(
                            "message", e.getMessage()
                    )
            );
        }
    }
}
