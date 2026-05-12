package com.shadee.Veeta.repository;

import com.shadee.Veeta.modelclass.AttendanceSession;
import com.shadee.Veeta.modelclass.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class StatusService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AttendanceSessionRepository attendanceSessionRepository;

    public Map<String,Object> getStudentStatus(String studentId){
        Users user = userRepository.findById(studentId)
                .orElseThrow(()->new RuntimeException("User not found"));
        boolean faceRegistered = user.isFaceRegistered();

        Optional<AttendanceSession> attendance = attendanceSessionRepository.findByStudentIdAndDate(studentId, LocalDate.now());

        boolean checkIn = false;
        boolean checkOut = false;
        LocalDateTime checkInTime= null;
        LocalDateTime checkOutTime= null;


        if(attendance.isPresent()){
            checkIn = attendance.get().getCheckInTime() != null;
            checkOut = attendance.get().getCheckOutTime() != null;
            checkInTime = attendance.get().getCheckInTime();
            checkOutTime = attendance.get().getCheckOutTime();
        }
        Map<String,Object> response = new HashMap<>();
        response.put("faceRegistered",faceRegistered);
        response.put("checkIn",checkIn);
        response.put("checkOut",checkOut);
        response.put("checkInTime",checkInTime);
        response.put("checkOutTime",checkOutTime);
        return response;
    }
}
