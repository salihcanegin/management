package com.company.management.student.controller;

import com.company.management.common.controller.GenericController;
import com.company.management.student.dto.request.StudentCreateRequest;
import com.company.management.student.dto.request.StudentUpdateRequest;
import com.company.management.student.dto.response.StudentDTO;
import com.company.management.student.service.StudentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/v1/students")
public class StudentController extends GenericController<StudentDTO, StudentCreateRequest, StudentUpdateRequest> {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        super(studentService);
        this.studentService = studentService;
    }

    @PostMapping("/{id}/courses/{courseId}")
    public ResponseEntity<StudentDTO> addCourse(
            @PathVariable Long id,
            @PathVariable Long courseId
    ) {
        StudentDTO student = studentService.addCourse(id, courseId);
        return ResponseEntity
                .status(CREATED)
                .body(student);
    }

    @DeleteMapping("/{id}/courses/{courseId}")
    public ResponseEntity<Void> removeCourse(
            @PathVariable Long id,
            @PathVariable Long courseId
    ) {
        studentService.removeCourse(id, courseId);
        return ResponseEntity
                .noContent()
                .build();
    }
}