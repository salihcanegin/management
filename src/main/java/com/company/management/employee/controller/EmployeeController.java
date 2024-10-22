package com.company.management.employee.controller;

import com.company.management.common.controller.GenericController;
import com.company.management.employee.dto.request.EmployeeCreateRequest;
import com.company.management.employee.dto.request.EmployeeUpdateRequest;
import com.company.management.employee.dto.response.EmployeeDTO;
import com.company.management.employee.service.EmployeeService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/employees")
public class EmployeeController extends GenericController<EmployeeDTO, EmployeeCreateRequest, EmployeeUpdateRequest> {

    public EmployeeController(EmployeeService employeeService) {
        super(employeeService);
    }
}