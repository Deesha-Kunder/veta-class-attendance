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
    public Student RegisterStudent(StudentRegisterRequest student, String adminId) {
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
        newStudent.setRegisteredBy(adminId);

        StudentCourse sc = new StudentCourse();
        sc.setEmailId(newStudent.getEmail());
        sc.setCourseId(1);

        sc.setRequiredMinutes(newStudent.getCourseHour()* 60L);
        studentCourseRepository.save(sc);

        return repository.save(newStudent);
    }

    public Student findStudentByEmail(String email) {
        return repository.findByEmail(email).orElseThrow(() -> new RuntimeException("Student not found"));
    }

    public Student getStudentDataById(Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public List<StudentListResponse> getRegisteredStudents(String userId) {

        List<Student> students = repository.findByRegisteredBy(userId);

        return students.stream().map(student -> {

            StudentCourse sc =
                    studentCourseRepository.findByEmailId(student.getEmail());

            System.out.println("Student Email: " + student.getEmail());
            System.out.println("StudentCourse: " + sc);

            double remainingHours = 0;
            double completedHours = 0;
            String id = "";

            if (sc != null) {

                id = sc.getStudentId();
                Long completed = sc.getTotalCompletedMinutes();
                Long required = sc.getRequiredMinutes();

                completedHours = completed / 60.0;

                remainingHours =
                        (required - completed) / 60.0;
            }

            return new StudentListResponse(
                    id,
                    student.getName(),
                    student.getEmail(),
                    remainingHours,
                    completedHours
            );

        }).toList();
    }
}
