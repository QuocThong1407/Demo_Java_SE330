package com.example.course_registration.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "courses", indexes = {
    @Index(name = "idx_course_code", columnList = "course_code", unique = true),
    @Index(name = "idx_active", columnList = "active")
})
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Course {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "course_code", nullable = false, unique = true, length = 20)
    private String courseCode;
    
    @Column(name = "course_name", nullable = false, length = 150)
    private String courseName;
    
    @Column(name = "credits", nullable = false)
    private Integer credits;
    
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "active", nullable = false)
    @Builder.Default
    private Boolean active = true;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
