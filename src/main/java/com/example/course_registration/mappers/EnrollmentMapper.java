package com.example.course_registration.mappers;

import com.example.course_registration.DTOs.EnrollmentCreateDTO;
import com.example.course_registration.DTOs.EnrollmentResponseDTO;
import com.example.course_registration.DTOs.EnrollmentUpdateDTO;
import com.example.course_registration.entities.Enrollment;
import com.example.course_registration.entities.EnrollmentStatus;
import org.springframework.stereotype.Component;

@Component
public class EnrollmentMapper {
    
    public Enrollment toEntity(EnrollmentCreateDTO createDTO) {
        if (createDTO == null) {
            return null;
        }
        
        return Enrollment.builder()
                .status(EnrollmentStatus.REGISTERED)
                .build();
    }
    
    public EnrollmentResponseDTO toResponseDTO(Enrollment entity) {
        if (entity == null) {
            return null;
        }
        
        EnrollmentResponseDTO.StudentBasicDTO studentDTO = null;
        if (entity.getStudent() != null) {
            studentDTO = EnrollmentResponseDTO.StudentBasicDTO.builder()
                    .id(entity.getStudent().getId())
                    .studentCode(entity.getStudent().getStudentCode())
                    .fullName(entity.getStudent().getFullName())
                    .email(entity.getStudent().getEmail())
                    .academicYear(entity.getStudent().getAcademicYear())
                    .build();
        }
        
        EnrollmentResponseDTO.CourseSectionBasicDTO sectionDTO = null;
        if (entity.getCourseSection() != null) {
            sectionDTO = EnrollmentResponseDTO.CourseSectionBasicDTO.builder()
                    .id(entity.getCourseSection().getId())
                    .sectionCode(entity.getCourseSection().getSectionCode())
                    .currentSlots(entity.getCourseSection().getCurrentSlots())
                    .maxSlots(entity.getCourseSection().getMaxSlots())
                    .dayOfWeek(entity.getCourseSection().getDayOfWeek())
                    .startPeriod(entity.getCourseSection().getStartPeriod())
                    .endPeriod(entity.getCourseSection().getEndPeriod())
                    .room(entity.getCourseSection().getRoom())
                    .build();
        }
        
        return EnrollmentResponseDTO.builder()
                .id(entity.getId())
                .studentId(entity.getStudent() != null ? entity.getStudent().getId() : null)
                .sectionId(entity.getCourseSection() != null ? entity.getCourseSection().getId() : null)
                .student(studentDTO)
                .courseSection(sectionDTO)
                .status(entity.getStatus())
                .registeredAt(entity.getRegisteredAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
    
    public void updateEntity(EnrollmentUpdateDTO updateDTO, Enrollment entity) {
        if (updateDTO == null) {
            return;
        }
        
        if (updateDTO.getStatus() != null) {
            entity.setStatus(updateDTO.getStatus());
        }
    }
}
