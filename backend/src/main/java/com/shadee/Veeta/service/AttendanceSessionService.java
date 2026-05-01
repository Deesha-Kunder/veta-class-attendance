package com.shadee.Veeta.service;

import com.shadee.Veeta.modelclass.AttendanceSession;
import com.shadee.Veeta.modelclass.StudentCourse;
import com.shadee.Veeta.repository.AttendanceSessionRepository;
import com.shadee.Veeta.repository.StudentCourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

@Service
public class AttendanceSessionService {
    @Autowired
    private AttendanceSessionRepository attendanceSessionRepository;

    @Autowired
    private StudentCourseRepository studentCourseRepository;

    public Map<String,Object> markAttendance(String studentId, int courseId){
        AttendanceSession openSession = attendanceSessionRepository.findTopByStudentIdAndCheckOutTimeIsNull(studentId)
                .orElse(null);

        if(openSession == null){
            AttendanceSession session = new AttendanceSession();
            session.setStudentId(studentId);
            session.setCourseId(courseId);
            session.setCheckInTime(LocalDateTime.now());
            session.setDate(LocalDate.now());

            attendanceSessionRepository.save(session);
            return Map.of(
                    "type","CHECK-IN",
                    "message","Checked in successfully",
                    "time",session.getCheckInTime()
            );
        }else{
            LocalDateTime time = LocalDateTime.now();
            openSession.setCheckOutTime(time);

            long duration = Duration.between(openSession.getCheckInTime(), time).toMinutes();

            openSession.setDurationMinutes(duration);
            attendanceSessionRepository.save(openSession);

            //update the progress

            StudentCourse sc = studentCourseRepository.findByStudentIdAndCourseId(studentId,courseId);
            sc.setTotalCompletedMinutes(sc.getTotalCompletedMinutes() + duration);
            studentCourseRepository.save(sc);

            return Map.of(
                    "type","CHECK-OUT",
                    "message","Checked out successfully",
                    "time",openSession.getCheckOutTime()
            );
        }
    }
}
