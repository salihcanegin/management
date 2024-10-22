package com.company.management.employee.service;

import com.company.management.common.service.GenericService;
import com.company.management.configuration.exception.AlreadyExistException;
import com.company.management.configuration.exception.NotFoundException;
import com.company.management.department.model.Department;
import com.company.management.department.repository.DepartmentRepository;
import com.company.management.employee.dto.converter.EmployeeDTOConverter;
import com.company.management.employee.dto.request.EmployeeCreateRequest;
import com.company.management.employee.dto.request.EmployeeUpdateRequest;
import com.company.management.employee.dto.response.EmployeeDTO;
import com.company.management.employee.model.Employee;
import com.company.management.employee.repository.EmployeeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EmployeeService implements GenericService<EmployeeDTO, EmployeeCreateRequest, EmployeeUpdateRequest> {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final EmployeeDTOConverter employeeDTOConverter;

    public EmployeeService(EmployeeRepository employeeRepository,
                           DepartmentRepository departmentRepository,
                           EmployeeDTOConverter employeeDTOConverter) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
        this.employeeDTOConverter = employeeDTOConverter;
    }

    public EmployeeDTO create(EmployeeCreateRequest employeeCreateRequest) {
        validateEmailIsAvailable(employeeCreateRequest.getEmail());

        Department department = departmentRepository.findById(employeeCreateRequest.getDepartmentId())
                .orElseThrow(() -> new NotFoundException("department.not.found"));

        Employee employee = new Employee();
        employee.setName(employeeCreateRequest.getName());
        employee.setEmail(employeeCreateRequest.getEmail());
        employee.setDepartment(department);
        Employee savedEmployee = employeeRepository.save(employee);

        return employeeDTOConverter.convert(savedEmployee);
    }

    public EmployeeDTO update(Long id, EmployeeUpdateRequest request) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("employee.not.found"));

        if (!employee.getEmail().equals(request.getEmail())) {
            validateEmailIsAvailable(request.getEmail());
        }

        Optional.ofNullable(request.getDepartmentId())
                .ifPresent(departmentId -> {
                    Department department = departmentRepository.findById(departmentId)
                            .orElseThrow(() -> new NotFoundException("department.not.found"));
                    employee.setDepartment(department);
                });

        Optional.ofNullable(request.getName())
                .filter(name -> !name.isBlank())
                .ifPresent(employee::setName);

        Optional.ofNullable(request.getEmail())
                .ifPresent(employee::setEmail);

        Employee savedEmployee = employeeRepository.save(employee);

        return employeeDTOConverter.convert(savedEmployee);
    }

    public Page<EmployeeDTO> list(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return employeeRepository.findAll(pageable)
                .map(employeeDTOConverter::convert);
    }

    public EmployeeDTO get(Long id) {
        return employeeRepository.findById(id)
                .map(employeeDTOConverter::convert)
                .orElseThrow(() -> new NotFoundException("employee.not.found"));
    }

    public void delete(Long id) {
        employeeRepository.deleteById(id);
    }

    public Page<EmployeeDTO> getByDepartmentId(Long departmentId, Pageable pageable) {
        return employeeRepository.findAllByDepartmentId(departmentId, pageable)
                .map(employeeDTOConverter::convert);
    }

    public long countByDepartmentId(Long departmentId) {
        return employeeRepository.countByDepartmentId(departmentId);
    }

    private void validateEmailIsAvailable(String email) {
        if (employeeRepository.existsByEmail(email)) {
            throw new AlreadyExistException("employee.already.exists");
        }
    }
}
