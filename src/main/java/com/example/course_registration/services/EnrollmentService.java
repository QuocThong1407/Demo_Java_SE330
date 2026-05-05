package com.example.course_registration.services;

import com.example.course_registration.DTOs.EnrollmentCreateDTO;
import com.example.course_registration.DTOs.EnrollmentResponseDTO;
import com.example.course_registration.DTOs.EnrollmentUpdateDTO;
import com.example.course_registration.entities.CourseSection;
import com.example.course_registration.entities.Enrollment;
import com.example.course_registration.entities.EnrollmentStatus;
import com.example.course_registration.entities.Student;
import com.example.course_registration.exceptions.DuplicateResourceException;
import com.example.course_registration.exceptions.InvalidEnrollmentException;
import com.example.course_registration.exceptions.ResourceNotFoundException;
import com.example.course_registration.mappers.EnrollmentMapper;
import com.example.course_registration.repositories.CourseSectionRepository;
import com.example.course_registration.repositories.EnrollmentRepository;
import com.example.course_registration.repositories.StudentRepository;
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
public class EnrollmentService {
    
    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final CourseSectionRepository courseSectionRepository;
    private final EnrollmentMapper enrollmentMapper;
    private final CourseSectionService courseSectionService;
    
    /**
     * Register a student for a course section
     */
    public EnrollmentResponseDTO registerForCourse(EnrollmentCreateDTO createDTO) {
        log.info("Registering student {} for section {}", createDTO.getStudentId(), createDTO.getSectionId());
        
        // Check if already enrolled
        if (enrollmentRepository.existsByStudentIdAndSectionId(createDTO.getStudentId(), createDTO.getSectionId())) {
            log.warn("Student {} is already enrolled in section {}", createDTO.getStudentId(), createDTO.getSectionId());
            throw new DuplicateResourceException("Enrollment", "student_section", 
                    createDTO.getStudentId() + "_" + createDTO.getSectionId());
        }
        
        // Load and validate student exists
        Student student = studentRepository.findById(createDTO.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", createDTO.getStudentId()));
        
        // Load and validate course section exists
        CourseSection courseSection = courseSectionRepository.findById(createDTO.getSectionId())
                .orElseThrow(() -> new ResourceNotFoundException("CourseSection", "id", createDTO.getSectionId()));
        
        // VALIDATION 1: Check if student has enough credits
        if (student.getTotalCredits() < courseSection.getCourse().getCredits()) {
            log.warn("Student {} does not have enough credits for course {}", 
                    createDTO.getStudentId(), courseSection.getCourse().getCourseCode());
            throw new InvalidEnrollmentException(
                    String.format("Student does not have enough credits. Required: %d, Available: %d",
                            courseSection.getCourse().getCredits(), student.getTotalCredits()));
        }
        
        // VALIDATION 2: Check for schedule conflicts
        String conflictMessage = validateScheduleConflicts(student, courseSection);
        if (conflictMessage != null) {
            log.warn("Schedule conflict detected for student {} and section {}: {}", 
                    createDTO.getStudentId(), createDTO.getSectionId(), conflictMessage);
            throw new InvalidEnrollmentException(conflictMessage);
        }
        
        Enrollment enrollment = enrollmentMapper.toEntity(createDTO);
        enrollment.setStudent(student);
        enrollment.setCourseSection(courseSection);
        
        Enrollment savedEnrollment = enrollmentRepository.save(enrollment);
        
        // Increment section slots and update student credits
        courseSectionService.incrementSlot(createDTO.getSectionId());
        student.setTotalCredits(student.getTotalCredits() - courseSection.getCourse().getCredits());
        studentRepository.save(student);
        
        log.info("Student registered successfully for enrollment id: {}", savedEnrollment.getId());
        return enrollmentMapper.toResponseDTO(savedEnrollment);
    }
    
    /**
     * Validate if there are schedule conflicts with existing enrollments
     * Allow same day with different periods, but not allow same day with overlapping periods
     */
    private String validateScheduleConflicts(Student student, CourseSection newSection) {
        // If the new section doesn't have schedule info, skip validation
        if (newSection.getDayOfWeek() == null || newSection.getStartPeriod() == null || newSection.getEndPeriod() == null) {
            return null;
        }
        
        // Get all non-cancelled enrollments for the student
        List<Enrollment> existingEnrollments = enrollmentRepository.findByStudentId(student.getId());
        
        for (Enrollment enrollment : existingEnrollments) {
            // Only check against REGISTERED and APPROVED enrollments
            if (enrollment.getStatus() != EnrollmentStatus.REGISTERED && 
                enrollment.getStatus() != EnrollmentStatus.APPROVED) {
                continue;
            }
            
            CourseSection existingSection = enrollment.getCourseSection();
            
            // If existing section has no schedule info, skip
            if (existingSection.getDayOfWeek() == null || 
                existingSection.getStartPeriod() == null || 
                existingSection.getEndPeriod() == null) {
                continue;
            }
            
            // Check if same day
            if (newSection.getDayOfWeek().equals(existingSection.getDayOfWeek())) {
                // Check for time overlap
                if (hasTimeOverlap(newSection.getStartPeriod(), newSection.getEndPeriod(),
                                   existingSection.getStartPeriod(), existingSection.getEndPeriod())) {
                    return String.format("Schedule conflict: Student is already enrolled in %s on %s from period %d to %d. " +
                                       "New section conflicts from period %d to %d",
                            existingSection.getSectionCode(), existingSection.getDayOfWeek(),
                            existingSection.getStartPeriod(), existingSection.getEndPeriod(),
                            newSection.getStartPeriod(), newSection.getEndPeriod());
                }
            }
        }
        
        return null;
    }
    
    /**
     * Check if two time periods overlap
     */
    private boolean hasTimeOverlap(Integer start1, Integer end1, Integer start2, Integer end2) {
        // Two periods overlap if: start1 < end2 AND start2 < end1
        return start1 <= end2 && start2 <= end1;
    }
    
    /**
     * Get enrollment by ID
     */
    @Transactional(readOnly = true)
    public EnrollmentResponseDTO getEnrollmentById(Long id) {
        log.info("Fetching enrollment with id: {}", id);
        
        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment", "id", id));
        
        return enrollmentMapper.toResponseDTO(enrollment);
    }
    
    /**
     * Get all enrollments with pagination
     */
    @Transactional(readOnly = true)
    public Page<EnrollmentResponseDTO> getAllEnrollments(Pageable pageable) {
        log.info("Fetching all enrollments with pagination");
        
        Page<Enrollment> enrollments = enrollmentRepository.findAll(pageable);
        
        List<EnrollmentResponseDTO> dtoList = enrollments.getContent().stream()
                .map(enrollmentMapper::toResponseDTO)
                .collect(Collectors.toList());
        
        return new PageImpl<>(dtoList, pageable, enrollments.getTotalElements());
    }
    
    /**
     * Get enrollments for a student
     */
    @Transactional(readOnly = true)
    public List<EnrollmentResponseDTO> getEnrollmentsByStudent(Long studentId) {
        log.info("Fetching enrollments for student: {}", studentId);
        
        List<Enrollment> enrollments = enrollmentRepository.findByStudentId(studentId);
        
        return enrollments.stream()
                .map(enrollmentMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Get enrollments for a student (paginated)
     */
    @Transactional(readOnly = true)
    public Page<EnrollmentResponseDTO> getEnrollmentsByStudent(Long studentId, Pageable pageable) {
        log.info("Fetching enrollments for student: {} with pagination", studentId);
        
        Page<Enrollment> enrollments = enrollmentRepository.findByStudentId(studentId, pageable);
        
        List<EnrollmentResponseDTO> dtoList = enrollments.getContent().stream()
                .map(enrollmentMapper::toResponseDTO)
                .collect(Collectors.toList());
        
        return new PageImpl<>(dtoList, pageable, enrollments.getTotalElements());
    }
    
    /**
     * Get enrollments for a course section
     */
    @Transactional(readOnly = true)
    public List<EnrollmentResponseDTO> getEnrollmentsBySection(Long sectionId) {
        log.info("Fetching enrollments for section: {}", sectionId);
        
        List<Enrollment> enrollments = enrollmentRepository.findBySectionId(sectionId);
        
        return enrollments.stream()
                .map(enrollmentMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Get enrollments for a course section (paginated)
     */
    @Transactional(readOnly = true)
    public Page<EnrollmentResponseDTO> getEnrollmentsBySection(Long sectionId, Pageable pageable) {
        log.info("Fetching enrollments for section: {} with pagination", sectionId);
        
        Page<Enrollment> enrollments = enrollmentRepository.findBySectionId(sectionId, pageable);
        
        List<EnrollmentResponseDTO> dtoList = enrollments.getContent().stream()
                .map(enrollmentMapper::toResponseDTO)
                .collect(Collectors.toList());
        
        return new PageImpl<>(dtoList, pageable, enrollments.getTotalElements());
    }
    
    /**
     * Get enrollments by status
     */
    @Transactional(readOnly = true)
    public List<EnrollmentResponseDTO> getEnrollmentsByStatus(EnrollmentStatus status) {
        log.info("Fetching enrollments with status: {}", status);
        
        List<Enrollment> enrollments = enrollmentRepository.findByStatus(status);
        
        return enrollments.stream()
                .map(enrollmentMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Get enrollments by status (paginated)
     */
    @Transactional(readOnly = true)
    public Page<EnrollmentResponseDTO> getEnrollmentsByStatus(EnrollmentStatus status, Pageable pageable) {
        log.info("Fetching enrollments with status: {} with pagination", status);
        
        Page<Enrollment> enrollments = enrollmentRepository.findByStatus(status, pageable);
        
        List<EnrollmentResponseDTO> dtoList = enrollments.getContent().stream()
                .map(enrollmentMapper::toResponseDTO)
                .collect(Collectors.toList());
        
        return new PageImpl<>(dtoList, pageable, enrollments.getTotalElements());
    }
    
    /**
     * Get pending enrollments (REGISTERED status)
     */
    @Transactional(readOnly = true)
    public List<EnrollmentResponseDTO> getPendingEnrollments() {
        log.info("Fetching pending enrollments");
        
        List<Enrollment> enrollments = enrollmentRepository.findPendingEnrollments();
        
        return enrollments.stream()
                .map(enrollmentMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Get pending enrollments (paginated)
     */
    @Transactional(readOnly = true)
    public Page<EnrollmentResponseDTO> getPendingEnrollments(Pageable pageable) {
        log.info("Fetching pending enrollments with pagination");
        
        Page<Enrollment> enrollments = enrollmentRepository.findPendingEnrollments(pageable);
        
        List<EnrollmentResponseDTO> dtoList = enrollments.getContent().stream()
                .map(enrollmentMapper::toResponseDTO)
                .collect(Collectors.toList());
        
        return new PageImpl<>(dtoList, pageable, enrollments.getTotalElements());
    }
    
    /**
     * Approve an enrollment
     */
    public EnrollmentResponseDTO approveEnrollment(Long enrollmentId) {
        log.info("Approving enrollment with id: {}", enrollmentId);
        
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment", "id", enrollmentId));
        
        enrollment.setStatus(EnrollmentStatus.APPROVED);
        Enrollment updatedEnrollment = enrollmentRepository.save(enrollment);
        
        log.info("Enrollment approved successfully");
        return enrollmentMapper.toResponseDTO(updatedEnrollment);
    }
    
    /**
     * Reject an enrollment
     */
    public EnrollmentResponseDTO rejectEnrollment(Long enrollmentId) {
        log.info("Rejecting enrollment with id: {}", enrollmentId);
        
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment", "id", enrollmentId));
        
        enrollment.setStatus(EnrollmentStatus.REJECTED);
        Enrollment updatedEnrollment = enrollmentRepository.save(enrollment);
        
        // Decrement section slots
        courseSectionService.decrementSlot(enrollment.getCourseSection().getId());
        
        log.info("Enrollment rejected successfully");
        return enrollmentMapper.toResponseDTO(updatedEnrollment);
    }
    
    /**
     * Cancel an enrollment
     */
    public EnrollmentResponseDTO cancelEnrollment(Long enrollmentId) {
        log.info("Cancelling enrollment with id: {}", enrollmentId);
        
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment", "id", enrollmentId));
        
        enrollment.setStatus(EnrollmentStatus.CANCELLED);
        Enrollment updatedEnrollment = enrollmentRepository.save(enrollment);
        
        // Decrement section slots
        courseSectionService.decrementSlot(enrollment.getCourseSection().getId());
        
        log.info("Enrollment cancelled successfully");
        return enrollmentMapper.toResponseDTO(updatedEnrollment);
    }
    
    /**
     * Update enrollment status
     */
    public EnrollmentResponseDTO updateEnrollment(Long enrollmentId, EnrollmentUpdateDTO updateDTO) {
        log.info("Updating enrollment with id: {}", enrollmentId);
        
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment", "id", enrollmentId));
        
        enrollmentMapper.updateEntity(updateDTO, enrollment);
        Enrollment updatedEnrollment = enrollmentRepository.save(enrollment);
        
        log.info("Enrollment updated successfully");
        return enrollmentMapper.toResponseDTO(updatedEnrollment);
    }
    
    /**
     * Delete enrollment
     */
    public void deleteEnrollment(Long enrollmentId) {
        log.info("Deleting enrollment with id: {}", enrollmentId);
        
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment", "id", enrollmentId));
        
        // Decrement section slots if not already rejected or cancelled
        if (enrollment.getStatus() == EnrollmentStatus.REGISTERED || enrollment.getStatus() == EnrollmentStatus.APPROVED) {
            courseSectionService.decrementSlot(enrollment.getCourseSection().getId());
        }
        
        enrollmentRepository.deleteById(enrollmentId);
        log.info("Enrollment deleted successfully");
    }
    
    /**
     * Count approved enrollments for a student
     */
    @Transactional(readOnly = true)
    public long countApprovedEnrollmentsByStudent(Long studentId) {
        log.info("Counting approved enrollments for student: {}", studentId);
        List<Enrollment> approvedEnrollments = enrollmentRepository.findApprovedEnrollmentsByStudent(studentId);
        return approvedEnrollments.size();
    }
    
    /**
     * Check if student is enrolled in section
     */
    @Transactional(readOnly = true)
    public boolean isStudentEnrolled(Long studentId, Long sectionId) {
        return enrollmentRepository.existsByStudentIdAndSectionId(studentId, sectionId);
    }
    
    /**
     * Count total enrollments in a section
     */
    @Transactional(readOnly = true)
    public long countEnrollmentsBySection(Long sectionId) {
        log.info("Counting enrollments for section: {}", sectionId);
        return enrollmentRepository.countBySectionId(sectionId);
    }
}
