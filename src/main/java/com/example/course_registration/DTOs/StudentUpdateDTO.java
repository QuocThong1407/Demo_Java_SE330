package com.example.course_registration.DTOs;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentUpdateDTO {
    
    @Size(min = 2, max = 100, message = "Full name must be between 2 and 100 characters")
    private String fullName;
    
    @Email(message = "Email should be valid")
    private String email;
    
    @Pattern(regexp = "^[+]?[0-9]{7,20}$", message = "Phone number should be valid")
    private String phone;
    
    private String major;
    
    @Min(value = 1, message = "Academic year must be at least 1")
    @Max(value = 4, message = "Academic year cannot exceed 4")
    private Integer academicYear;
    
    @Min(value = 0, message = "Total credits cannot be negative")
    private Integer totalCredits;
}
