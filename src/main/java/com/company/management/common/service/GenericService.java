package com.company.management.common.service;

import org.springframework.data.domain.Page;

public interface GenericService<D, C, U> {
    D get(Long id);

    Page<D> list(int page, int size);

    D create(C createRequest);

    D update(Long id, U updateRequest);

    void delete(Long id);
}
