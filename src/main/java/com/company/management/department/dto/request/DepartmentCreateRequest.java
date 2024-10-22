package com.company.management.department.dto.request;

import jakarta.validation.constraints.NotEmpty;

public class DepartmentCreateRequest {
    @NotEmpty
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

