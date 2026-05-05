package com.example.course_registration.DTOs;

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
public class CourseSectionResponseDTO {
    
    private Long id;
    
    private Long courseId;

    private CourseBasicInfoDTO course;
    
    private String sectionCode;
    
    private Integer maxSlots;
    
    private Integer currentSlots;
    
    private Integer semester;
    
    private Integer year;
    
    private String dayOfWeek;
    
    private Integer startPeriod;
    
    private Integer endPeriod;
    
    private String room;
    
    private Long prerequisiteCourseId;
    
    @JsonFormat(shape = com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    
    @JsonFormat(shape = com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CourseBasicInfoDTO {
        private Long id;
        private String courseCode;
        private String courseName;
        private Integer credits;
    }
}