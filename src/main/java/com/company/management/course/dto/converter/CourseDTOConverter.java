package com.company.management.course.dto.converter;

import com.company.management.course.dto.response.CourseDTO;
import com.company.management.course.model.Course;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CourseDTOConverter {

    public CourseDTO convert(Course course) {
        CourseDTO dto = new CourseDTO();
        dto.setId(course.getId());
        dto.setName(course.getName());
        dto.setCode(course.getCode());
        return dto;
    }

    public List<CourseDTO> convertList(List<Course> courses) {
        return courses.stream()
                .map(this::convert)
                .toList();
    }
}

