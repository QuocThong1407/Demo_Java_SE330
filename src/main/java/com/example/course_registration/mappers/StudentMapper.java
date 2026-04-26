package com.example.course_registration.mappers;

import com.example.course_registration.DTOs.StudentCreateDTO;
import com.example.course_registration.DTOs.StudentResponseDTO;
import com.example.course_registration.DTOs.StudentUpdateDTO;
import com.example.course_registration.entities.Student;
import org.springframework.stereotype.Component;

@Component
public class StudentMapper {
    
    /**
     * Convert StudentCreateDTO to Student entity
     */
    public Student toEntity(StudentCreateDTO dto) {
        if (dto == null) {
            return null;
        }
        
        return Student.builder()
                .studentCode(dto.getStudentCode())
                .fullName(dto.getFullName())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .major(dto.getMajor())
                .academicYear(dto.getAcademicYear())
                .totalCredits(0)
                .build();
    }
    
    /**
     * Convert Student entity to StudentResponseDTO
     */
    public StudentResponseDTO toResponseDTO(Student student) {
        if (student == null) {
            return null;
        }
        
        return StudentResponseDTO.builder()
                .id(student.getId())
                .studentCode(student.getStudentCode())
                .fullName(student.getFullName())
                .email(student.getEmail())
                .phone(student.getPhone())
                .major(student.getMajor())
                .academicYear(student.getAcademicYear())
                .totalCredits(student.getTotalCredits())
                .createdAt(student.getCreatedAt())
                .updatedAt(student.getUpdatedAt())
                .build();
    }
    
    /**
     * Update Student entity from StudentUpdateDTO
     */
    public void updateEntity(StudentUpdateDTO dto, Student student) {
        if (dto == null || student == null) {
            return;
        }
        
        if (dto.getFullName() != null && !dto.getFullName().isBlank()) {
            student.setFullName(dto.getFullName());
        }
        
        if (dto.getEmail() != null && !dto.getEmail().isBlank()) {
            student.setEmail(dto.getEmail());
        }
        
        if (dto.getPhone() != null && !dto.getPhone().isBlank()) {
            student.setPhone(dto.getPhone());
        }
        
        if (dto.getMajor() != null && !dto.getMajor().isBlank()) {
            student.setMajor(dto.getMajor());
        }
        
        if (dto.getAcademicYear() != null) {
            student.setAcademicYear(dto.getAcademicYear());
        }
        
        if (dto.getTotalCredits() != null) {
            student.setTotalCredits(dto.getTotalCredits());
        }
    }
}
