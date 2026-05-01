package com.shadee.Veeta.service;

import com.shadee.Veeta.repository.StudentCourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentCourseService {
    @Autowired
    private StudentCourseRepository repository;


}
