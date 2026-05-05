package com.example.course_registration.services;

import com.example.course_registration.DTOs.CourseSectionCreateDTO;
import com.example.course_registration.DTOs.CourseSectionResponseDTO;
import com.example.course_registration.DTOs.CourseSectionUpdateDTO;
import com.example.course_registration.entities.Course;
import com.example.course_registration.entities.CourseSection;
import com.example.course_registration.exceptions.ResourceNotFoundException;
import com.example.course_registration.mappers.CourseSectionMapper;
import com.example.course_registration.repositories.CourseRepository;
import com.example.course_registration.repositories.CourseSectionRepository;
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
public class CourseSectionService {
    
    private final CourseSectionRepository courseSectionRepository;
    private final CourseRepository courseRepository;
    private final CourseSectionMapper courseSectionMapper;
    
    /**
     * Create a new course section
     */
    public CourseSectionResponseDTO createCourseSection(CourseSectionCreateDTO createDTO) {
        log.info("Creating new course section: {} for course {}", createDTO.getSectionCode(), createDTO.getCourseId());
        
        // Load and validate course exists
        Course course = courseRepository.findById(createDTO.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course", "id", createDTO.getCourseId()));
        
        CourseSection courseSection = courseSectionMapper.toEntity(createDTO);
        courseSection.setCourse(course);
        
        // Load prerequisite course if specified
        if (createDTO.getPrerequisiteCourseId() != null) {
            Course prerequisiteCourse = courseRepository.findById(createDTO.getPrerequisiteCourseId())
                    .orElseThrow(() -> new ResourceNotFoundException("Course", "id", createDTO.getPrerequisiteCourseId()));
            courseSection.setPrerequisiteCourse(prerequisiteCourse);
        }
        
        CourseSection savedSection = courseSectionRepository.save(courseSection);
        
        log.info("Course section created successfully with id: {}", savedSection.getId());
        return courseSectionMapper.toResponseDTO(savedSection);
    }
    
    /**
     * Get course section by ID
     */
    @Transactional(readOnly = true)
    public CourseSectionResponseDTO getCourseSectionById(Long id) {
        log.info("Fetching course section with id: {}", id);
        
        CourseSection courseSection = courseSectionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("CourseSection", "id", id));
        
        return courseSectionMapper.toResponseDTO(courseSection);
    }
    
    /**
     * Get course section by section code
     */
    @Transactional(readOnly = true)
    public CourseSectionResponseDTO getCourseSectionByCode(String sectionCode) {
        log.info("Fetching course section with code: {}", sectionCode);
        
        CourseSection courseSection = courseSectionRepository.findBySectionCode(sectionCode)
                .orElseThrow(() -> new ResourceNotFoundException("CourseSection", "section_code", sectionCode));
        
        return courseSectionMapper.toResponseDTO(courseSection);
    }
    
    /**
     * Get all course sections with pagination
     */
    @Transactional(readOnly = true)
    public Page<CourseSectionResponseDTO> getAllCourseSections(Pageable pageable) {
        log.info("Fetching all course sections with pagination");
        
        Page<CourseSection> sections = courseSectionRepository.findAll(pageable);
        
        List<CourseSectionResponseDTO> dtoList = sections.getContent().stream()
                .map(courseSectionMapper::toResponseDTO)
                .collect(Collectors.toList());
        
        return new PageImpl<>(dtoList, pageable, sections.getTotalElements());
    }
    
