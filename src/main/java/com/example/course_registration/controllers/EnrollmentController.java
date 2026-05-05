package com.example.course_registration.controllers;

import com.example.course_registration.DTOs.EnrollmentCreateDTO;
import com.example.course_registration.DTOs.EnrollmentResponseDTO;
import com.example.course_registration.DTOs.EnrollmentUpdateDTO;
import com.example.course_registration.DTOs.common.AppResponse;
import com.example.course_registration.entities.EnrollmentStatus;
import com.example.course_registration.services.EnrollmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
@RequestMapping("/api/v1/enrollments")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Enrollment Management", description = "APIs for managing course enrollments and registrations")
public class EnrollmentController {
    
    private final EnrollmentService enrollmentService;
    
    /**
     * Register a student for a course section
     */
    @PostMapping
    @Operation(summary = "Register for a course", description = "Register a student for a course section")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Enrollment created successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Already enrolled")
    })
    public ResponseEntity<AppResponse<EnrollmentResponseDTO>> registerForCourse(
            @Valid @RequestBody EnrollmentCreateDTO createDTO) {
        
        log.info("POST /api/v1/enrollments - Registering student");
        
        EnrollmentResponseDTO responseDTO = enrollmentService.registerForCourse(createDTO);
        
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(AppResponse.<EnrollmentResponseDTO>builder()
                        .status(HttpStatus.CREATED.value())
                        .message("Enrollment created successfully")
                        .data(responseDTO)
                        .build());
    }
    
    /**
     * Get all enrollments with pagination
     */
    @GetMapping
    @Operation(summary = "Get all enrollments", description = "Retrieve all enrollments with pagination")
    public ResponseEntity<AppResponse<Page<EnrollmentResponseDTO>>> getAllEnrollments(
            @PageableDefault(size = 10, page = 0, sort = "id", direction = Sort.Direction.ASC)
            Pageable pageable) {
        
        log.info("GET /api/v1/enrollments - Fetching all enrollments");
        
        Page<EnrollmentResponseDTO> enrollments = enrollmentService.getAllEnrollments(pageable);
        
        return ResponseEntity.ok(AppResponse.<Page<EnrollmentResponseDTO>>builder()
                .status(HttpStatus.OK.value())
                .message("Enrollments retrieved successfully")
                .data(enrollments)
                .build());
    }
    
    /**
     * Get enrollment by ID
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get enrollment by ID", description = "Retrieve a specific enrollment by ID")
    public ResponseEntity<AppResponse<EnrollmentResponseDTO>> getEnrollmentById(
            @Parameter(description = "Enrollment ID")
            @PathVariable Long id) {
        
        log.info("GET /api/v1/enrollments/{} - Fetching enrollment", id);
        
        EnrollmentResponseDTO enrollment = enrollmentService.getEnrollmentById(id);
        
        return ResponseEntity.ok(AppResponse.<EnrollmentResponseDTO>builder()
                .status(HttpStatus.OK.value())
                .message("Enrollment retrieved successfully")
                .data(enrollment)
                .build());
    }
    
    /**
     * Get enrollments by student
     */
    @GetMapping("/student/{studentId}")
    @Operation(summary = "Get enrollments by student", description = "Retrieve all enrollments for a specific student")
    public ResponseEntity<AppResponse<List<EnrollmentResponseDTO>>> getEnrollmentsByStudent(
            @Parameter(description = "Student ID")
            @PathVariable Long studentId) {
        
        log.info("GET /api/v1/enrollments/student/{} - Fetching enrollments", studentId);
        
        List<EnrollmentResponseDTO> enrollments = enrollmentService.getEnrollmentsByStudent(studentId);
        
        return ResponseEntity.ok(AppResponse.<List<EnrollmentResponseDTO>>builder()
                .status(HttpStatus.OK.value())
                .message("Enrollments retrieved successfully")
                .data(enrollments)
                .build());
    }
    
    /**
     * Get enrollments by student (paginated)
     */
    @GetMapping("/student/{studentId}/paged")
    @Operation(summary = "Get student enrollments (paginated)", description = "Retrieve student enrollments with pagination")
    public ResponseEntity<AppResponse<Page<EnrollmentResponseDTO>>> getEnrollmentsByStudentPaged(
            @Parameter(description = "Student ID")
            @PathVariable Long studentId,
            @PageableDefault(size = 10, page = 0, sort = "id", direction = Sort.Direction.ASC)
            Pageable pageable) {
        
        log.info("GET /api/v1/enrollments/student/{}/paged - Fetching enrollments with pagination", studentId);
        
        Page<EnrollmentResponseDTO> enrollments = enrollmentService.getEnrollmentsByStudent(studentId, pageable);
        
        return ResponseEntity.ok(AppResponse.<Page<EnrollmentResponseDTO>>builder()
                .status(HttpStatus.OK.value())
                .message("Enrollments retrieved successfully")
                .data(enrollments)
                .build());
    }
    
    /**
     * Get enrollments by course section
     */
    @GetMapping("/section/{sectionId}")
    @Operation(summary = "Get enrollments by section", description = "Retrieve all enrollments for a specific course section")
    public ResponseEntity<AppResponse<List<EnrollmentResponseDTO>>> getEnrollmentsBySection(
            @Parameter(description = "Course Section ID")
            @PathVariable Long sectionId) {
        
        log.info("GET /api/v1/enrollments/section/{} - Fetching enrollments", sectionId);
        
        List<EnrollmentResponseDTO> enrollments = enrollmentService.getEnrollmentsBySection(sectionId);
        
        return ResponseEntity.ok(AppResponse.<List<EnrollmentResponseDTO>>builder()
                .status(HttpStatus.OK.value())
                .message("Enrollments retrieved successfully")
                .data(enrollments)
                .build());
    }
    
    /**
     * Get enrollments by course section (paginated)
     */
    @GetMapping("/section/{sectionId}/paged")
    @Operation(summary = "Get section enrollments (paginated)", description = "Retrieve section enrollments with pagination")
    public ResponseEntity<AppResponse<Page<EnrollmentResponseDTO>>> getEnrollmentsBySectionPaged(
            @Parameter(description = "Course Section ID")
            @PathVariable Long sectionId,
            @PageableDefault(size = 10, page = 0, sort = "id", direction = Sort.Direction.ASC)
            Pageable pageable) {
        
        log.info("GET /api/v1/enrollments/section/{}/paged - Fetching enrollments with pagination", sectionId);
        
        Page<EnrollmentResponseDTO> enrollments = enrollmentService.getEnrollmentsBySection(sectionId, pageable);
        
        return ResponseEntity.ok(AppResponse.<Page<EnrollmentResponseDTO>>builder()
                .status(HttpStatus.OK.value())
                .message("Enrollments retrieved successfully")
                .data(enrollments)
                .build());
    }
    
    /**
     * Get enrollments by status
     */
    @GetMapping("/status/{status}")
    @Operation(summary = "Get enrollments by status", description = "Retrieve enrollments with specific status")
    public ResponseEntity<AppResponse<List<EnrollmentResponseDTO>>> getEnrollmentsByStatus(
            @Parameter(description = "Enrollment status (REGISTERED, APPROVED, REJECTED, CANCELLED)")
            @PathVariable EnrollmentStatus status) {
        
        log.info("GET /api/v1/enrollments/status/{} - Fetching enrollments", status);
        
        List<EnrollmentResponseDTO> enrollments = enrollmentService.getEnrollmentsByStatus(status);
        
        return ResponseEntity.ok(AppResponse.<List<EnrollmentResponseDTO>>builder()
                .status(HttpStatus.OK.value())
                .message("Enrollments retrieved successfully")
                .data(enrollments)
                .build());
    }
    
    /**
     * Get pending enrollments
     */
    @GetMapping("/pending/list")
    @Operation(summary = "Get pending enrollments", description = "Retrieve all pending enrollments (REGISTERED status)")
    public ResponseEntity<AppResponse<List<EnrollmentResponseDTO>>> getPendingEnrollments() {
        
        log.info("GET /api/v1/enrollments/pending/list - Fetching pending enrollments");
        
        List<EnrollmentResponseDTO> enrollments = enrollmentService.getPendingEnrollments();
        
        return ResponseEntity.ok(AppResponse.<List<EnrollmentResponseDTO>>builder()
                .status(HttpStatus.OK.value())
                .message("Pending enrollments retrieved successfully")
                .data(enrollments)
                .build());
    }
    
    /**
     * Get pending enrollments (paginated)
     */
    @GetMapping("/pending")
    @Operation(summary = "Get pending enrollments (paginated)", description = "Retrieve pending enrollments with pagination")
    public ResponseEntity<AppResponse<Page<EnrollmentResponseDTO>>> getPendingEnrollmentsPaged(
            @PageableDefault(size = 10, page = 0, sort = "id", direction = Sort.Direction.ASC)
            Pageable pageable) {
        
        log.info("GET /api/v1/enrollments/pending - Fetching pending enrollments with pagination");
        
        Page<EnrollmentResponseDTO> enrollments = enrollmentService.getPendingEnrollments(pageable);
        
        return ResponseEntity.ok(AppResponse.<Page<EnrollmentResponseDTO>>builder()
                .status(HttpStatus.OK.value())
                .message("Pending enrollments retrieved successfully")
                .data(enrollments)
                .build());
    }
    
    /**
     * Approve an enrollment
     */
    @PatchMapping("/{id}/approve")
    @Operation(summary = "Approve enrollment", description = "Approve a pending enrollment")
    public ResponseEntity<AppResponse<EnrollmentResponseDTO>> approveEnrollment(
            @Parameter(description = "Enrollment ID")
            @PathVariable Long id) {
        
        log.info("PATCH /api/v1/enrollments/{}/approve - Approving enrollment", id);
        
        EnrollmentResponseDTO responseDTO = enrollmentService.approveEnrollment(id);
        
        return ResponseEntity.ok(AppResponse.<EnrollmentResponseDTO>builder()
                .status(HttpStatus.OK.value())
                .message("Enrollment approved successfully")
                .data(responseDTO)
                .build());
    }
    
    /**
     * Reject an enrollment
     */
    @PatchMapping("/{id}/reject")
    @Operation(summary = "Reject enrollment", description = "Reject a pending enrollment")
    public ResponseEntity<AppResponse<EnrollmentResponseDTO>> rejectEnrollment(
            @Parameter(description = "Enrollment ID")
            @PathVariable Long id) {
        
        log.info("PATCH /api/v1/enrollments/{}/reject - Rejecting enrollment", id);
        
        EnrollmentResponseDTO responseDTO = enrollmentService.rejectEnrollment(id);
        
        return ResponseEntity.ok(AppResponse.<EnrollmentResponseDTO>builder()
                .status(HttpStatus.OK.value())
                .message("Enrollment rejected successfully")
                .data(responseDTO)
                .build());
    }
    
    /**
     * Cancel an enrollment
     */
    @PatchMapping("/{id}/cancel")
    @Operation(summary = "Cancel enrollment", description = "Cancel an active enrollment")
    public ResponseEntity<AppResponse<EnrollmentResponseDTO>> cancelEnrollment(
            @Parameter(description = "Enrollment ID")
            @PathVariable Long id) {
        
        log.info("PATCH /api/v1/enrollments/{}/cancel - Cancelling enrollment", id);
        
        EnrollmentResponseDTO responseDTO = enrollmentService.cancelEnrollment(id);
        
        return ResponseEntity.ok(AppResponse.<EnrollmentResponseDTO>builder()
                .status(HttpStatus.OK.value())
                .message("Enrollment cancelled successfully")
                .data(responseDTO)
                .build());
    }
    
    /**
     * Update enrollment status
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update enrollment", description = "Update enrollment status")
    public ResponseEntity<AppResponse<EnrollmentResponseDTO>> updateEnrollment(
            @Parameter(description = "Enrollment ID")
            @PathVariable Long id,
            @Valid @RequestBody EnrollmentUpdateDTO updateDTO) {
        
        log.info("PUT /api/v1/enrollments/{} - Updating enrollment", id);
        
        EnrollmentResponseDTO responseDTO = enrollmentService.updateEnrollment(id, updateDTO);
        
        return ResponseEntity.ok(AppResponse.<EnrollmentResponseDTO>builder()
                .status(HttpStatus.OK.value())
                .message("Enrollment updated successfully")
                .data(responseDTO)
                .build());
    }
    
    /**
     * Delete enrollment
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete enrollment", description = "Delete an enrollment record")
    public ResponseEntity<Void> deleteEnrollment(
            @Parameter(description = "Enrollment ID")
            @PathVariable Long id) {
        
        log.info("DELETE /api/v1/enrollments/{} - Deleting enrollment", id);
        
        enrollmentService.deleteEnrollment(id);
        
        return ResponseEntity.noContent().build();
    }
}