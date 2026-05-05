package com.example.course_registration.DTOs;

import com.example.course_registration.entities.EnrollmentStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnrollmentUpdateDTO {
    
    @NotNull(message = "Status is required")
    private EnrollmentStatus status;
}
