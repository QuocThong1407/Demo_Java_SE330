package com.example.course_registration.data;

import com.example.course_registration.entities.Student;
import com.example.course_registration.repositories.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class StudentDataSeed implements CommandLineRunner {
    
    private final StudentRepository studentRepository;
    
    @Override
    public void run(String... args) throws Exception {
        if (studentRepository.count() > 0) {
            log.info("Sample data already exists. Skipping seed data initialization.");
            return;
        }
        
        log.info("Initializing sample student data...");
        
        // Create sample students
        Student student1 = Student.builder()
                .studentCode("SV001")
                .fullName("Nguyễn Văn A")
                .email("student1@example.com")
                .phone("+84912345678")
                .major("Computer Science")
                .academicYear(1)
                .totalCredits(0)
                .build();
        
        Student student2 = Student.builder()
                .studentCode("SV002")
                .fullName("Trần Thị B")
                .email("student2@example.com")
                .phone("+84912345679")
                .major("Information Technology")
                .academicYear(2)
                .totalCredits(12)
                .build();
        
        Student student3 = Student.builder()
                .studentCode("SV003")
                .fullName("Lê Minh C")
                .email("student3@example.com")
                .phone("+84912345680")
                .major("Computer Science")
                .academicYear(3)
                .totalCredits(36)
                .build();
        
        Student student4 = Student.builder()
                .studentCode("SV004")
                .fullName("Phạm Quốc D")
                .email("student4@example.com")
                .phone("+84912345681")
                .major("Information Technology")
                .academicYear(4)
                .totalCredits(48)
                .build();
        
        Student student5 = Student.builder()
                .studentCode("SV005")
                .fullName("Hoàng Thúy E")
                .email("student5@example.com")
                .phone("+84912345682")
                .major("Software Engineering")
                .academicYear(2)
                .totalCredits(24)
                .build();
        
        Student student6 = Student.builder()
                .studentCode("SV006")
                .fullName("Vũ Hải F")
                .email("student6@example.com")
                .phone("+84912345683")
                .major("Computer Science")
                .academicYear(1)
                .totalCredits(6)
                .build();
        
        Student student7 = Student.builder()
                .studentCode("SV007")
                .fullName("Đặng Kim G")
                .email("student7@example.com")
                .phone("+84912345684")
                .major("Software Engineering")
                .academicYear(3)
                .totalCredits(42)
                .build();
        
        Student student8 = Student.builder()
                .studentCode("SV008")
                .fullName("Bùi Sơn H")
                .email("student8@example.com")
                .phone("+84912345685")
                .major("Information Technology")
                .academicYear(1)
                .totalCredits(0)
                .build();
        
        // Save all students
        studentRepository.save(student1);
        studentRepository.save(student2);
        studentRepository.save(student3);
        studentRepository.save(student4);
        studentRepository.save(student5);
        studentRepository.save(student6);
        studentRepository.save(student7);
        studentRepository.save(student8);
        
        log.info("Sample student data has been successfully initialized. Total: {} students", 
                 studentRepository.count());
    }
}
