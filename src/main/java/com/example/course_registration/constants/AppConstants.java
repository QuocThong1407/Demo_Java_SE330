package com.example.course_registration.constants;

public class AppConstants {
    
    // API Version
    public static final String API_VERSION = "v1";
    public static final String API_BASE_PATH = "/api/" + API_VERSION;
    
    // Pagination
    public static final int DEFAULT_PAGE_SIZE = 10;
    public static final int DEFAULT_PAGE_NUMBER = 0;
    
    // Validation
    public static final int MIN_STUDENT_CODE_LENGTH = 3;
    public static final int MAX_STUDENT_CODE_LENGTH = 20;
    public static final int MIN_NAME_LENGTH = 2;
    public static final int MAX_NAME_LENGTH = 100;
    public static final int MAX_EMAIL_LENGTH = 100;
    public static final int MAX_PHONE_LENGTH = 20;
    public static final int MAX_MAJOR_LENGTH = 100;
    public static final int MIN_ACADEMIC_YEAR = 1;
    public static final int MAX_ACADEMIC_YEAR = 4;
    
    // HTTP Status Messages
    public static final String SUCCESS = "Success";
    public static final String ERROR = "Error";
    public static final String NOT_FOUND = "Resource not found";
    public static final String DUPLICATE = "Resource already exists";
    public static final String VALIDATION_ERROR = "Validation error";
    
    // Entity Names
    public static final String STUDENT_ENTITY = "Student";
    public static final String COURSE_ENTITY = "Course";
    public static final String ENROLLMENT_ENTITY = "Enrollment";
    
    // Field Names
    public static final String STUDENT_CODE_FIELD = "student_code";
    public static final String EMAIL_FIELD = "email";
    
    // Academic Year Constants
    public static final String FRESHMAN = "Freshman";
    public static final String SOPHOMORE = "Sophomore";
    public static final String JUNIOR = "Junior";
    public static final String SENIOR = "Senior";
}
