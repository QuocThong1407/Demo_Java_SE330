package com.example.course_registration.controllers;

import com.example.course_registration.DTOs.CourseSectionCreateDTO;
import com.example.course_registration.DTOs.CourseSectionResponseDTO;
import com.example.course_registration.DTOs.CourseSectionUpdateDTO;
import com.example.course_registration.DTOs.common.AppResponse;
import com.example.course_registration.services.CourseSectionService;
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
@RequestMapping("/api/v1/course-sections")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Course Section Management", description = "APIs for managing course sections")
public class CourseSectionController {
    
    private final CourseSectionService courseSectionService;
    
    /**
     * Create a new course section
     */
    @PostMapping
    @Operation(summary = "Create a new course section", description = "Create a new course section record")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Section created successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public ResponseEntity<AppResponse<CourseSectionResponseDTO>> createCourseSection(
            @Valid @RequestBody CourseSectionCreateDTO createDTO) {
        
        log.info("POST /api/v1/course-sections - Creating new section");
        
        CourseSectionResponseDTO responseDTO = courseSectionService.createCourseSection(createDTO);
        
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(AppResponse.<CourseSectionResponseDTO>builder()
                        .status(HttpStatus.CREATED.value())
                        .message("Course section created successfully")
                        .data(responseDTO)
                        .build());
    }
    
    /**
     * Get all course sections with pagination
     */
    @GetMapping
    @Operation(summary = "Get all course sections", description = "Retrieve all course sections with pagination")
    public ResponseEntity<AppResponse<Page<CourseSectionResponseDTO>>> getAllCourseSections(
            @PageableDefault(size = 10, page = 0, sort = "id", direction = Sort.Direction.ASC)
            Pageable pageable) {
        
        log.info("GET /api/v1/course-sections - Fetching all sections");
        
        Page<CourseSectionResponseDTO> sections = courseSectionService.getAllCourseSections(pageable);
        
        return ResponseEntity.ok(AppResponse.<Page<CourseSectionResponseDTO>>builder()
                .status(HttpStatus.OK.value())
                .message("Sections retrieved successfully")
                .data(sections)
                .build());
    }
    
    /**
     * Get course section by ID
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get section by ID", description = "Retrieve a specific course section by ID")
    public ResponseEntity<AppResponse<CourseSectionResponseDTO>> getCourseSectionById(
            @Parameter(description = "Course Section ID")
            @PathVariable Long id) {
        
        log.info("GET /api/v1/course-sections/{} - Fetching section", id);
        
        CourseSectionResponseDTO section = courseSectionService.getCourseSectionById(id);
        
        return ResponseEntity.ok(AppResponse.<CourseSectionResponseDTO>builder()
                .status(HttpStatus.OK.value())
                .message("Section retrieved successfully")
                .data(section)
                .build());
    }
    
    /**
     * Get course section by section code
     */
    @GetMapping("/code/{sectionCode}")
    @Operation(summary = "Get section by code", description = "Retrieve a specific course section by section code")
    public ResponseEntity<AppResponse<CourseSectionResponseDTO>> getCourseSectionByCode(
            @Parameter(description = "Section code")
            @PathVariable String sectionCode) {
        
        log.info("GET /api/v1/course-sections/code/{} - Fetching section", sectionCode);
        
        CourseSectionResponseDTO section = courseSectionService.getCourseSectionByCode(sectionCode);
        
        return ResponseEntity.ok(AppResponse.<CourseSectionResponseDTO>builder()
                .status(HttpStatus.OK.value())
                .message("Section retrieved successfully")
                .data(section)
                .build());
    }
    
    /**
     * Get sections for a course
     */
    @GetMapping("/course/{courseId}")
    @Operation(summary = "Get sections by course", description = "Retrieve all sections for a specific course")
    public ResponseEntity<AppResponse<List<CourseSectionResponseDTO>>> getSectionsByCourse(
            @Parameter(description = "Course ID")
            @PathVariable Long courseId) {
        
        log.info("GET /api/v1/course-sections/course/{} - Fetching sections for course", courseId);
        
        List<CourseSectionResponseDTO> sections = courseSectionService.getSectionsByCourse(courseId);
        
        return ResponseEntity.ok(AppResponse.<List<CourseSectionResponseDTO>>builder()
                .status(HttpStatus.OK.value())
                .message("Sections retrieved successfully")
                .data(sections)
                .build());
    }
    
    /**
     * Get available sections
     */
    @GetMapping("/available/list")
    @Operation(summary = "Get available sections", description = "Retrieve all sections with available slots")
    public ResponseEntity<AppResponse<List<CourseSectionResponseDTO>>> getAvailableSections() {
        
        log.info("GET /api/v1/course-sections/available/list - Fetching available sections");
        
        List<CourseSectionResponseDTO> sections = courseSectionService.getAvailableSections();
        
        return ResponseEntity.ok(AppResponse.<List<CourseSectionResponseDTO>>builder()
                .status(HttpStatus.OK.value())
                .message("Available sections retrieved successfully")
                .data(sections)
                .build());
    }
    
