package com.company.management.student.repository;

import com.company.management.student.model.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Page<Student> findByCourses_Id(Long courseId, Pageable pageable);
    boolean existsByCourses_Id(Long courseId);
    boolean existsByEmail(String email);
}
