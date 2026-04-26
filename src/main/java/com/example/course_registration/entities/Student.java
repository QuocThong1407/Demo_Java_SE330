package com.example.course_registration.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "students")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String studentCode;

    private String fullName;
    private String email;
}