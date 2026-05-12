package com.shadee.Veeta.service;

import com.shadee.Veeta.dto.AttendanceResponse;
import com.shadee.Veeta.modelclass.AttendanceSession;
import com.shadee.Veeta.modelclass.StudentCourse;
import com.shadee.Veeta.repository.AttendanceSessionRepository;
import com.shadee.Veeta.repository.StudentCourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class AttendanceSessionService {
    @Autowired
    private AttendanceSessionRepository attendanceSessionRepository;

    @Autowired
    private StudentCourseRepository studentCourseRepository;

    public Map<String,Object> markAttendance(String studentId, int courseId){
        System.out.println("STEP 1");
        AttendanceSession openSession = attendanceSessionRepository.findTopByStudentIdAndDateAndCheckOutTimeIsNull(studentId, LocalDate.now())
                .orElse(null);
        System.out.println("STEP 2");
        if(openSession != null){
            System.out.println("OPEN SESSION FOUND");
        }else{
            System.out.println("NO OPEN SESSION");
        }

        if(openSession == null){
            System.out.println("CREATING CHECKIN");
            AttendanceSession session = new AttendanceSession();
            session.setStudentId(studentId);
            session.setCourseId(courseId);
            session.setCheckInTime(LocalDateTime.now());
            session.setDate(LocalDate.now());

            attendanceSessionRepository.save(session);
            System.out.println("CHECKIN SAVED");
            return Map.of(
                    "type","CHECK-IN",
                    "message","Checked in successfully",
                    "time",session.getCheckInTime()
            );
        }
        if(openSession.getCheckOutTime  () == null){
            System.out.println("UPDATING CHECKOUT");
            LocalDateTime time = LocalDateTime.now();
            openSession.setCheckOutTime(time);

            long duration = Duration.between(openSession.getCheckInTime(), time).toMinutes();

            openSession.setDurationMinutes(duration);
            attendanceSessionRepository.save(openSession);
            System.out.println("CHECKOUT SAVED");

            //update the progress

            StudentCourse sc = studentCourseRepository.findByStudentIdAndCourseId(studentId,courseId);
            System.out.println("STUDENT COURSE FOUND");
            sc.setTotalCompletedMinutes(sc.getTotalCompletedMinutes() + duration);
            studentCourseRepository.save(sc);

            System.out.println("STUDENT COURSE UPDATED");

            return Map.of(
                    "type","CHECK-OUT",
                    "message","Checked out successfully",
                    "time",openSession.getCheckOutTime()
            );
        }
        throw new RuntimeException(
                "Attendance already completed for today"
        );
    }
    public AttendanceResponse getAttendanceSession(String studentId){
        List<AttendanceSession>sessions = attendanceSessionRepository.findByStudentIdOrderByDateDesc(studentId);
        if(sessions.isEmpty()){
            throw new RuntimeException("No Session found");
        }
        StudentCourse sc = studentCourseRepository.findByStudentId(studentId);
        Long completed = sc.getTotalCompletedMinutes();
        Long required = sc.getRequiredMinutes();
        long remaining = required - completed;
        Double completedHours = completed / 60.0;
        Double remainingHours = remaining / 60.0;
        return new AttendanceResponse(
                sessions, completed, completedHours, remaining, remainingHours
        );
    }
}
