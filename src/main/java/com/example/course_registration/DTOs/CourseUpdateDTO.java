package com.example.course_registration.DTOs;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseUpdateDTO {
    
    @Size(min = 3, max = 150, message = "Course name must be between 3 and 150 characters")
    private String courseName;
    
    @Min(value = 1, message = "Credits must be at least 1")
    @Max(value = 12, message = "Credits cannot exceed 12")
    private Integer credits;
    
    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;
    
    private Boolean active;
}
