package com.company.management.course.dto.request;

import jakarta.validation.constraints.NotEmpty;

public class CourseCreateRequest {
    @NotEmpty
    private String name;

    @NotEmpty
    private String code;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}

