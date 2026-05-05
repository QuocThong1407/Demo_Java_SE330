package com.example.course_registration.exceptions;

public class InvalidEnrollmentException extends RuntimeException {
    
    public InvalidEnrollmentException(String message) {
        super(message);
    }
    
    public InvalidEnrollmentException(String message, Throwable cause) {
        super(message, cause);
    }
}
