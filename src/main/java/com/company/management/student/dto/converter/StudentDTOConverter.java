package com.company.management.student.dto.converter;

import com.company.management.course.dto.converter.CourseDTOConverter;
import com.company.management.student.dto.response.StudentDTO;
import com.company.management.student.dto.response.StudentDTOSummary;
import com.company.management.student.model.Student;
import org.springframework.stereotype.Component;

@Component
public class StudentDTOConverter {
    private final CourseDTOConverter courseDTOConverter;

    public StudentDTOConverter(CourseDTOConverter courseDTOConverter) {
        this.courseDTOConverter = courseDTOConverter;
    }

    public StudentDTO convert(Student student) {
        StudentDTO dto = new StudentDTO();
        dto.setId(student.getId());
        dto.setName(student.getName());
        dto.setEmail(student.getEmail());
        dto.setCourses(courseDTOConverter.convertList(student.getCourses()));
        return dto;
    }

    public StudentDTOSummary convertToSummary(Student student) {
        StudentDTOSummary dto = new StudentDTOSummary();
        dto.setId(student.getId());
        dto.setName(student.getName());
        dto.setEmail(student.getEmail());
        return dto;
    }
}

