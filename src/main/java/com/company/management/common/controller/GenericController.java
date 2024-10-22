package com.company.management.common.controller;

import com.company.management.common.service.GenericService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;

@Validated
public abstract class GenericController<D, C, U> {

    private final GenericService<D, C, U> service;

    protected GenericController(GenericService<D, C, U> service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public ResponseEntity<D> find(@PathVariable Long id) {
        D dto = service.get(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    public ResponseEntity<Page<D>> list(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size) {
        Page<D> entities = service.list(page, size);
        return ResponseEntity.ok(entities);
    }

    @PostMapping
    public ResponseEntity<D> create(@RequestBody @Valid C createRequest) {
        D dto = service.create(createRequest);
        return ResponseEntity.status(CREATED).body(dto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<D> update(@PathVariable Long id, @RequestBody @Valid U updateRequest) {
        D dto = service.update(id, updateRequest);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
