package com.example.course_registration.data;

import com.example.course_registration.entities.Course;
import com.example.course_registration.entities.CourseSection;
import com.example.course_registration.repositories.CourseRepository;
import com.example.course_registration.repositories.CourseSectionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
@Order(2) // Ensure this runs after CourseDataSeed
public class CourseSectionDataSeed implements CommandLineRunner {
    
    private final CourseSectionRepository courseSectionRepository;
    private final CourseRepository courseRepository;
    
    @Override
    public void run(String... args) throws Exception {
        // Check if sections already exist
        if (courseSectionRepository.count() > 0) {
            log.info("Sample course section data already exists. Skipping seed data initialization.");
            return;
        }
        
        log.info("Initializing sample course section data...");
        
        // Load courses - they should be pre-seeded
        Course cs101 = courseRepository.findByCourseCode("CS101").orElse(null);
        Course cs102 = courseRepository.findByCourseCode("CS102").orElse(null);
        Course cs201 = courseRepository.findByCourseCode("CS201").orElse(null);
        Course db101 = courseRepository.findByCourseCode("DB101").orElse(null);
        Course web101 = courseRepository.findByCourseCode("WEB101").orElse(null);
        Course web201 = courseRepository.findByCourseCode("WEB201").orElse(null);
        Course se101 = courseRepository.findByCourseCode("SE101").orElse(null);
        Course oop101 = courseRepository.findByCourseCode("OOP101").orElse(null);
        
        // Create sample sections for CS101 - Introduction to Programming (No prerequisite)
        createSection("CS101-01", 30, 0, 1, 2026, "MONDAY", 1, 2, "A101", cs101, null);
        createSection("CS101-02", 30, 0, 1, 2026, "WEDNESDAY", 3, 4, "A102", cs101, null);
        
        // Create sample sections for CS102 - Data Structures (Prerequisite: CS101)
        createSection("CS102-01", 25, 0, 1, 2026, "TUESDAY", 5, 6, "B201", cs102, cs101);
        
        // Create sample sections for CS201 - Algorithms (Prerequisite: CS102)
        createSection("CS201-01", 20, 0, 2, 2026, "THURSDAY", 1, 2, "B301", cs201, cs102);
        
        // Create sample sections for DB101 - Database Design (No prerequisite)
        createSection("DB101-01", 25, 0, 1, 2026, "TUESDAY", 1, 5, "C101", db101, null);
        createSection("DB101-02", 25, 0, 2, 2026, "FRIDAY", 2, 3, "C102", db101, null);
        
        // Create sample sections for WEB101 - Web Development Basics (Prerequisite: CS101)
        createSection("WEB101-01", 30, 0, 1, 2026, "MONDAY", 9, 10, "D101", web101, cs101);
        
        // Create sample sections for WEB201 - Advanced Web Development (Prerequisite: WEB101)
        createSection("WEB201-01", 25, 0, 2, 2026, "WEDNESDAY", 5, 6, "D201", web201, web101);
        
        // Create sample sections for SE101 - Software Engineering (Prerequisite: CS101)
        createSection("SE101-01", 20, 0, 1, 2026, "THURSDAY", 3, 4, "E101", se101, cs101);
        
        // Create sample sections for OOP101 - Object-Oriented Programming (Prerequisite: CS101)
        createSection("OOP101-01", 28, 0, 2, 2026, "FRIDAY", 7, 8, "E201", oop101, cs101);
        
        log.info("Sample course section data has been successfully initialized. Total: {} sections", 
                 courseSectionRepository.count());
    }
    
    private void createSection(String sectionCode, Integer maxSlots, Integer currentSlots,
                               Integer semester, Integer year, String dayOfWeek,
                               Integer startPeriod, Integer endPeriod, String room,
                               Course course, Course prerequisiteCourse) {
        CourseSection section = CourseSection.builder()
                .sectionCode(sectionCode)
                .maxSlots(maxSlots)
                .currentSlots(currentSlots)
                .semester(semester)
                .year(year)
                .dayOfWeek(dayOfWeek)
                .startPeriod(startPeriod)
                .endPeriod(endPeriod)
                .room(room)
                .course(course)
                .prerequisiteCourse(prerequisiteCourse)
                .build();
        courseSectionRepository.save(section);
    }
}
