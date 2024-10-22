package com.company.management.course.model;

import jakarta.persistence.*;

@Entity
@Table(name = "course",
        indexes = {
                @Index(name = "idx_course_code", columnList = "code", unique = true),
                @Index(name = "idx_course_name", columnList = "name", unique = true)
        })
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String code;

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}

