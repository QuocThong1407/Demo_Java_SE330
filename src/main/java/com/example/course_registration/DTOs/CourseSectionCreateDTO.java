package com.example.course_registration.DTOs;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseSectionCreateDTO {
    
    @NotNull(message = "Course ID is required")
    private Long courseId;
    
    @NotBlank(message = "Section code is required")
    @Size(min = 2, max = 10, message = "Section code must be between 2 and 10 characters")
    private String sectionCode;
    
    @NotNull(message = "Maximum slots is required")
    @Min(value = 1, message = "Maximum slots must be at least 1")
    @Max(value = 200, message = "Maximum slots cannot exceed 200")
    private Integer maxSlots;
    
    @NotNull(message = "Current slots is required")
    @Min(value = 0, message = "Current slots cannot be negative")
    private Integer currentSlots;
    
    @NotNull(message = "Semester is required")
    @Min(value = 1, message = "Semester must be 1 or 2")
    @Max(value = 2, message = "Semester must be 1 or 2")
    private Integer semester;
    
    @NotNull(message = "Year is required")
    @Min(value = 2020, message = "Year must be 2020 or later")
    @Max(value = 2100, message = "Year cannot exceed 2100")
    private Integer year;
    
    @Pattern(regexp = "^(MONDAY|TUESDAY|WEDNESDAY|THURSDAY|FRIDAY|SATURDAY|SUNDAY)?$",
             message = "Invalid day of week")
    private String dayOfWeek;
    
    @Min(value = 1, message = "Start period must be at least 1")
    @Max(value = 10, message = "Start period cannot exceed 10")
    private Integer startPeriod;
    
    @Min(value = 1, message = "End period must be at least 1")
    @Max(value = 10, message = "End period cannot exceed 10")
    private Integer endPeriod;
    
    @Size(max = 50, message = "Room must be at most 50 characters")
    private String room;
    
    private Long prerequisiteCourseId;
}

