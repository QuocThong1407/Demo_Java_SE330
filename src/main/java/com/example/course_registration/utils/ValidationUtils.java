package com.example.course_registration.utils;

import java.util.regex.Pattern;

public class ValidationUtils {
    
    private static final Pattern EMAIL_PATTERN = 
            Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    
    private static final Pattern PHONE_PATTERN = 
            Pattern.compile("^[+]?[0-9]{7,20}$");
    
    private static final Pattern STUDENT_CODE_PATTERN = 
            Pattern.compile("^[A-Z0-9]{3,20}$");
    
    /**
     * Validate email format
     */
    public static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email).matches();
    }
    
    /**
     * Validate phone number format
     */
    public static boolean isValidPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return false;
        }
        return PHONE_PATTERN.matcher(phone).matches();
    }
    
    /**
     * Validate student code format
     */
    public static boolean isValidStudentCode(String studentCode) {
        if (studentCode == null || studentCode.trim().isEmpty()) {
            return false;
        }
        return STUDENT_CODE_PATTERN.matcher(studentCode).matches();
    }
    
    /**
     * Validate academic year
     */
    public static boolean isValidAcademicYear(Integer year) {
        return year != null && year >= 1 && year <= 4;
    }
    
    /**
     * Validate string is not empty
     */
    public static boolean isNotEmpty(String str) {
        return str != null && !str.trim().isEmpty();
    }
    
    /**
     * Validate positive integer
     */
    public static boolean isPositive(Integer number) {
        return number != null && number > 0;
    }
}
