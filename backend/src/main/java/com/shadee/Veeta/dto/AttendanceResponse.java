package com.shadee.Veeta.dto;

import com.shadee.Veeta.modelclass.AttendanceSession;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AttendanceResponse {
    public List<AttendanceSession> attendanceSessions;
    public Long totalCompletedMinute;
    public Double totalCompletedHours;
    public Long remainingMinute;
    public Double remainingHours;
}
