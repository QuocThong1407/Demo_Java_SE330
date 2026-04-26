package com.example.course_registration.repositories;

import com.example.course_registration.entities.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    
    /**
     * Find student by student code
     */
    Optional<Student> findByStudentCode(String studentCode);
    
    /**
     * Find student by email
     */
    Optional<Student> findByEmail(String email);
    
    /**
     * Check if student code exists
     */
    boolean existsByStudentCode(String studentCode);
    
    /**
     * Check if email exists
     */
    boolean existsByEmail(String email);
    
    /**
     * Find students by major
     */
    List<Student> findByMajor(String major);
    
    /**
     * Find students by academic year
     */
    List<Student> findByAcademicYear(Integer academicYear);
    
    /**
     * Search students with pagination by full name or email
     */
    @Query("SELECT s FROM Student s WHERE LOWER(s.fullName) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(s.email) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(s.studentCode) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Student> searchStudents(@Param("keyword") String keyword, Pageable pageable);
    
    /**
     * Find all students with pagination
     */
    Page<Student> findAll(Pageable pageable);
    
    /**
     * Find students with credits greater than or equal to the given value
     */
    @Query("SELECT s FROM Student s WHERE s.totalCredits >= :minCredits ORDER BY s.totalCredits DESC")
    List<Student> findStudentsByMinCredits(@Param("minCredits") Integer minCredits);
    
    /**
     * Count students by major
     */
    @Query("SELECT COUNT(s) FROM Student s WHERE s.major = :major")
    long countByMajor(@Param("major") String major);
    
    /**
     * Find students in a specific academic year and major
     */
    @Query("SELECT s FROM Student s WHERE s.academicYear = :academicYear AND s.major = :major")
    List<Student> findByAcademicYearAndMajor(@Param("academicYear") Integer academicYear, 
                                             @Param("major") String major);
}
