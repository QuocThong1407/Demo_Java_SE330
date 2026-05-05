package com.example.course_registration.repositories;

import com.example.course_registration.entities.Enrollment;
import com.example.course_registration.entities.EnrollmentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    
    // Find enrollments by student
    @Query("SELECT e FROM Enrollment e WHERE e.student.id = :studentId")
    List<Enrollment> findByStudentId(@Param("studentId") Long studentId);
    
    // Find enrollments by student with pagination
    @Query("SELECT e FROM Enrollment e WHERE e.student.id = :studentId")
    Page<Enrollment> findByStudentId(@Param("studentId") Long studentId, Pageable pageable);
    
    // Find enrollments by section
    @Query("SELECT e FROM Enrollment e WHERE e.courseSection.id = :sectionId")
    List<Enrollment> findBySectionId(@Param("sectionId") Long sectionId);
    
    // Find enrollments by section with pagination
    @Query("SELECT e FROM Enrollment e WHERE e.courseSection.id = :sectionId")
    Page<Enrollment> findBySectionId(@Param("sectionId") Long sectionId, Pageable pageable);
    
    // Find enrollments by status
    List<Enrollment> findByStatus(EnrollmentStatus status);
    
    // Find enrollments by status with pagination
    Page<Enrollment> findByStatus(EnrollmentStatus status, Pageable pageable);
    
    // Check if student is enrolled in section
    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END FROM Enrollment e WHERE e.student.id = :studentId AND e.courseSection.id = :sectionId")
    boolean existsByStudentIdAndSectionId(@Param("studentId") Long studentId, @Param("sectionId") Long sectionId);
    
    // Find enrollment by student and section
    @Query("SELECT e FROM Enrollment e WHERE e.student.id = :studentId AND e.courseSection.id = :sectionId")
    Optional<Enrollment> findByStudentIdAndSectionId(@Param("studentId") Long studentId, @Param("sectionId") Long sectionId);
    
    // Count enrollments for a section
    @Query("SELECT COUNT(e) FROM Enrollment e WHERE e.courseSection.id = :sectionId")
    long countBySectionId(@Param("sectionId") Long sectionId);
    
    // Count enrollments by student
    @Query("SELECT COUNT(e) FROM Enrollment e WHERE e.student.id = :studentId")
    long countByStudentId(@Param("studentId") Long studentId);
    
    // Find approved enrollments for a student
    @Query("SELECT e FROM Enrollment e WHERE e.student.id = :studentId AND e.status = 'APPROVED'")
    List<Enrollment> findApprovedEnrollmentsByStudent(@Param("studentId") Long studentId);
    
    // Find pending enrollments (REGISTERED status)
    @Query("SELECT e FROM Enrollment e WHERE e.status = 'REGISTERED'")
    List<Enrollment> findPendingEnrollments();
    
    // Find pending enrollments with pagination
    @Query("SELECT e FROM Enrollment e WHERE e.status = 'REGISTERED'")
    Page<Enrollment> findPendingEnrollments(Pageable pageable);
    
    // Find enrollments by student and status
    @Query("SELECT e FROM Enrollment e WHERE e.student.id = :studentId AND e.status = :status")
    List<Enrollment> findByStudentIdAndStatus(@Param("studentId") Long studentId, @Param("status") EnrollmentStatus status);
    
    // Count enrollments by status
    @Query("SELECT COUNT(e) FROM Enrollment e WHERE e.status = :status")
    long countByStatus(@Param("status") EnrollmentStatus status);
    
    // Find all enrollments ordered by date
    @Query("SELECT e FROM Enrollment e ORDER BY e.registeredAt DESC")
    List<Enrollment> findAllOrderedByDate();
    
    // Find enrollments for a course section with specific status
    @Query("SELECT e FROM Enrollment e WHERE e.courseSection.id = :sectionId AND e.status = :status")
    List<Enrollment> findBySectionAndStatus(@Param("sectionId") Long sectionId, @Param("status") EnrollmentStatus status);
}
