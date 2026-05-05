package com.example.course_registration.repositories;

import com.example.course_registration.entities.CourseSection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseSectionRepository extends JpaRepository<CourseSection, Long> {
    
    // Find by section code
    Optional<CourseSection> findBySectionCode(String sectionCode);
    
    // Check if section code exists
    boolean existsBySectionCode(String sectionCode);
    
    // Find all sections for a course
    List<CourseSection> findByCourse_Id(Long courseId);
    
    // Find all sections for a course with pagination
    Page<CourseSection> findByCourse_Id(Long courseId, Pageable pageable);
    
    // Find sections with available slots
    @Query("SELECT cs FROM CourseSection cs WHERE cs.currentSlots < cs.maxSlots")
    List<CourseSection> findAvailableSections();
    
    // Find sections with available slots (paginated)
    @Query("SELECT cs FROM CourseSection cs WHERE cs.currentSlots < cs.maxSlots")
    Page<CourseSection> findAvailableSections(Pageable pageable);
    
    // Find sections for a specific course with available slots
    @Query("SELECT cs FROM CourseSection cs WHERE cs.course.id = :courseId AND cs.currentSlots < cs.maxSlots")
    List<CourseSection> findAvailableSectionsByCourse(@Param("courseId") Long courseId);
    
    // Find sections by semester and year
    List<CourseSection> findBySemesterAndYear(Integer semester, Integer year);
    
    // Find sections by semester and year (paginated)
    Page<CourseSection> findBySemesterAndYear(Integer semester, Integer year, Pageable pageable);
    
    // Find sections with full capacity
    @Query("SELECT cs FROM CourseSection cs WHERE cs.currentSlots >= cs.maxSlots")
    List<CourseSection> findFullSections();
    
    // Count available sections
    @Query("SELECT COUNT(cs) FROM CourseSection cs WHERE cs.currentSlots < cs.maxSlots")
    long countAvailableSections();
    
    // Find all sections ordered by course
    @Query("SELECT cs FROM CourseSection cs ORDER BY cs.course.id, cs.sectionCode")
    List<CourseSection> findAllOrderedByCourseAndSection();
    
    // Search sections by course ID and semester/year
    @Query("SELECT cs FROM CourseSection cs WHERE cs.course.id = :courseId AND cs.semester = :semester AND cs.year = :year")
    List<CourseSection> findSectionsByCourseAndSemesterYear(
        @Param("courseId") Long courseId,
        @Param("semester") Integer semester,
        @Param("year") Integer year
    );
    
    // Find sections by day of week
    List<CourseSection> findByDayOfWeek(String dayOfWeek);
    
    // Find sections by room
    List<CourseSection> findByRoom(String room);
    
    // Find sections by schedule (day and period)
    @Query("SELECT cs FROM CourseSection cs WHERE cs.dayOfWeek = :dayOfWeek AND cs.startPeriod = :startPeriod AND cs.endPeriod = :endPeriod")
    List<CourseSection> findBySchedule(@Param("dayOfWeek") String dayOfWeek, @Param("startPeriod") Integer startPeriod, @Param("endPeriod") Integer endPeriod);
    
    // Find sections with prerequisites
    @Query("SELECT cs FROM CourseSection cs WHERE cs.prerequisiteCourse.id IS NOT NULL")
    List<CourseSection> findSectionsWithPrerequisites();
    
    // Find sections that require a specific prerequisite course
    @Query("SELECT cs FROM CourseSection cs WHERE cs.prerequisiteCourse.id = :prerequisiteCourseId")
    List<CourseSection> findSectionsByPrerequisite(@Param("prerequisiteCourseId") Long prerequisiteCourseId);
    
    // Find sections by course with a specific prerequisite
    @Query("SELECT cs FROM CourseSection cs WHERE cs.course.id = :courseId AND cs.prerequisiteCourse.id = :prerequisiteCourseId")
    List<CourseSection> findSectionsByCourseAndPrerequisite(@Param("courseId") Long courseId, @Param("prerequisiteCourseId") Long prerequisiteCourseId);
}
