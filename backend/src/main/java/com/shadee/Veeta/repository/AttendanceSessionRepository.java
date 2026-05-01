package com.shadee.Veeta.repository;

import com.shadee.Veeta.modelclass.AttendanceSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AttendanceSessionRepository extends JpaRepository<AttendanceSession,Long> {
    Optional<AttendanceSession> findTopByStudentIdAndCheckOutTimeIsNull(String studentId);
}