    /**
     * Get available sections with pagination
     */
    @GetMapping("/available")
    @Operation(summary = "Get available sections (paginated)", description = "Retrieve available sections with pagination")
    public ResponseEntity<AppResponse<Page<CourseSectionResponseDTO>>> getAvailableSectionsPaged(
            @PageableDefault(size = 10, page = 0, sort = "id", direction = Sort.Direction.ASC)
            Pageable pageable) {
        
        log.info("GET /api/v1/course-sections/available - Fetching available sections");
        
        Page<CourseSectionResponseDTO> sections = courseSectionService.getAvailableSections(pageable);
        
        return ResponseEntity.ok(AppResponse.<Page<CourseSectionResponseDTO>>builder()
                .status(HttpStatus.OK.value())
                .message("Available sections retrieved successfully")
                .data(sections)
                .build());
    }
    
    /**
     * Get available sections for a course
     */
    @GetMapping("/course/{courseId}/available")
    @Operation(summary = "Get available sections by course", description = "Retrieve available sections for a specific course")
    public ResponseEntity<AppResponse<List<CourseSectionResponseDTO>>> getAvailableSectionsByCourse(
            @Parameter(description = "Course ID")
            @PathVariable Long courseId) {
        
        log.info("GET /api/v1/course-sections/course/{}/available - Fetching available sections for course", courseId);
        
        List<CourseSectionResponseDTO> sections = courseSectionService.getAvailableSectionsByCourse(courseId);
        
        return ResponseEntity.ok(AppResponse.<List<CourseSectionResponseDTO>>builder()
                .status(HttpStatus.OK.value())
                .message("Available sections retrieved successfully")
                .data(sections)
                .build());
    }
    
    /**
     * Get sections by semester and year
     */
    @GetMapping("/semester/{semester}/year/{year}")
    @Operation(summary = "Get sections by semester and year", description = "Retrieve sections for a specific semester and year")
    public ResponseEntity<AppResponse<List<CourseSectionResponseDTO>>> getSectionsBySemesterAndYear(
            @Parameter(description = "Semester (1 or 2)")
            @PathVariable Integer semester,
            @Parameter(description = "Year")
            @PathVariable Integer year) {
        
        log.info("GET /api/v1/course-sections/semester/{}/year/{} - Fetching sections", semester, year);
        
        List<CourseSectionResponseDTO> sections = courseSectionService.getSectionsBySemesterAndYear(semester, year);
        
        return ResponseEntity.ok(AppResponse.<List<CourseSectionResponseDTO>>builder()
                .status(HttpStatus.OK.value())
                .message("Sections retrieved successfully")
                .data(sections)
                .build());
    }
    
    /**
     * Update course section
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update course section", description = "Update an existing course section")
    public ResponseEntity<AppResponse<CourseSectionResponseDTO>> updateCourseSection(
            @Parameter(description = "Course Section ID")
            @PathVariable Long id,
            @Valid @RequestBody CourseSectionUpdateDTO updateDTO) {
        
        log.info("PUT /api/v1/course-sections/{} - Updating section", id);
        
        CourseSectionResponseDTO responseDTO = courseSectionService.updateCourseSection(id, updateDTO);
        
        return ResponseEntity.ok(AppResponse.<CourseSectionResponseDTO>builder()
                .status(HttpStatus.OK.value())
                .message("Section updated successfully")
                .data(responseDTO)
                .build());
    }
    
    /**
     * Delete course section
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete course section", description = "Delete a course section by ID")
    public ResponseEntity<Void> deleteCourseSectionById(
            @Parameter(description = "Course Section ID")
            @PathVariable Long id) {
        
        log.info("DELETE /api/v1/course-sections/{} - Deleting section", id);
        
        courseSectionService.deleteCourseSectionById(id);
        
        return ResponseEntity.noContent().build();
    }
    
    /**
     * Get sections by day of week
     */
    @GetMapping("/schedule/day/{dayOfWeek}")
    @Operation(summary = "Get sections by day of week", description = "Retrieve sections scheduled on a specific day")
    public ResponseEntity<AppResponse<List<CourseSectionResponseDTO>>> getSectionsByDayOfWeek(
            @Parameter(description = "Day of week (MONDAY-SUNDAY)")
            @PathVariable String dayOfWeek) {
        
        log.info("GET /api/v1/course-sections/schedule/day/{} - Fetching sections", dayOfWeek);
        
        List<CourseSectionResponseDTO> sections = courseSectionService.getSectionsByDayOfWeek(dayOfWeek);
        
        return ResponseEntity.ok(AppResponse.<List<CourseSectionResponseDTO>>builder()
                .status(HttpStatus.OK.value())
                .message("Sections retrieved successfully")
                .data(sections)
                .build());
    }
    
