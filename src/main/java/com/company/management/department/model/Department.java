package com.company.management.department.model;

import jakarta.persistence.*;

@Entity
@Table(name = "department",
        indexes = {
                @Index(name = "idx_department_name", columnList = "name", unique = true)
        })
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

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
}

