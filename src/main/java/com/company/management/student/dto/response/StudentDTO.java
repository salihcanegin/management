package com.company.management.student.dto.response;

import com.company.management.course.dto.response.CourseDTO;
import com.company.management.course.model.Course;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

public class StudentDTO {
    private Long id;
    private String name;
    private String email;
    private List<CourseDTO> courses = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<CourseDTO> getCourses() {
        return courses;
    }

    public void setCourses(List<CourseDTO> courses) {
        this.courses = courses;
    }
}

