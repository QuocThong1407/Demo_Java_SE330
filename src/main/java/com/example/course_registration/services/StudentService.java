package com.example.course_registration.services;

import com.example.course_registration.DTOs.StudentCreateDTO;
import com.example.course_registration.DTOs.StudentResponseDTO;
import com.example.course_registration.DTOs.StudentUpdateDTO;
import com.example.course_registration.entities.Student;
import com.example.course_registration.exceptions.DuplicateResourceException;
import com.example.course_registration.exceptions.ResourceNotFoundException;
import com.example.course_registration.mappers.StudentMapper;
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
public class StudentService {
    
    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;
    
    /**
     * Create a new student
     */
    public StudentResponseDTO createStudent(StudentCreateDTO createDTO) {
        log.info("Creating new student with code: {}", createDTO.getStudentCode());
        
        // Check if student code already exists
        if (studentRepository.existsByStudentCode(createDTO.getStudentCode())) {
            log.warn("Student code already exists: {}", createDTO.getStudentCode());
            throw new DuplicateResourceException("Student", "student_code", createDTO.getStudentCode());
        }
        
        // Check if email already exists
        if (studentRepository.existsByEmail(createDTO.getEmail())) {
            log.warn("Email already exists: {}", createDTO.getEmail());
            throw new DuplicateResourceException("Student", "email", createDTO.getEmail());
        }
        
        Student student = studentMapper.toEntity(createDTO);
        Student savedStudent = studentRepository.save(student);
        
        log.info("Student created successfully with id: {}", savedStudent.getId());
        return studentMapper.toResponseDTO(savedStudent);
    }
    
    /**
     * Get student by ID
     */
    @Transactional(readOnly = true)
    public StudentResponseDTO getStudentById(Long id) {
        log.info("Fetching student with id: {}", id);
        
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", id));
        
        return studentMapper.toResponseDTO(student);
    }
    
    /**
     * Get student by student code
     */
    @Transactional(readOnly = true)
    public StudentResponseDTO getStudentByCode(String studentCode) {
        log.info("Fetching student with code: {}", studentCode);
        
        Student student = studentRepository.findByStudentCode(studentCode)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "student_code", studentCode));
        
        return studentMapper.toResponseDTO(student);
    }
    
    /**
     * Get student by email
     */
    @Transactional(readOnly = true)
    public StudentResponseDTO getStudentByEmail(String email) {
        log.info("Fetching student with email: {}", email);
        
        Student student = studentRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "email", email));
        
        return studentMapper.toResponseDTO(student);
    }
    
    /**
     * Get all students with pagination
     */
    @Transactional(readOnly = true)
    public Page<StudentResponseDTO> getAllStudents(Pageable pageable) {
        log.info("Fetching all students with pagination");
        
        Page<Student> students = studentRepository.findAll(pageable);
        
        List<StudentResponseDTO> dtoList = students.getContent().stream()
                .map(studentMapper::toResponseDTO)
                .collect(Collectors.toList());
        
        return new PageImpl<>(dtoList, pageable, students.getTotalElements());
    }
    
    /**
     * Search students by keyword
     */
    @Transactional(readOnly = true)
    public Page<StudentResponseDTO> searchStudents(String keyword, Pageable pageable) {
        log.info("Searching students with keyword: {}", keyword);
        
        Page<Student> students = studentRepository.searchStudents(keyword, pageable);
        
        List<StudentResponseDTO> dtoList = students.getContent().stream()
                .map(studentMapper::toResponseDTO)
                .collect(Collectors.toList());
        
        return new PageImpl<>(dtoList, pageable, students.getTotalElements());
    }
    
    /**
     * Update student
     */
    public StudentResponseDTO updateStudent(Long id, StudentUpdateDTO updateDTO) {
        log.info("Updating student with id: {}", id);
        
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", id));
        
        // Check if new email exists (if email is being updated)
        if (updateDTO.getEmail() != null && !updateDTO.getEmail().equals(student.getEmail())) {
            if (studentRepository.existsByEmail(updateDTO.getEmail())) {
                log.warn("Email already exists: {}", updateDTO.getEmail());
                throw new DuplicateResourceException("Student", "email", updateDTO.getEmail());
            }
        }
        
        studentMapper.updateEntity(updateDTO, student);
        Student updatedStudent = studentRepository.save(student);
        
        log.info("Student updated successfully");
        return studentMapper.toResponseDTO(updatedStudent);
    }
    
    /**
     * Delete student
     */
    public void deleteStudent(Long id) {
        log.info("Deleting student with id: {}", id);
        
        if (!studentRepository.existsById(id)) {
            log.warn("Student not found with id: {}", id);
            throw new ResourceNotFoundException("Student", "id", id);
        }
        
        studentRepository.deleteById(id);
        log.info("Student deleted successfully");
    }
    
    /**
     * Get students by major
     */
    @Transactional(readOnly = true)
    public List<StudentResponseDTO> getStudentsByMajor(String major) {
        log.info("Fetching students by major: {}", major);
        
        List<Student> students = studentRepository.findByMajor(major);
        
        return students.stream()
                .map(studentMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Get students by academic year
     */
    @Transactional(readOnly = true)
    public List<StudentResponseDTO> getStudentsByAcademicYear(Integer academicYear) {
        log.info("Fetching students by academic year: {}", academicYear);
        
        List<Student> students = studentRepository.findByAcademicYear(academicYear);
        
        return students.stream()
                .map(studentMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Get students by minimum credits
     */
    @Transactional(readOnly = true)
    public List<StudentResponseDTO> getStudentsByMinCredits(Integer minCredits) {
        log.info("Fetching students with minimum credits: {}", minCredits);
        
        List<Student> students = studentRepository.findStudentsByMinCredits(minCredits);
        
        return students.stream()
                .map(studentMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Get students by academic year and major
     */
    @Transactional(readOnly = true)
    public List<StudentResponseDTO> getStudentsByAcademicYearAndMajor(Integer academicYear, String major) {
        log.info("Fetching students by academic year: {} and major: {}", academicYear, major);
        
        List<Student> students = studentRepository.findByAcademicYearAndMajor(academicYear, major);
        
        return students.stream()
                .map(studentMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Count students by major
     */
    @Transactional(readOnly = true)
    public long countStudentsByMajor(String major) {
        log.info("Counting students by major: {}", major);
        return studentRepository.countByMajor(major);
    }
    
    /**
     * Add credits to student
     */
    public StudentResponseDTO addCreditsToStudent(Long id, Integer credits) {
        log.info("Adding {} credits to student with id: {}", credits, id);
        
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", id));
        
        int newTotalCredits = student.getTotalCredits() + credits;
        student.setTotalCredits(newTotalCredits);
        
        Student updatedStudent = studentRepository.save(student);
        log.info("Credits added successfully. New total: {}", newTotalCredits);
        
        return studentMapper.toResponseDTO(updatedStudent);
    }
}
