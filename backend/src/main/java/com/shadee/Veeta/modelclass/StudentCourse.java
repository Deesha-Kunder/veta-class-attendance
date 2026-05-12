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
    private String studentId;
    private int courseId;
    private String emailId;
    private Long totalCompletedMinutes;
    private Long requiredMinutes;
}
