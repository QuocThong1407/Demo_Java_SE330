package com.example.course_registration.services;

import com.example.course_registration.DTOs.CourseCreateDTO;
import com.example.course_registration.DTOs.CourseResponseDTO;
import com.example.course_registration.DTOs.CourseUpdateDTO;
import com.example.course_registration.entities.Course;
import com.example.course_registration.exceptions.DuplicateResourceException;
import com.example.course_registration.exceptions.ResourceNotFoundException;
import com.example.course_registration.mappers.CourseMapper;
import com.example.course_registration.repositories.CourseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CourseService {
    
    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;
    
    /**
     * Create a new course
     */
    public CourseResponseDTO createCourse(CourseCreateDTO createDTO) {
        log.info("Creating new course with code: {}", createDTO.getCourseCode());
        
        // Check if course code already exists
        if (courseRepository.existsByCourseCode(createDTO.getCourseCode())) {
            log.warn("Course code already exists: {}", createDTO.getCourseCode());
            throw new DuplicateResourceException("Course", "course_code", createDTO.getCourseCode());
        }
        
        Course course = courseMapper.toEntity(createDTO);
        Course savedCourse = courseRepository.save(course);
        
        log.info("Course created successfully with id: {}", savedCourse.getId());
        return courseMapper.toResponseDTO(savedCourse);
    }
    
    /**
     * Get course by ID
     */
    @Transactional(readOnly = true)
    public CourseResponseDTO getCourseById(Long id) {
        log.info("Fetching course with id: {}", id);
        
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course", "id", id));
        
        return courseMapper.toResponseDTO(course);
    }
    
    /**
     * Get course by course code
     */
    @Transactional(readOnly = true)
    public CourseResponseDTO getCourseByCode(String courseCode) {
        log.info("Fetching course with code: {}", courseCode);
        
        Course course = courseRepository.findByCourseCode(courseCode)
                .orElseThrow(() -> new ResourceNotFoundException("Course", "course_code", courseCode));
        
        return courseMapper.toResponseDTO(course);
    }
    
    /**
     * Get all courses with pagination
     */
    @Transactional(readOnly = true)
    public Page<CourseResponseDTO> getAllCourses(Pageable pageable) {
        log.info("Fetching all courses with pagination");
        
        Page<Course> courses = courseRepository.findAll(pageable);
        
        List<CourseResponseDTO> dtoList = courses.getContent().stream()
                .map(courseMapper::toResponseDTO)
                .collect(Collectors.toList());
        
        return new PageImpl<>(dtoList, pageable, courses.getTotalElements());
    }
    
    /**
     * Get all active courses with pagination
     */
    @Transactional(readOnly = true)
    public Page<CourseResponseDTO> getActiveCourses(Pageable pageable) {
        log.info("Fetching active courses with pagination");
        
        Page<Course> courses = courseRepository.findByActiveTrue(pageable);
        
        List<CourseResponseDTO> dtoList = courses.getContent().stream()
                .map(courseMapper::toResponseDTO)
                .collect(Collectors.toList());
        
        return new PageImpl<>(dtoList, pageable, courses.getTotalElements());
    }
    
    /**
     * Get all active courses (non-paginated)
     */
    @Transactional(readOnly = true)
    public List<CourseResponseDTO> getAllActiveCourses() {
        log.info("Fetching all active courses");
        
        List<Course> courses = courseRepository.findByActiveTrue();
        
        return courses.stream()
                .map(courseMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Get all inactive courses
     */
    @Transactional(readOnly = true)
    public List<CourseResponseDTO> getAllInactiveCourses() {
        log.info("Fetching all inactive courses");
        
        List<Course> courses = courseRepository.findByActiveFalse();
        
        return courses.stream()
                .map(courseMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Search courses by keyword
     */
    @Transactional(readOnly = true)
    public Page<CourseResponseDTO> searchCourses(String keyword, Pageable pageable) {
        log.info("Searching courses with keyword: {}", keyword);
        
        Page<Course> courses = courseRepository.searchCourses(keyword, pageable);
        
        List<CourseResponseDTO> dtoList = courses.getContent().stream()
                .map(courseMapper::toResponseDTO)
                .collect(Collectors.toList());
        
        return new PageImpl<>(dtoList, pageable, courses.getTotalElements());
    }
    
    /**
     * Search active courses by keyword
     */
    @Transactional(readOnly = true)
    public Page<CourseResponseDTO> searchActiveCourses(String keyword, Pageable pageable) {
        log.info("Searching active courses with keyword: {}", keyword);
        
        Page<Course> courses = courseRepository.searchActiveCourses(keyword, pageable);
        
        List<CourseResponseDTO> dtoList = courses.getContent().stream()
                .map(courseMapper::toResponseDTO)
                .collect(Collectors.toList());
        
        return new PageImpl<>(dtoList, pageable, courses.getTotalElements());
    }
    
    /**
     * Get courses by credit value
     */
    @Transactional(readOnly = true)
    public List<CourseResponseDTO> getCoursesByCredits(Integer credits) {
        log.info("Fetching courses with {} credits", credits);
        
        List<Course> courses = courseRepository.findByCredits(credits);
        
        return courses.stream()
                .map(courseMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Get courses with minimum credits
     */
    @Transactional(readOnly = true)
    public List<CourseResponseDTO> getCoursesByMinCredits(Integer minCredits) {
        log.info("Fetching courses with minimum credits: {}", minCredits);
        
        List<Course> courses = courseRepository.findByMinCredits(minCredits);
        
        return courses.stream()
                .map(courseMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Get courses by credits in range with pagination
     */
    @Transactional(readOnly = true)
    public Page<CourseResponseDTO> getCoursesByCreditsInRange(Integer minCredits, Integer maxCredits, Pageable pageable) {
        log.info("Fetching courses with credits between {} and {}", minCredits, maxCredits);
        
        Page<Course> courses = courseRepository.findByCreditsInRangePageable(minCredits, maxCredits, pageable);
        
        List<CourseResponseDTO> dtoList = courses.getContent().stream()
                .map(courseMapper::toResponseDTO)
                .collect(Collectors.toList());
        
        return new PageImpl<>(dtoList, pageable, courses.getTotalElements());
    }
    
    /**
     * Update course
     */
    public CourseResponseDTO updateCourse(Long id, CourseUpdateDTO updateDTO) {
        log.info("Updating course with id: {}", id);
        
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course", "id", id));
        
        courseMapper.updateEntity(updateDTO, course);
        Course updatedCourse = courseRepository.save(course);
        
        log.info("Course updated successfully");
        return courseMapper.toResponseDTO(updatedCourse);
    }
    
    /**
     * Activate course
     */
    public CourseResponseDTO activateCourse(Long id) {
        log.info("Activating course with id: {}", id);
        
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course", "id", id));
        
        course.setActive(true);
        Course updatedCourse = courseRepository.save(course);
        
        log.info("Course activated successfully");
        return courseMapper.toResponseDTO(updatedCourse);
    }
    
    /**
     * Deactivate course
     */
    public CourseResponseDTO deactivateCourse(Long id) {
        log.info("Deactivating course with id: {}", id);
        
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course", "id", id));
        
        course.setActive(false);
        Course updatedCourse = courseRepository.save(course);
        
        log.info("Course deactivated successfully");
        return courseMapper.toResponseDTO(updatedCourse);
    }
    
    /**
     * Delete course
     */
    public void deleteCourse(Long id) {
        log.info("Deleting course with id: {}", id);
        
        if (!courseRepository.existsById(id)) {
            log.warn("Course not found with id: {}", id);
            throw new ResourceNotFoundException("Course", "id", id);
        }
        
        courseRepository.deleteById(id);
        log.info("Course deleted successfully");
    }
    
    /**
     * Count active courses
     */
    @Transactional(readOnly = true)
    public long countActiveCourses() {
        log.info("Counting active courses");
        return courseRepository.countActiveCourses();
    }
    
    /**
     * Get all courses ordered by code
     */
    @Transactional(readOnly = true)
    public List<CourseResponseDTO> getAllCoursesOrderedByCode() {
        log.info("Fetching all courses ordered by code");
        
        List<Course> courses = courseRepository.findAllOrderByCode();
        
        return courses.stream()
                .map(courseMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
}
