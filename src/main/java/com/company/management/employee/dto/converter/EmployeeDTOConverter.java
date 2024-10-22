package com.company.management.employee.dto.converter;

import com.company.management.employee.dto.response.EmployeeDTO;
import com.company.management.employee.model.Employee;
import org.springframework.stereotype.Component;

@Component
public class EmployeeDTOConverter {

    public EmployeeDTO convert(Employee employee) {
        EmployeeDTO dto = new EmployeeDTO();
        dto.setId(employee.getId());
        dto.setName(employee.getName());
        dto.setEmail(employee.getEmail());
        dto.setDepartmentId(employee.getDepartment().getId());
        return dto;
    }
}

