package com.example.course_registration.DTOs.common;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiResponse<T> {
    private int status;
    private String message;
    private T data;
}