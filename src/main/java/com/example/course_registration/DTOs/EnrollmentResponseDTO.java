package com.example.course_registration.DTOs;

import com.example.course_registration.entities.EnrollmentStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnrollmentResponseDTO {
    
    private Long id;
    
    private Long studentId;
    
    private Long sectionId;
    
    private StudentBasicDTO student;
    
    private CourseSectionBasicDTO courseSection;
    
    private EnrollmentStatus status;
    
    @JsonFormat(shape = com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime registeredAt;
    
    @JsonFormat(shape = com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
    
    /**
     * Basic Student DTO for enrollment response
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class StudentBasicDTO {
        private Long id;
        private String studentCode;
        private String fullName;
        private String email;
        private Integer academicYear;
    }
    
    /**
     * Basic Course Section DTO for enrollment response
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CourseSectionBasicDTO {
        private Long id;
        private String sectionCode;
        private Integer currentSlots;
        private Integer maxSlots;
        private String dayOfWeek;
        private Integer startPeriod;
        private Integer endPeriod;
        private String room;
    }
}
