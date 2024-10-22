package com.company.management.department.service;

import com.company.management.common.service.GenericService;
import com.company.management.configuration.exception.AlreadyExistException;
import com.company.management.configuration.exception.BusinessException;
import com.company.management.configuration.exception.NotFoundException;
import com.company.management.department.dto.converter.DepartmentDTOConverter;
import com.company.management.department.dto.request.DepartmentCreateRequest;
import com.company.management.department.dto.request.DepartmentUpdateRequest;
import com.company.management.department.dto.response.DepartmentDTO;
import com.company.management.department.model.Department;
import com.company.management.department.repository.DepartmentRepository;
import com.company.management.employee.dto.response.EmployeeDTO;
import com.company.management.employee.service.EmployeeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DepartmentService implements GenericService<DepartmentDTO, DepartmentCreateRequest, DepartmentUpdateRequest> {

    private final DepartmentRepository departmentRepository;
    private final DepartmentDTOConverter departmentDTOConverter;
    private final EmployeeService employeeService;

    public DepartmentService(DepartmentRepository departmentRepository, DepartmentDTOConverter departmentDTOConverter,
                             EmployeeService employeeService) {
        this.departmentRepository = departmentRepository;
        this.departmentDTOConverter = departmentDTOConverter;
        this.employeeService = employeeService;
    }

    public DepartmentDTO create(DepartmentCreateRequest request) {
        validateNameIsAvailable(request.getName());

        Department department = new Department();
        department.setName(request.getName());
        Department savedDepartment = departmentRepository.save(department);

        return departmentDTOConverter.convert(savedDepartment);
    }

    public DepartmentDTO update(Long id, DepartmentUpdateRequest request) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("department.not.found"));

        validateNameIsAvailable(request.getName());

        Optional.ofNullable(request.getName())
                .filter(name -> !name.isBlank())
                .ifPresent(department::setName);

        Department savedDepartment = departmentRepository.save(department);

        return departmentDTOConverter.convert(savedDepartment);
    }

    public DepartmentDTO get(Long id) {
        return departmentRepository.findById(id)
                .map(departmentDTOConverter::convert)
                .orElseThrow(() -> new NotFoundException("department.not.found"));
    }

    public void delete(Long id) {
        long employeeCount = employeeService.countByDepartmentId(id);
        if (employeeCount != 0) {
            throw new BusinessException("department.contains.employees");
        }

        departmentRepository.deleteById(id);
    }

    public Page<DepartmentDTO> list(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return departmentRepository.findAll(pageable)
                .map(departmentDTOConverter::convert);
    }

    public Page<EmployeeDTO> listEmployees(Long id, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return employeeService.getByDepartmentId(id, pageable);
    }

    private void validateNameIsAvailable(String name) {
        if (departmentRepository.existsByName(name)) {
            throw new AlreadyExistException("department.already.exists");
        }
    }
}
