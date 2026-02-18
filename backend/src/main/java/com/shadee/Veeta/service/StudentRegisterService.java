package com.shadee.Veeta.service;

import com.shadee.Veeta.dto.StudentRegisterRequest;
import com.shadee.Veeta.modelclass.Student;
import com.shadee.Veeta.repository.StudentRegisterRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudentRegisterService {

    private final StudentRegisterRepository repository;

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
    public Student getStudentDataById(Long id){
        return repository.findById(id).orElseThrow(()-> new RuntimeException("User not found"));
    }
}
