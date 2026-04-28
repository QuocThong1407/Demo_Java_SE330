package com.example.course_registration.repositories;

import com.example.course_registration.entities.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    
    /**
     * Find course by course code
     */
    Optional<Course> findByCourseCode(String courseCode);
    
    /**
     * Check if course code exists
     */
    boolean existsByCourseCode(String courseCode);
    
    /**
     * Find all active courses
     */
    List<Course> findByActiveTrue();
    
    /**
     * Find all active courses with pagination
     */
    Page<Course> findByActiveTrue(Pageable pageable);
    
    /**
     * Find all inactive courses
     */
    List<Course> findByActiveFalse();
    
    /**
     * Search courses by course name or code
     */
    @Query("SELECT c FROM Course c WHERE LOWER(c.courseName) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(c.courseCode) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Course> searchCourses(@Param("keyword") String keyword, Pageable pageable);
    
    /**
     * Search active courses
     */
    @Query("SELECT c FROM Course c WHERE c.active = true AND " +
           "(LOWER(c.courseName) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(c.courseCode) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Course> searchActiveCourses(@Param("keyword") String keyword, Pageable pageable);
    
    /**
     * Find courses by number of credits
     */
    List<Course> findByCredits(Integer credits);
    
    /**
     * Find courses with credits greater than or equal to specified value
     */
    @Query("SELECT c FROM Course c WHERE c.credits >= :minCredits ORDER BY c.credits ASC")
    List<Course> findByMinCredits(@Param("minCredits") Integer minCredits);
    
    /**
     * Find courses with credits in range
     */
    @Query("SELECT c FROM Course c WHERE c.credits >= :minCredits AND c.credits <= :maxCredits " +
           "ORDER BY c.credits ASC")
    List<Course> findByCreditsInRange(@Param("minCredits") Integer minCredits, 
                                      @Param("maxCredits") Integer maxCredits);
    
    /**
     * Count active courses
     */
    @Query("SELECT COUNT(c) FROM Course c WHERE c.active = true")
    long countActiveCourses();
    
    /**
     * Find all courses ordered by code
     */
    @Query("SELECT c FROM Course c ORDER BY c.courseCode ASC")
    List<Course> findAllOrderByCode();
    
    /**
     * Find courses by credit range with pagination
     */
    @Query("SELECT c FROM Course c WHERE c.credits >= :minCredits AND c.credits <= :maxCredits")
    Page<Course> findByCreditsInRangePageable(@Param("minCredits") Integer minCredits,
                                              @Param("maxCredits") Integer maxCredits,
                                              Pageable pageable);
}
