package com.shadee.Veeta.modelclass;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Data
@Table(
        name = "attendance_session"
)
public class AttendanceSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String studentId;
    private int courseId;
    private Instant checkInTime;
    private Instant checkOutTime;
    private LocalDate date;
    private Long durationMinutes;

}
