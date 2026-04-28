package com.example.course_registration.controllers;

import com.example.course_registration.DTOs.CourseCreateDTO;
import com.example.course_registration.DTOs.CourseResponseDTO;
import com.example.course_registration.DTOs.CourseUpdateDTO;
import com.example.course_registration.DTOs.common.AppResponse;
import com.example.course_registration.services.CourseService;
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
@RequestMapping("/api/v1/courses")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Course Management", description = "APIs for managing course records")
public class CourseController {
    
    private final CourseService courseService;
    
    /**
     * Create a new course
     */
    @PostMapping
    @Operation(summary = "Create a new course", description = "Create a new course record with validation")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Course created successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Course code already exists")
    })
    public ResponseEntity<AppResponse<CourseResponseDTO>> createCourse(
            @Valid @RequestBody CourseCreateDTO createDTO) {
        
        log.info("POST /api/v1/courses - Creating new course");
        
        CourseResponseDTO responseDTO = courseService.createCourse(createDTO);
        
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(AppResponse.<CourseResponseDTO>builder()
                        .status(HttpStatus.CREATED.value())
                        .message("Course created successfully")
                        .data(responseDTO)
                        .build());
    }
    
    /**
     * Get all courses with pagination
     */
    @GetMapping
    @Operation(summary = "Get all courses", description = "Retrieve all courses with pagination support")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Courses retrieved successfully")
    })
    public ResponseEntity<AppResponse<Page<CourseResponseDTO>>> getAllCourses(
            @PageableDefault(size = 10, page = 0, sort = "id", direction = Sort.Direction.ASC)
            Pageable pageable) {
        
        log.info("GET /api/v1/courses - Fetching all courses");
        
        Page<CourseResponseDTO> courses = courseService.getAllCourses(pageable);
        
        return ResponseEntity.ok(AppResponse.<Page<CourseResponseDTO>>builder()
                .status(HttpStatus.OK.value())
                .message("Courses retrieved successfully")
                .data(courses)
                .build());
    }
    
    /**
     * Get all active courses with pagination
     */
    @GetMapping("/active")
    @Operation(summary = "Get all active courses", description = "Retrieve all active courses with pagination support")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Active courses retrieved successfully")
    })
    public ResponseEntity<AppResponse<Page<CourseResponseDTO>>> getActiveCourses(
            @PageableDefault(size = 10, page = 0, sort = "id", direction = Sort.Direction.ASC)
            Pageable pageable) {
        
        log.info("GET /api/v1/courses/active - Fetching active courses");
        
        Page<CourseResponseDTO> courses = courseService.getActiveCourses(pageable);
        
        return ResponseEntity.ok(AppResponse.<Page<CourseResponseDTO>>builder()
                .status(HttpStatus.OK.value())
                .message("Active courses retrieved successfully")
                .data(courses)
                .build());
    }
    
    /**
     * Get all active courses (non-paginated)
     */
    @GetMapping("/active/list")
    @Operation(summary = "Get all active courses as list", description = "Retrieve all active courses without pagination")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Active courses retrieved successfully")
    })
    public ResponseEntity<AppResponse<List<CourseResponseDTO>>> getAllActiveCoursesList() {
        
        log.info("GET /api/v1/courses/active/list - Fetching all active courses");
        
        List<CourseResponseDTO> courses = courseService.getAllActiveCourses();
        
        return ResponseEntity.ok(AppResponse.<List<CourseResponseDTO>>builder()
                .status(HttpStatus.OK.value())
                .message("Active courses retrieved successfully")
                .data(courses)
                .build());
    }
    
    /**
     * Get all inactive courses
     */
    @GetMapping("/inactive")
    @Operation(summary = "Get all inactive courses", description = "Retrieve all inactive courses")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Inactive courses retrieved successfully")
    })
    public ResponseEntity<AppResponse<List<CourseResponseDTO>>> getInactiveCourses() {
        
        log.info("GET /api/v1/courses/inactive - Fetching inactive courses");
        
        List<CourseResponseDTO> courses = courseService.getAllInactiveCourses();
        
        return ResponseEntity.ok(AppResponse.<List<CourseResponseDTO>>builder()
                .status(HttpStatus.OK.value())
                .message("Inactive courses retrieved successfully")
                .data(courses)
                .build());
    }
    
    /**
     * Search courses by keyword
     */
    @GetMapping("/search")
    @Operation(summary = "Search courses", description = "Search courses by keyword (name or code)")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Search results retrieved successfully")
    })
    public ResponseEntity<AppResponse<Page<CourseResponseDTO>>> searchCourses(
            @Parameter(description = "Search keyword")
            @RequestParam String keyword,
            @PageableDefault(size = 10, page = 0, sort = "id", direction = Sort.Direction.ASC)
            Pageable pageable) {
        
        log.info("GET /api/v1/courses/search - Searching courses with keyword: {}", keyword);
        
        Page<CourseResponseDTO> courses = courseService.searchCourses(keyword, pageable);
        
        return ResponseEntity.ok(AppResponse.<Page<CourseResponseDTO>>builder()
                .status(HttpStatus.OK.value())
                .message("Search completed successfully")
                .data(courses)
                .build());
    }
    
    /**
     * Search active courses by keyword
     */
    @GetMapping("/search/active")
    @Operation(summary = "Search active courses", description = "Search active courses by keyword (name or code)")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Search results retrieved successfully")
    })
    public ResponseEntity<AppResponse<Page<CourseResponseDTO>>> searchActiveCourses(
            @Parameter(description = "Search keyword")
            @RequestParam String keyword,
            @PageableDefault(size = 10, page = 0, sort = "id", direction = Sort.Direction.ASC)
            Pageable pageable) {
        
        log.info("GET /api/v1/courses/search/active - Searching active courses with keyword: {}", keyword);
        
        Page<CourseResponseDTO> courses = courseService.searchActiveCourses(keyword, pageable);
        
        return ResponseEntity.ok(AppResponse.<Page<CourseResponseDTO>>builder()
                .status(HttpStatus.OK.value())
                .message("Search completed successfully")
                .data(courses)
                .build());
    }
    
    /**
     * Get course by ID
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get course by ID", description = "Retrieve a specific course by their ID")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Course retrieved successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Course not found")
    })
    public ResponseEntity<AppResponse<CourseResponseDTO>> getCourseById(
            @Parameter(description = "Course ID")
            @PathVariable Long id) {
        
        log.info("GET /api/v1/courses/{} - Fetching course", id);
        
        CourseResponseDTO course = courseService.getCourseById(id);
        
        return ResponseEntity.ok(AppResponse.<CourseResponseDTO>builder()
                .status(HttpStatus.OK.value())
                .message("Course retrieved successfully")
                .data(course)
                .build());
    }
    
    /**
     * Get course by course code
     */
    @GetMapping("/code/{courseCode}")
    @Operation(summary = "Get course by code", description = "Retrieve a specific course by their course code")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Course retrieved successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Course not found")
    })
    public ResponseEntity<AppResponse<CourseResponseDTO>> getCourseByCode(
            @Parameter(description = "Course code")
            @PathVariable String courseCode) {
        
        log.info("GET /api/v1/courses/code/{} - Fetching course", courseCode);
        
        CourseResponseDTO course = courseService.getCourseByCode(courseCode);
        
        return ResponseEntity.ok(AppResponse.<CourseResponseDTO>builder()
                .status(HttpStatus.OK.value())
                .message("Course retrieved successfully")
                .data(course)
                .build());
    }
    
    /**
     * Get courses by credits
     */
    @GetMapping("/credits/{credits}")
    @Operation(summary = "Get courses by credits", description = "Retrieve all courses with specific credit value")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Courses retrieved successfully")
    })
    public ResponseEntity<AppResponse<List<CourseResponseDTO>>> getCoursesByCredits(
            @Parameter(description = "Credit value")
            @PathVariable Integer credits) {
        
        log.info("GET /api/v1/courses/credits/{} - Fetching courses", credits);
        
        List<CourseResponseDTO> courses = courseService.getCoursesByCredits(credits);
        
        return ResponseEntity.ok(AppResponse.<List<CourseResponseDTO>>builder()
                .status(HttpStatus.OK.value())
                .message("Courses retrieved successfully")
                .data(courses)
                .build());
    }
    
    /**
     * Get courses by minimum credits
     */
    @GetMapping("/min-credits/{minCredits}")
    @Operation(summary = "Get courses by minimum credits", description = "Retrieve all courses with credits greater than or equal to specified value")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Courses retrieved successfully")
    })
    public ResponseEntity<AppResponse<List<CourseResponseDTO>>> getCoursesByMinCredits(
            @Parameter(description = "Minimum credits")
            @PathVariable Integer minCredits) {
        
        log.info("GET /api/v1/courses/min-credits/{} - Fetching courses", minCredits);
        
        List<CourseResponseDTO> courses = courseService.getCoursesByMinCredits(minCredits);
        
        return ResponseEntity.ok(AppResponse.<List<CourseResponseDTO>>builder()
                .status(HttpStatus.OK.value())
                .message("Courses retrieved successfully")
                .data(courses)
                .build());
    }
    
    /**
     * Get courses by credits range
     */
    @GetMapping("/credits-range/{minCredits}/{maxCredits}")
    @Operation(summary = "Get courses by credits range", description = "Retrieve courses with credits in specified range")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Courses retrieved successfully")
    })
    public ResponseEntity<AppResponse<Page<CourseResponseDTO>>> getCoursesByCreditsRange(
            @Parameter(description = "Minimum credits")
            @PathVariable Integer minCredits,
            @Parameter(description = "Maximum credits")
            @PathVariable Integer maxCredits,
            @PageableDefault(size = 10, page = 0, sort = "credits", direction = Sort.Direction.ASC)
            Pageable pageable) {
        
        log.info("GET /api/v1/courses/credits-range/{}/{} - Fetching courses", minCredits, maxCredits);
        
        Page<CourseResponseDTO> courses = courseService.getCoursesByCreditsInRange(minCredits, maxCredits, pageable);
        
        return ResponseEntity.ok(AppResponse.<Page<CourseResponseDTO>>builder()
                .status(HttpStatus.OK.value())
                .message("Courses retrieved successfully")
                .data(courses)
                .build());
    }
    
    /**
     * Update course
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update course", description = "Update an existing course record")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Course updated successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Course not found")
    })
    public ResponseEntity<AppResponse<CourseResponseDTO>> updateCourse(
            @Parameter(description = "Course ID")
            @PathVariable Long id,
            @Valid @RequestBody CourseUpdateDTO updateDTO) {
        
        log.info("PUT /api/v1/courses/{} - Updating course", id);
        
        CourseResponseDTO responseDTO = courseService.updateCourse(id, updateDTO);
        
        return ResponseEntity.ok(AppResponse.<CourseResponseDTO>builder()
                .status(HttpStatus.OK.value())
                .message("Course updated successfully")
                .data(responseDTO)
                .build());
    }
    
    /**
     * Activate course
     */
    @PatchMapping("/{id}/activate")
    @Operation(summary = "Activate course", description = "Activate a course")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Course activated successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Course not found")
    })
    public ResponseEntity<AppResponse<CourseResponseDTO>> activateCourse(
            @Parameter(description = "Course ID")
            @PathVariable Long id) {
        
        log.info("PATCH /api/v1/courses/{}/activate - Activating course", id);
        
        CourseResponseDTO responseDTO = courseService.activateCourse(id);
        
        return ResponseEntity.ok(AppResponse.<CourseResponseDTO>builder()
                .status(HttpStatus.OK.value())
                .message("Course activated successfully")
                .data(responseDTO)
                .build());
    }
    
    /**
     * Deactivate course
     */
    @PatchMapping("/{id}/deactivate")
    @Operation(summary = "Deactivate course", description = "Deactivate a course")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Course deactivated successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Course not found")
    })
    public ResponseEntity<AppResponse<CourseResponseDTO>> deactivateCourse(
            @Parameter(description = "Course ID")
            @PathVariable Long id) {
        
        log.info("PATCH /api/v1/courses/{}/deactivate - Deactivating course", id);
        
        CourseResponseDTO responseDTO = courseService.deactivateCourse(id);
        
        return ResponseEntity.ok(AppResponse.<CourseResponseDTO>builder()
                .status(HttpStatus.OK.value())
                .message("Course deactivated successfully")
                .data(responseDTO)
                .build());
    }
    
    /**
     * Delete course
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete course", description = "Delete a course record by ID")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "Course deleted successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Course not found")
    })
    public ResponseEntity<Void> deleteCourse(
            @Parameter(description = "Course ID")
            @PathVariable Long id) {
        
        log.info("DELETE /api/v1/courses/{} - Deleting course", id);
        
        courseService.deleteCourse(id);
        
        return ResponseEntity.noContent().build();
    }
    
    /**
     * Get all courses ordered by code
     */
    @GetMapping("/all/sorted")
    @Operation(summary = "Get all courses sorted by code", description = "Retrieve all courses ordered by course code")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Courses retrieved successfully")
    })
    public ResponseEntity<AppResponse<List<CourseResponseDTO>>> getAllCoursesSorted() {
        
        log.info("GET /api/v1/courses/all/sorted - Fetching all courses sorted");
        
        List<CourseResponseDTO> courses = courseService.getAllCoursesOrderedByCode();
        
        return ResponseEntity.ok(AppResponse.<List<CourseResponseDTO>>builder()
                .status(HttpStatus.OK.value())
                .message("Courses retrieved successfully")
                .data(courses)
                .build());
    }
}
