package com.shadee.Veeta.repository;

import com.shadee.Veeta.modelclass.StudentCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentCourseRepository extends JpaRepository<StudentCourse, Long> {
    StudentCourse findByStudentIdAndCourseId(String studentId, int courseId);
    StudentCourse findByStudentId(String studentId);
    StudentCourse findByEmailId(String emailId);
}
