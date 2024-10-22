package com.company.management.course.controller;

import com.company.management.common.controller.GenericController;
import com.company.management.course.dto.request.CourseCreateRequest;
import com.company.management.course.dto.request.CourseUpdateRequest;
import com.company.management.course.dto.response.CourseDTO;
import com.company.management.course.service.CourseService;
import com.company.management.student.dto.response.StudentDTOSummary;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/courses")
public class CourseController extends GenericController<CourseDTO, CourseCreateRequest, CourseUpdateRequest> {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        super(courseService);
        this.courseService = courseService;
    }

    @GetMapping("/{id}/students")
    public ResponseEntity<Page<StudentDTOSummary>> listStudents(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size
    ) {
        Page<StudentDTOSummary> employees = courseService.listStudents(id, page, size);
        return ResponseEntity
                .ok()
                .body(employees);
    }
}
