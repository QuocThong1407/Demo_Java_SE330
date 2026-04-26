package com.example.course_registration.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.course_registration.entities.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {
    
}
