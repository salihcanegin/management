package com.company.management.course.service;

import com.company.management.common.service.GenericService;
import com.company.management.configuration.exception.AlreadyExistException;
import com.company.management.configuration.exception.BusinessException;
import com.company.management.configuration.exception.NotFoundException;
import com.company.management.course.dto.converter.CourseDTOConverter;
import com.company.management.course.dto.request.CourseCreateRequest;
import com.company.management.course.dto.request.CourseUpdateRequest;
import com.company.management.course.dto.response.CourseDTO;
import com.company.management.course.model.Course;
import com.company.management.course.repository.CourseRepository;
import com.company.management.student.dto.response.StudentDTOSummary;
import com.company.management.student.service.StudentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CourseService implements GenericService<CourseDTO, CourseCreateRequest, CourseUpdateRequest> {

    private final CourseRepository courseRepository;
    private final CourseDTOConverter courseDTOConverter;
    private final StudentService studentService;

    public CourseService(CourseRepository courseRepository, CourseDTOConverter courseDTOConverter, StudentService studentService) {
        this.courseRepository = courseRepository;
        this.courseDTOConverter = courseDTOConverter;
        this.studentService = studentService;
    }

    public CourseDTO create(CourseCreateRequest request) {
        validateCodeIsAvailable(request.getCode());

        Course course = new Course();
        course.setName(request.getName());
        course.setCode(request.getCode());
        Course savedCourse = courseRepository.save(course);

        return courseDTOConverter.convert(savedCourse);
    }

    public CourseDTO update(Long id, CourseUpdateRequest request) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("course.not.found"));

        if (!course.getCode().equals(request.getCode())) {
            validateCodeIsAvailable(request.getCode());
        }

        Optional.ofNullable(request.getName())
                .filter(name -> !name.isBlank())
                .ifPresent(course::setName);

        Optional.ofNullable(request.getCode())
                .filter(code -> !code.isBlank())
                .ifPresent(course::setCode);

        Course savedCourse = courseRepository.save(course);

        return courseDTOConverter.convert(savedCourse);
    }

    public CourseDTO get(Long id) {
        return courseRepository.findById(id)
                .map(courseDTOConverter::convert)
                .orElseThrow(() -> new NotFoundException("course.not.found"));
    }

    public void delete(Long id) {
        boolean studentsExist = studentService.existsByCoursesId(id);
        if (studentsExist) {
            throw new BusinessException("course.contains.students");
        }

        courseRepository.deleteById(id);
    }

    public Page<CourseDTO> list(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return courseRepository.findAll(pageable)
                .map(courseDTOConverter::convert);
    }

    public Page<StudentDTOSummary> listStudents(Long id, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return studentService.getByCourseId(id, pageable);
    }

    private void validateCodeIsAvailable(String code) {
        if (courseRepository.existsByCode(code)) {
            throw new AlreadyExistException("course.already.exists");
        }
    }
}