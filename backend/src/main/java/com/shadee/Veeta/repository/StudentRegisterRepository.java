package com.shadee.Veeta.repository;

import com.shadee.Veeta.modelclass.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRegisterRepository extends JpaRepository<Student,Long> {
    Boolean existsByEmail(String email);
    Optional<Student> findByEmail(String email);
    List<Student> findByRegisteredBy(String registeredBy);
}
