package com.example.course_registration.mappers;

import com.example.course_registration.DTOs.CourseCreateDTO;
import com.example.course_registration.DTOs.CourseResponseDTO;
import com.example.course_registration.DTOs.CourseUpdateDTO;
import com.example.course_registration.entities.Course;
import org.springframework.stereotype.Component;

@Component
public class CourseMapper {
    
    /**
     * Convert CourseCreateDTO to Course entity
     */
    public Course toEntity(CourseCreateDTO dto) {
        if (dto == null) {
            return null;
        }
        
        return Course.builder()
                .courseCode(dto.getCourseCode())
                .courseName(dto.getCourseName())
                .credits(dto.getCredits())
                .description(dto.getDescription())
                .active(dto.getActive() != null ? dto.getActive() : true)
                .build();
    }
    
    /**
     * Convert Course entity to CourseResponseDTO
     */
    public CourseResponseDTO toResponseDTO(Course course) {
        if (course == null) {
            return null;
        }
        
        return CourseResponseDTO.builder()
                .id(course.getId())
                .courseCode(course.getCourseCode())
                .courseName(course.getCourseName())
                .credits(course.getCredits())
                .description(course.getDescription())
                .active(course.getActive())
                .createdAt(course.getCreatedAt())
                .build();
    }
    
    /**
     * Update Course entity from CourseUpdateDTO
     */
    public void updateEntity(CourseUpdateDTO dto, Course course) {
        if (dto == null || course == null) {
            return;
        }
        
        if (dto.getCourseName() != null && !dto.getCourseName().isBlank()) {
            course.setCourseName(dto.getCourseName());
        }
        
        if (dto.getCredits() != null) {
            course.setCredits(dto.getCredits());
        }
        
        if (dto.getDescription() != null && !dto.getDescription().isBlank()) {
            course.setDescription(dto.getDescription());
        }
        
        if (dto.getActive() != null) {
            course.setActive(dto.getActive());
        }
    }
}
