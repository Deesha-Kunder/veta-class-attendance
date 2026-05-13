package com.shadee.Veeta.service;

import com.shadee.Veeta.dto.StudentListResponse;
import com.shadee.Veeta.dto.StudentRegisterRequest;
import com.shadee.Veeta.modelclass.Student;
import com.shadee.Veeta.modelclass.StudentCourse;
import com.shadee.Veeta.repository.StudentCourseRepository;
import com.shadee.Veeta.repository.StudentRegisterRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentRegisterService {

    private final StudentRegisterRepository repository;

    private final StudentCourseRepository studentCourseRepository;

    @Transactional
    public Student RegisterStudent(StudentRegisterRequest student) {
        System.out.println(student);
        if (repository.existsByEmail(student.getEmail())) {
            throw new RuntimeException("Student already exist with this email");
        }

        Student newStudent = new Student();
        newStudent.setName(student.getName());
        newStudent.setEmail(student.getEmail());
        newStudent.setProfession(student.getProfession());
        newStudent.setBatch(student.getBatch());
        newStudent.setCourseHour(student.getCourseHour());
        newStudent.setJoinedDate(student.getJoinedDate());

        return repository.save(newStudent);
    }

    public Student findStudentByEmail(String email) {
        return repository.findByEmail(email).orElseThrow(() -> new RuntimeException("Student not found"));
    }

    public Student getStudentDataById(Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public List<StudentListResponse> getAllStudents() {

        List<Student> students = repository.findAll();

        return students.stream().map(student -> {

            StudentCourse sc =
                    studentCourseRepository.findByEmailId(student.getEmail());

            double remainingHours = 0;
            double completedHours = 0;

            if (sc != null) {

                Long completed = sc.getTotalCompletedMinutes();
                Long required = sc.getRequiredMinutes();

                completedHours = completed / 60.0;

                remainingHours =
                        (required - completed) / 60.0;
            }

            return new StudentListResponse(
                    student.getName(),
                    student.getEmail(),
                    remainingHours,
                    completedHours
            );

        }).toList();
    }
}
