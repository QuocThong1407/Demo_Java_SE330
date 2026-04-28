package com.example.course_registration.data;

import com.example.course_registration.entities.Course;
import com.example.course_registration.repositories.CourseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CourseDataSeed implements CommandLineRunner {
    
    private final CourseRepository courseRepository;
    
    @Override
    public void run(String... args) throws Exception {
        // Check if courses already exist
        if (courseRepository.count() > 0) {
            log.info("Sample course data already exists. Skipping seed data initialization.");
            return;
        }
        
        log.info("Initializing sample course data...");
        
        // Create sample courses
        Course course1 = Course.builder()
                .courseCode("CS101")
                .courseName("Introduction to Programming")
                .credits(3)
                .description("Fundamental concepts of programming using Java")
                .active(true)
                .build();
        
        Course course2 = Course.builder()
                .courseCode("CS102")
                .courseName("Data Structures")
                .credits(3)
                .description("Study of arrays, linked lists, stacks, queues, and trees")
                .active(true)
                .build();
        
        Course course3 = Course.builder()
                .courseCode("CS201")
                .courseName("Algorithms")
                .credits(3)
                .description("Algorithm design and complexity analysis")
                .active(true)
                .build();
        
        Course course4 = Course.builder()
                .courseCode("DB101")
                .courseName("Database Design")
                .credits(3)
                .description("Relational database design and SQL")
                .active(true)
                .build();
        
        Course course5 = Course.builder()
                .courseCode("WEB101")
                .courseName("Web Development Basics")
                .credits(3)
                .description("HTML, CSS, and JavaScript fundamentals")
                .active(true)
                .build();
        
        Course course6 = Course.builder()
                .courseCode("WEB201")
                .courseName("Advanced Web Development")
                .credits(3)
                .description("Spring Boot, React, and modern web frameworks")
                .active(true)
                .build();
        
        Course course7 = Course.builder()
                .courseCode("SE101")
                .courseName("Software Engineering")
                .credits(3)
                .description("Software development lifecycle and methodologies")
                .active(true)
                .build();
        
        Course course8 = Course.builder()
                .courseCode("OOP101")
                .courseName("Object-Oriented Programming")
                .credits(3)
                .description("OOP principles: inheritance, polymorphism, encapsulation")
                .active(true)
                .build();
        
        Course course9 = Course.builder()
                .courseCode("MATH101")
                .courseName("Discrete Mathematics")
                .credits(3)
                .description("Logic, sets, functions, and combinatorics")
                .active(true)
                .build();
        
        Course course10 = Course.builder()
                .courseCode("MATH201")
                .courseName("Linear Algebra")
                .credits(3)
                .description("Vectors, matrices, and linear transformations")
                .active(true)
                .build();
        
        Course course11 = Course.builder()
                .courseCode("NET101")
                .courseName("Computer Networks")
                .credits(3)
                .description("Network protocols, TCP/IP, and network security")
                .active(true)
                .build();
        
        Course course12 = Course.builder()
                .courseCode("OS101")
                .courseName("Operating Systems")
                .credits(3)
                .description("Process management, memory management, and file systems")
                .active(true)
                .build();
        
        // Save all courses
        courseRepository.save(course1);
        courseRepository.save(course2);
        courseRepository.save(course3);
        courseRepository.save(course4);
        courseRepository.save(course5);
        courseRepository.save(course6);
        courseRepository.save(course7);
        courseRepository.save(course8);
        courseRepository.save(course9);
        courseRepository.save(course10);
        courseRepository.save(course11);
        courseRepository.save(course12);
        
        log.info("Sample course data has been successfully initialized. Total: {} courses", 
                 courseRepository.count());
    }
}
