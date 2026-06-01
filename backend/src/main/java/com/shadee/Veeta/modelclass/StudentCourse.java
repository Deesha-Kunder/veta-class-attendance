package com.shadee.Veeta.modelclass;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Data
@Table(name = "student_course")
public class StudentCourse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String studentId;
    @Column(nullable = false)
    private int courseId;
    @Column(nullable = false)
    private String emailId;
    @Column(nullable = false)
    private Long totalCompletedMinutes = 0L;
    @Column(nullable = false)
    private Long requiredMinutes = 0L;
}
