package com.example.course_registration.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonBackReference;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.ArrayList;

@Entity
@Table(name = "course_sections", indexes = {
    @Index(name = "idx_course_id", columnList = "course_id"),
    @Index(name = "idx_section_code", columnList = "section_code"),
    @Index(name = "idx_semester_year", columnList = "semester, year"),
    @Index(name = "idx_prerequisite_course", columnList = "prerequisite_course_id")
})
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseSection {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    @JsonBackReference(value = "course-sections")
    private Course course;
    
    @Column(name = "section_code", nullable = false, length = 10)
    private String sectionCode;
    
    @Column(name = "max_slots", nullable = false)
    private Integer maxSlots;
    
    @Column(name = "current_slots", nullable = false)
    private Integer currentSlots;
    
    @Column(name = "semester", nullable = false)
    private Integer semester;
    
    @Column(name = "year", nullable = false)
    private Integer year;
    
    @Column(name = "day_of_week", length = 10)
    @Pattern(regexp = "^(MONDAY|TUESDAY|WEDNESDAY|THURSDAY|FRIDAY|SATURDAY|SUNDAY)?$",
             message = "Invalid day of week")
    private String dayOfWeek;
    
    @Column(name = "start_period")
    private Integer startPeriod;
    
    @Column(name = "end_period")
    private Integer endPeriod;
    
    @Column(name = "room", length = 50)
    private String room;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prerequisite_course_id")
    @JsonBackReference(value = "prerequisite-sections")
    private Course prerequisiteCourse;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    @OneToMany(mappedBy = "courseSection", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference(value = "section-enrollments")
    @Builder.Default
    private List<Enrollment> enrollments = new ArrayList<>();
}
