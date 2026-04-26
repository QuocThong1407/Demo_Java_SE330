package com.example.course_registration.enums;

public enum EnrollmentStatus {
    REGISTERED("Registered"),
    APPROVED("Approved"),
    REJECTED("Rejected"),
    CANCELLED("Cancelled");
    
    private final String displayName;
    
    EnrollmentStatus(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public static EnrollmentStatus fromString(String status) {
        for (EnrollmentStatus enrollmentStatus : EnrollmentStatus.values()) {
            if (enrollmentStatus.name().equalsIgnoreCase(status)) {
                return enrollmentStatus;
            }
        }
        throw new IllegalArgumentException("Invalid enrollment status: " + status);
    }
}