    /**
     * Get sections for a specific course
     */
    @Transactional(readOnly = true)
    public List<CourseSectionResponseDTO> getSectionsByCourse(Long courseId) {
        log.info("Fetching sections for course: {}", courseId);
        
        List<CourseSection> sections = courseSectionRepository.findByCourse_Id(courseId);
        
        return sections.stream()
                .map(courseSectionMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Get sections for a specific course with pagination
     */
    @Transactional(readOnly = true)
    public Page<CourseSectionResponseDTO> getSectionsByCourse(Long courseId, Pageable pageable) {
        log.info("Fetching sections for course: {} with pagination", courseId);
        
        Page<CourseSection> sections = courseSectionRepository.findByCourse_Id(courseId, pageable);
        
        List<CourseSectionResponseDTO> dtoList = sections.getContent().stream()
                .map(courseSectionMapper::toResponseDTO)
                .collect(Collectors.toList());
        
        return new PageImpl<>(dtoList, pageable, sections.getTotalElements());
    }
    
    /**
     * Get available sections (with open slots)
     */
    @Transactional(readOnly = true)
    public List<CourseSectionResponseDTO> getAvailableSections() {
        log.info("Fetching available course sections");
        
        List<CourseSection> sections = courseSectionRepository.findAvailableSections();
        
        return sections.stream()
                .map(courseSectionMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Get available sections with pagination
     */
    @Transactional(readOnly = true)
    public Page<CourseSectionResponseDTO> getAvailableSections(Pageable pageable) {
        log.info("Fetching available course sections with pagination");
        
        Page<CourseSection> sections = courseSectionRepository.findAvailableSections(pageable);
        
        List<CourseSectionResponseDTO> dtoList = sections.getContent().stream()
                .map(courseSectionMapper::toResponseDTO)
                .collect(Collectors.toList());
        
        return new PageImpl<>(dtoList, pageable, sections.getTotalElements());
    }
    
    /**
     * Get available sections for a specific course
     */
    @Transactional(readOnly = true)
    public List<CourseSectionResponseDTO> getAvailableSectionsByCourse(Long courseId) {
        log.info("Fetching available sections for course: {}", courseId);
        
        List<CourseSection> sections = courseSectionRepository.findAvailableSectionsByCourse(courseId);
        
        return sections.stream()
                .map(courseSectionMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Get sections by semester and year
     */
    @Transactional(readOnly = true)
    public List<CourseSectionResponseDTO> getSectionsBySemesterAndYear(Integer semester, Integer year) {
        log.info("Fetching sections for semester: {} and year: {}", semester, year);
        
        List<CourseSection> sections = courseSectionRepository.findBySemesterAndYear(semester, year);
        
        return sections.stream()
                .map(courseSectionMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Get sections by semester and year (paginated)
     */
    @Transactional(readOnly = true)
    public Page<CourseSectionResponseDTO> getSectionsBySemesterAndYear(Integer semester, Integer year, Pageable pageable) {
        log.info("Fetching sections for semester: {} and year: {} with pagination", semester, year);
        
        Page<CourseSection> sections = courseSectionRepository.findBySemesterAndYear(semester, year, pageable);
        
        List<CourseSectionResponseDTO> dtoList = sections.getContent().stream()
                .map(courseSectionMapper::toResponseDTO)
                .collect(Collectors.toList());
        
        return new PageImpl<>(dtoList, pageable, sections.getTotalElements());
    }
    
    /**
     * Update course section
     */
    public CourseSectionResponseDTO updateCourseSection(Long id, CourseSectionUpdateDTO updateDTO) {
        log.info("Updating course section with id: {}", id);
        
        CourseSection courseSection = courseSectionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("CourseSection", "id", id));
        
        courseSectionMapper.updateEntity(updateDTO, courseSection);
        
        // Update prerequisite course if specified
        if (updateDTO.getPrerequisiteCourseId() != null) {
            Course prerequisiteCourse = courseRepository.findById(updateDTO.getPrerequisiteCourseId())
                    .orElseThrow(() -> new ResourceNotFoundException("Course", "id", updateDTO.getPrerequisiteCourseId()));
            courseSection.setPrerequisiteCourse(prerequisiteCourse);
        }
        
        CourseSection updatedSection = courseSectionRepository.save(courseSection);
        
        log.info("Course section updated successfully");
        return courseSectionMapper.toResponseDTO(updatedSection);
    }
    
    /**
     * Delete course section
     */
    public void deleteCourseSectionById(Long id) {
        log.info("Deleting course section with id: {}", id);
        
        if (!courseSectionRepository.existsById(id)) {
            log.warn("Course section not found with id: {}", id);
            throw new ResourceNotFoundException("CourseSection", "id", id);
        }
        
        courseSectionRepository.deleteById(id);
        log.info("Course section deleted successfully");
    }
    
    /**
     * Count available sections
     */
    @Transactional(readOnly = true)
    public long countAvailableSections() {
        log.info("Counting available sections");
        return courseSectionRepository.countAvailableSections();
    }
    
    /**
     * Increment current slots
     */
    public void incrementSlot(Long sectionId) {
        log.info("Incrementing slot for section: {}", sectionId);
        
        CourseSection section = courseSectionRepository.findById(sectionId)
                .orElseThrow(() -> new ResourceNotFoundException("CourseSection", "id", sectionId));
        
        section.setCurrentSlots(section.getCurrentSlots() + 1);
        courseSectionRepository.save(section);
    }
    
    /**
     * Decrement current slots
     */
    public void decrementSlot(Long sectionId) {
        log.info("Decrementing slot for section: {}", sectionId);
        
        CourseSection section = courseSectionRepository.findById(sectionId)
                .orElseThrow(() -> new ResourceNotFoundException("CourseSection", "id", sectionId));
        
        if (section.getCurrentSlots() > 0) {
            section.setCurrentSlots(section.getCurrentSlots() - 1);
            courseSectionRepository.save(section);
        }
    }
    
    /**
     * Get sections by day of week
     */
    @Transactional(readOnly = true)
    public List<CourseSectionResponseDTO> getSectionsByDayOfWeek(String dayOfWeek) {
        log.info("Fetching sections for day: {}", dayOfWeek);
        
        List<CourseSection> sections = courseSectionRepository.findByDayOfWeek(dayOfWeek);
        
        return sections.stream()
                .map(courseSectionMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Get sections by room
     */
    @Transactional(readOnly = true)
    public List<CourseSectionResponseDTO> getSectionsByRoom(String room) {
        log.info("Fetching sections in room: {}", room);
        
        List<CourseSection> sections = courseSectionRepository.findByRoom(room);
        
        return sections.stream()
                .map(courseSectionMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Get sections by schedule (day and period)
     */
    @Transactional(readOnly = true)
    public List<CourseSectionResponseDTO> getSectionsBySchedule(String dayOfWeek, Integer startPeriod, Integer endPeriod) {
        log.info("Fetching sections for day: {} from period {} to {}", dayOfWeek, startPeriod, endPeriod);
        
        List<CourseSection> sections = courseSectionRepository.findBySchedule(dayOfWeek, startPeriod, endPeriod);
        
        return sections.stream()
                .map(courseSectionMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Get sections with prerequisites
     */
    @Transactional(readOnly = true)
    public List<CourseSectionResponseDTO> getSectionsWithPrerequisites() {
        log.info("Fetching sections with prerequisites");
        
        List<CourseSection> sections = courseSectionRepository.findSectionsWithPrerequisites();
        
        return sections.stream()
                .map(courseSectionMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Get sections that require a specific prerequisite course
     */
    @Transactional(readOnly = true)
    public List<CourseSectionResponseDTO> getSectionsByPrerequisite(Long prerequisiteCourseId) {
        log.info("Fetching sections with prerequisite course: {}", prerequisiteCourseId);
        
        List<CourseSection> sections = courseSectionRepository.findSectionsByPrerequisite(prerequisiteCourseId);
        
        return sections.stream()
                .map(courseSectionMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Get sections by course with a specific prerequisite
     */
    @Transactional(readOnly = true)
    public List<CourseSectionResponseDTO> getSectionsByCourseAndPrerequisite(Long courseId, Long prerequisiteCourseId) {
        log.info("Fetching sections for course: {} with prerequisite: {}", courseId, prerequisiteCourseId);
        
        List<CourseSection> sections = courseSectionRepository.findSectionsByCourseAndPrerequisite(courseId, prerequisiteCourseId);
        
        return sections.stream()
                .map(courseSectionMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
}
