package com.company.management.department.controller;

import com.company.management.common.controller.GenericController;
import com.company.management.common.service.GenericService;
import com.company.management.department.dto.request.DepartmentCreateRequest;
import com.company.management.department.dto.request.DepartmentUpdateRequest;
import com.company.management.department.dto.response.DepartmentDTO;
import com.company.management.department.service.DepartmentService;
import com.company.management.employee.dto.response.EmployeeDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/departments")
public class DepartmentController extends GenericController<DepartmentDTO, DepartmentCreateRequest, DepartmentUpdateRequest> {

    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        super(departmentService);
        this.departmentService = departmentService;
    }

    @GetMapping("/{id}/employees")
    public ResponseEntity<Page<EmployeeDTO>> listEmployees(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size
    ) {
        Page<EmployeeDTO> employees = departmentService.listEmployees(id, page, size);
        return ResponseEntity
                .ok()
                .body(employees);
    }
}
