package com.company.management.department.dto.converter;

import com.company.management.department.dto.response.DepartmentDTO;
import com.company.management.department.model.Department;
import org.springframework.stereotype.Component;

@Component
public class DepartmentDTOConverter {

    public DepartmentDTO convert(Department department) {
        DepartmentDTO dto = new DepartmentDTO();
        dto.setId(department.getId());
        dto.setName(department.getName());
        return dto;
    }
}

