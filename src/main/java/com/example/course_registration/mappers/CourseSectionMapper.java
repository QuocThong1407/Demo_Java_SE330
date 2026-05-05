package com.example.course_registration.mappers;

import com.example.course_registration.DTOs.CourseSectionCreateDTO;
import com.example.course_registration.DTOs.CourseSectionResponseDTO;
import com.example.course_registration.DTOs.CourseSectionUpdateDTO;
import com.example.course_registration.entities.CourseSection;
import org.springframework.stereotype.Component;

@Component
public class CourseSectionMapper {
    
    public CourseSection toEntity(CourseSectionCreateDTO createDTO) {
        if (createDTO == null) {
            return null;
        }
        
        return CourseSection.builder()
                .sectionCode(createDTO.getSectionCode())
                .maxSlots(createDTO.getMaxSlots())
                .currentSlots(createDTO.getCurrentSlots())
                .semester(createDTO.getSemester())
                .year(createDTO.getYear())
                .dayOfWeek(createDTO.getDayOfWeek())
                .startPeriod(createDTO.getStartPeriod())
                .endPeriod(createDTO.getEndPeriod())
                .room(createDTO.getRoom())
                .build();
    }
    
    public CourseSectionResponseDTO toResponseDTO(CourseSection entity) {
        if (entity == null) {
            return null;
        }

        CourseSectionResponseDTO.CourseBasicInfoDTO courseDTO = null;
        if (entity.getCourse() != null) {
            courseDTO = CourseSectionResponseDTO.CourseBasicInfoDTO.builder()
                    .id(entity.getCourse().getId())
                    .courseCode(entity.getCourse().getCourseCode())
                    .courseName(entity.getCourse().getCourseName())
                    .credits(entity.getCourse().getCredits())
                    .build();
        }
        
        Long courseId = entity.getCourse() != null ? entity.getCourse().getId() : null;
        Long prerequisiteId = entity.getPrerequisiteCourse() != null ? entity.getPrerequisiteCourse().getId() : null;
        
        return CourseSectionResponseDTO.builder()
                .id(entity.getId())
                .courseId(courseId)
                .course(courseDTO)
                .sectionCode(entity.getSectionCode())
                .maxSlots(entity.getMaxSlots())
                .currentSlots(entity.getCurrentSlots())
                .semester(entity.getSemester())
                .year(entity.getYear())
                .dayOfWeek(entity.getDayOfWeek())
                .startPeriod(entity.getStartPeriod())
                .endPeriod(entity.getEndPeriod())
                .room(entity.getRoom())
                .prerequisiteCourseId(prerequisiteId)
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
    
    public void updateEntity(CourseSectionUpdateDTO updateDTO, CourseSection entity) {
        if (updateDTO == null) {
            return;
        }
        
        if (updateDTO.getSectionCode() != null) {
            entity.setSectionCode(updateDTO.getSectionCode());
        }
        if (updateDTO.getMaxSlots() != null) {
            entity.setMaxSlots(updateDTO.getMaxSlots());
        }
        if (updateDTO.getCurrentSlots() != null) {
            entity.setCurrentSlots(updateDTO.getCurrentSlots());
        }
        if (updateDTO.getSemester() != null) {
            entity.setSemester(updateDTO.getSemester());
        }
        if (updateDTO.getYear() != null) {
            entity.setYear(updateDTO.getYear());
        }
        if (updateDTO.getDayOfWeek() != null) {
            entity.setDayOfWeek(updateDTO.getDayOfWeek());
        }
        if (updateDTO.getStartPeriod() != null) {
            entity.setStartPeriod(updateDTO.getStartPeriod());
        }
        if (updateDTO.getEndPeriod() != null) {
            entity.setEndPeriod(updateDTO.getEndPeriod());
        }
        if (updateDTO.getRoom() != null) {
            entity.setRoom(updateDTO.getRoom());
        }
        // Note: prerequisiteCourseId is handled by service layer
    }
}
