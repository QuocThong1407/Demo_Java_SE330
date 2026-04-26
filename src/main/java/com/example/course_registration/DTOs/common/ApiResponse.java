package com.example.course_registration.DTOs.common;

import lombok.*;
import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) 
public class ApiResponse<T> {
    private int status;
    private String message;

    @Schema(description = "Response data")
    private T data;
}