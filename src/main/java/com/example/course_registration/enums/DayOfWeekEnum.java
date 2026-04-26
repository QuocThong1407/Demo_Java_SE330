package com.example.course_registration.enums;

public enum DayOfWeekEnum {
    MON("Monday", 1),
    TUE("Tuesday", 2),
    WED("Wednesday", 3),
    THU("Thursday", 4),
    FRI("Friday", 5),
    SAT("Saturday", 6);
    
    private final String displayName;
    private final int dayNumber;
    
    DayOfWeekEnum(String displayName, int dayNumber) {
        this.displayName = displayName;
        this.dayNumber = dayNumber;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public int getDayNumber() {
        return dayNumber;
    }
    
    public static DayOfWeekEnum fromString(String day) {
        for (DayOfWeekEnum dayOfWeek : DayOfWeekEnum.values()) {
            if (dayOfWeek.name().equalsIgnoreCase(day) || 
                dayOfWeek.displayName.equalsIgnoreCase(day)) {
                return dayOfWeek;
            }
        }
        throw new IllegalArgumentException("Invalid day of week: " + day);
    }
    
    public static DayOfWeekEnum fromDayNumber(int dayNumber) {
        for (DayOfWeekEnum dayOfWeek : DayOfWeekEnum.values()) {
            if (dayOfWeek.dayNumber == dayNumber) {
                return dayOfWeek;
            }
        }
        throw new IllegalArgumentException("Invalid day number: " + dayNumber);
    }
}
