package com.company.management.employee.repository;

import com.company.management.employee.model.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Page<Employee> findAllByDepartmentId(Long departmentId, Pageable pageable);
    long countByDepartmentId(Long departmentId);
    boolean existsByEmail(String email);
}
