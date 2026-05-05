package com.example.course_registration.controllers;

import com.example.course_registration.DTOs.StudentCreateDTO;
import com.example.course_registration.DTOs.StudentResponseDTO;
import com.example.course_registration.DTOs.StudentUpdateDTO;
import com.example.course_registration.DTOs.common.AppResponse;
import com.example.course_registration.services.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/students")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Student Management", description = "APIs for managing student records")
public class StudentController {
    
    private final StudentService studentService;
    
    /**
     * Create a new student
     */
    @PostMapping
    @Operation(summary = "Create a new student", description = "Create a new student record with validation")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Student created successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Student code or email already exists")
    })
    public ResponseEntity<AppResponse<StudentResponseDTO>> createStudent(
            @Valid @RequestBody StudentCreateDTO createDTO) {
        
        log.info("POST /api/v1/students - Creating new student");
        
        StudentResponseDTO responseDTO = studentService.createStudent(createDTO);
        
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(AppResponse.<StudentResponseDTO>builder()
                        .status(HttpStatus.CREATED.value())
                        .message("Student created successfully")
                        .data(responseDTO)
                        .build());
    }
    
    /**
     * Get all students with pagination
     */
    @GetMapping
    @Operation(summary = "Get all students", description = "Retrieve all students with pagination support")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Students retrieved successfully")
    })
    public ResponseEntity<AppResponse<Page<StudentResponseDTO>>> getAllStudents(
            @PageableDefault(size = 10, page = 0, sort = "id", direction = Sort.Direction.ASC)
            Pageable pageable) {
        
        log.info("GET /api/v1/students - Fetching all students");
        
        Page<StudentResponseDTO> students = studentService.getAllStudents(pageable);
        
        return ResponseEntity.ok(AppResponse.<Page<StudentResponseDTO>>builder()
                .status(HttpStatus.OK.value())
                .message("Students retrieved successfully")
                .data(students)
                .build());
    }
    
    /**
     * Search students by keyword
     */
    @GetMapping("/search")
    @Operation(summary = "Search students", description = "Search students by keyword (name, email, or student code)")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Search results retrieved successfully")
    })
    public ResponseEntity<AppResponse<Page<StudentResponseDTO>>> searchStudents(
            @Parameter(description = "Search keyword")
            @RequestParam String keyword,
            @PageableDefault(size = 10, page = 0, sort = "id", direction = Sort.Direction.ASC)
            Pageable pageable) {
        
        log.info("GET /api/v1/students/search - Searching students with keyword: {}", keyword);
        
        Page<StudentResponseDTO> students = studentService.searchStudents(keyword, pageable);
        
        return ResponseEntity.ok(AppResponse.<Page<StudentResponseDTO>>builder()
                .status(HttpStatus.OK.value())
                .message("Search completed successfully")
                .data(students)
                .build());
    }
    
    /**
     * Get student by ID
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get student by ID", description = "Retrieve a specific student by their ID")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Student retrieved successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Student not found")
    })
    public ResponseEntity<AppResponse<StudentResponseDTO>> getStudentById(
            @Parameter(description = "Student ID")
            @PathVariable Long id) {
        
        log.info("GET /api/v1/students/{} - Fetching student", id);
        
        StudentResponseDTO student = studentService.getStudentById(id);
        
        return ResponseEntity.ok(AppResponse.<StudentResponseDTO>builder()
                .status(HttpStatus.OK.value())
                .message("Student retrieved successfully")
                .data(student)
                .build());
    }
    
    /**
     * Get student by student code
     */
    @GetMapping("/code/{studentCode}")
    @Operation(summary = "Get student by code", description = "Retrieve a specific student by their student code")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Student retrieved successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Student not found")
    })
    public ResponseEntity<AppResponse<StudentResponseDTO>> getStudentByCode(
            @Parameter(description = "Student code")
            @PathVariable String studentCode) {
        
        log.info("GET /api/v1/students/code/{} - Fetching student", studentCode);
        
        StudentResponseDTO student = studentService.getStudentByCode(studentCode);
        
        return ResponseEntity.ok(AppResponse.<StudentResponseDTO>builder()
                .status(HttpStatus.OK.value())
                .message("Student retrieved successfully")
                .data(student)
                .build());
    }
    
    /**
     * Get student by email
     */
    @GetMapping("/email/{email}")
    @Operation(summary = "Get student by email", description = "Retrieve a specific student by their email")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Student retrieved successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Student not found")
    })
    public ResponseEntity<AppResponse<StudentResponseDTO>> getStudentByEmail(
            @Parameter(description = "Student email")
            @PathVariable String email) {
        
        log.info("GET /api/v1/students/email/{} - Fetching student", email);
        
        StudentResponseDTO student = studentService.getStudentByEmail(email);
        
        return ResponseEntity.ok(AppResponse.<StudentResponseDTO>builder()
                .status(HttpStatus.OK.value())
                .message("Student retrieved successfully")
                .data(student)
                .build());
    }
    
    /**
     * Get students by major
     */
    @GetMapping("/major/{major}")
    @Operation(summary = "Get students by major", description = "Retrieve all students in a specific major")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Students retrieved successfully")
    })
    public ResponseEntity<AppResponse<List<StudentResponseDTO>>> getStudentsByMajor(
            @Parameter(description = "Major name")
            @PathVariable String major) {
        
        log.info("GET /api/v1/students/major/{} - Fetching students by major", major);
        
        List<StudentResponseDTO> students = studentService.getStudentsByMajor(major);
        
        return ResponseEntity.ok(AppResponse.<List<StudentResponseDTO>>builder()
                .status(HttpStatus.OK.value())
                .message("Students retrieved successfully")
                .data(students)
                .build());
    }
    
    /**
     * Get students by academic year
     */
    @GetMapping("/academic-year/{year}")
    @Operation(summary = "Get students by academic year", description = "Retrieve all students in a specific academic year")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Students retrieved successfully")
    })
    public ResponseEntity<AppResponse<List<StudentResponseDTO>>> getStudentsByAcademicYear(
            @Parameter(description = "Academic year (1-4)")
            @PathVariable Integer year) {
        
        log.info("GET /api/v1/students/academic-year/{} - Fetching students by academic year", year);
        
        List<StudentResponseDTO> students = studentService.getStudentsByAcademicYear(year);
        
        return ResponseEntity.ok(AppResponse.<List<StudentResponseDTO>>builder()
                .status(HttpStatus.OK.value())
                .message("Students retrieved successfully")
                .data(students)
                .build());
    }
    
    /**
     * Get students by minimum credits
     */
    @GetMapping("/min-credits/{minCredits}")
    @Operation(summary = "Get students by minimum credits", description = "Retrieve all students with credits greater than or equal to specified value")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Students retrieved successfully")
    })
    public ResponseEntity<AppResponse<List<StudentResponseDTO>>> getStudentsByMinCredits(
            @Parameter(description = "Minimum credits")
            @PathVariable Integer minCredits) {
        
        log.info("GET /api/v1/students/min-credits/{} - Fetching students by minimum credits", minCredits);
        
        List<StudentResponseDTO> students = studentService.getStudentsByMinCredits(minCredits);
        
        return ResponseEntity.ok(AppResponse.<List<StudentResponseDTO>>builder()
                .status(HttpStatus.OK.value())
                .message("Students retrieved successfully")
                .data(students)
                .build());
    }
    
    /**
     * Update student
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update student", description = "Update an existing student record")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Student updated successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Student not found"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Email already exists")
    })
    public ResponseEntity<AppResponse<StudentResponseDTO>> updateStudent(
            @Parameter(description = "Student ID")
            @PathVariable Long id,
            @Valid @RequestBody StudentUpdateDTO updateDTO) {
        
        log.info("PUT /api/v1/students/{} - Updating student", id);
        
        StudentResponseDTO responseDTO = studentService.updateStudent(id, updateDTO);
        
        return ResponseEntity.ok(AppResponse.<StudentResponseDTO>builder()
                .status(HttpStatus.OK.value())
                .message("Student updated successfully")
                .data(responseDTO)
                .build());
    }
    
    /**
     * Add credits to student
     */
    @PatchMapping("/{id}/credits")
    @Operation(summary = "Add credits to student", description = "Add credits to a student's total credits")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Credits added successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Student not found")
    })
    public ResponseEntity<AppResponse<StudentResponseDTO>> addCreditsToStudent(
            @Parameter(description = "Student ID")
            @PathVariable Long id,
            @Parameter(description = "Credits to add")
            @RequestParam Integer credits) {
        
        log.info("PATCH /api/v1/students/{}/credits - Adding {} credits", id, credits);
        
        StudentResponseDTO responseDTO = studentService.addCreditsToStudent(id, credits);
        
        return ResponseEntity.ok(AppResponse.<StudentResponseDTO>builder()
                .status(HttpStatus.OK.value())
                .message("Credits added successfully")
                .data(responseDTO)
                .build());
    }
    
    /**
     * Delete student
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete student", description = "Delete a student record by ID")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "Student deleted successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Student not found")
    })
    public ResponseEntity<Void> deleteStudent(
            @Parameter(description = "Student ID")
            @PathVariable Long id) {
        
        log.info("DELETE /api/v1/students/{} - Deleting student", id);
        
        studentService.deleteStudent(id);
        
        return ResponseEntity.noContent().build();
    }
}