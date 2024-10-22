package com.company.management.student.service;

import com.company.management.common.service.GenericService;
import com.company.management.configuration.exception.AlreadyExistException;
import com.company.management.configuration.exception.BusinessException;
import com.company.management.configuration.exception.NotFoundException;
import com.company.management.course.model.Course;
import com.company.management.course.repository.CourseRepository;
import com.company.management.student.dto.converter.StudentDTOConverter;
import com.company.management.student.dto.request.StudentCreateRequest;
import com.company.management.student.dto.request.StudentUpdateRequest;
import com.company.management.student.dto.response.StudentDTO;
import com.company.management.student.dto.response.StudentDTOSummary;
import com.company.management.student.model.Student;
import com.company.management.student.repository.StudentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StudentService implements GenericService<StudentDTO, StudentCreateRequest, StudentUpdateRequest> {

    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final StudentDTOConverter studentDTOConverter;

    public StudentService(StudentRepository studentRepository,
                          CourseRepository courseRepository,
                          StudentDTOConverter studentDTOConverter) {
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
        this.studentDTOConverter = studentDTOConverter;
    }

    public StudentDTO create(StudentCreateRequest studentCreateRequest) {
        validateEmailIsAvailable(studentCreateRequest.getEmail());

        Student student = new Student();
        student.setName(studentCreateRequest.getName());
        student.setEmail(studentCreateRequest.getEmail());
        Student savedStudent = studentRepository.save(student);

        return studentDTOConverter.convert(savedStudent);
    }

    public StudentDTO update(Long id, StudentUpdateRequest request) {
        Student student = getStudent(id);

        if (!student.getEmail().equals(request.getEmail())) {
            validateEmailIsAvailable(request.getEmail());
        }

        Optional.ofNullable(request.getName())
                .filter(name -> !name.isBlank())
                .ifPresent(student::setName);

        Optional.ofNullable(request.getEmail())
                .ifPresent(student::setEmail);

        Student savedStudent = studentRepository.save(student);

        return studentDTOConverter.convert(savedStudent);
    }

    public StudentDTO addCourse(Long id, Long courseId) {
        Student student = getStudent(id);
        Course course = getCourse(courseId);

        if (student.getCourses().stream()
                .anyMatch(c -> c.getCode().equals(course.getCode()))) {
            throw new BusinessException("student.already.enrolled");
        }

        student.getCourses().add(course);

        Student savedStudent = studentRepository.save(student);
        return studentDTOConverter.convert(savedStudent);
    }

    public void removeCourse(Long id, Long courseId) {
        Student student = getStudent(id);
        Course course = getCourse(courseId);

        if (student.getCourses().stream()
                .noneMatch(c -> c.getCode().equals(course.getCode()))) {
            throw new BusinessException("student.is.not.enrolled");
        }

        student.getCourses().remove(course);
        studentRepository.save(student);
    }

    public Page<StudentDTO> list(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return studentRepository.findAll(pageable)
                .map(studentDTOConverter::convert);
    }

    public StudentDTO get(Long id) {
        return studentRepository.findById(id)
                .map(studentDTOConverter::convert)
                .orElseThrow(() -> new NotFoundException("student.not.found"));
    }

    public void delete(Long id) {
        studentRepository.deleteById(id);
    }

    public Page<StudentDTOSummary> getByCourseId(Long courseId, Pageable pageable) {
        return studentRepository.findByCourses_Id(courseId, pageable)
                .map(studentDTOConverter::convertToSummary);
    }

    public boolean existsByCoursesId(Long courseId) {
        return studentRepository.existsByCourses_Id(courseId);
    }

    private void validateEmailIsAvailable(String studentUpdateRequest) {
        if (studentRepository.existsByEmail(studentUpdateRequest)) {
            throw new AlreadyExistException("student.already.exists");
        }
    }

    private Student getStudent(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("student.not.found"));
    }

    private Course getCourse(Long courseId) {
        return courseRepository.findById(courseId)
                .orElseThrow(() -> new NotFoundException("course.not.found"));
    }
}