    /**
     * Get sections by room
     */
    @GetMapping("/schedule/room/{room}")
    @Operation(summary = "Get sections by room", description = "Retrieve sections in a specific room")
    public ResponseEntity<AppResponse<List<CourseSectionResponseDTO>>> getSectionsByRoom(
            @Parameter(description = "Room identifier")
            @PathVariable String room) {
        
        log.info("GET /api/v1/course-sections/schedule/room/{} - Fetching sections", room);
        
        List<CourseSectionResponseDTO> sections = courseSectionService.getSectionsByRoom(room);
        
        return ResponseEntity.ok(AppResponse.<List<CourseSectionResponseDTO>>builder()
                .status(HttpStatus.OK.value())
                .message("Sections retrieved successfully")
                .data(sections)
                .build());
    }
    
    /**
     * Get sections by schedule (day and period)
     */
    @GetMapping("/schedule/{dayOfWeek}/{startPeriod}/{endPeriod}")
    @Operation(summary = "Get sections by schedule", description = "Retrieve sections on specific day and time period")
    public ResponseEntity<AppResponse<List<CourseSectionResponseDTO>>> getSectionsBySchedule(
            @Parameter(description = "Day of week")
            @PathVariable String dayOfWeek,
            @Parameter(description = "Start period")
            @PathVariable Integer startPeriod,
            @Parameter(description = "End period")
            @PathVariable Integer endPeriod) {
        
        log.info("GET /api/v1/course-sections/schedule/{}/{}/{} - Fetching sections", dayOfWeek, startPeriod, endPeriod);
        
        List<CourseSectionResponseDTO> sections = courseSectionService.getSectionsBySchedule(dayOfWeek, startPeriod, endPeriod);
        
        return ResponseEntity.ok(AppResponse.<List<CourseSectionResponseDTO>>builder()
                .status(HttpStatus.OK.value())
                .message("Sections retrieved successfully")
                .data(sections)
                .build());
    }
    
    /**
     * Get sections with prerequisites
     */
    @GetMapping("/prerequisites/list")
    @Operation(summary = "Get sections with prerequisites", description = "Retrieve all sections that have prerequisites")
    public ResponseEntity<AppResponse<List<CourseSectionResponseDTO>>> getSectionsWithPrerequisites() {
        
        log.info("GET /api/v1/course-sections/prerequisites/list - Fetching sections with prerequisites");
        
        List<CourseSectionResponseDTO> sections = courseSectionService.getSectionsWithPrerequisites();
        
        return ResponseEntity.ok(AppResponse.<List<CourseSectionResponseDTO>>builder()
                .status(HttpStatus.OK.value())
                .message("Sections retrieved successfully")
                .data(sections)
                .build());
    }
    
    /**
     * Get sections that require a specific prerequisite course
     */
    @GetMapping("/prerequisites/{prerequisiteCourseId}")
    @Operation(summary = "Get sections by prerequisite", description = "Retrieve sections that require a specific prerequisite course")
    public ResponseEntity<AppResponse<List<CourseSectionResponseDTO>>> getSectionsByPrerequisite(
            @Parameter(description = "Prerequisite Course ID")
            @PathVariable Long prerequisiteCourseId) {
        
        log.info("GET /api/v1/course-sections/prerequisites/{} - Fetching sections", prerequisiteCourseId);
        
        List<CourseSectionResponseDTO> sections = courseSectionService.getSectionsByPrerequisite(prerequisiteCourseId);
        
        return ResponseEntity.ok(AppResponse.<List<CourseSectionResponseDTO>>builder()
                .status(HttpStatus.OK.value())
                .message("Sections retrieved successfully")
                .data(sections)
                .build());
    }
    
    /**
     * Get sections by course with a specific prerequisite
     */
    @GetMapping("/course/{courseId}/prerequisites/{prerequisiteCourseId}")
    @Operation(summary = "Get sections with prerequisite by course", description = "Retrieve sections of a course that have a specific prerequisite")
    public ResponseEntity<AppResponse<List<CourseSectionResponseDTO>>> getSectionsByCourseAndPrerequisite(
            @Parameter(description = "Course ID")
            @PathVariable Long courseId,
            @Parameter(description = "Prerequisite Course ID")
            @PathVariable Long prerequisiteCourseId) {
        
        log.info("GET /api/v1/course-sections/course/{}/prerequisites/{} - Fetching sections", courseId, prerequisiteCourseId);
        
        List<CourseSectionResponseDTO> sections = courseSectionService.getSectionsByCourseAndPrerequisite(courseId, prerequisiteCourseId);
        
        return ResponseEntity.ok(AppResponse.<List<CourseSectionResponseDTO>>builder()
                .status(HttpStatus.OK.value())
                .message("Sections retrieved successfully")
                .data(sections)
                .build());
    }
}