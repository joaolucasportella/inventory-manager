package dev.portella.inventory_manager.service;

import java.util.Optional;

import org.springframework.data.domain.Page;

public interface ICrudService<T> {
    Page<T> findPaginated(int page, int size);

    Optional<T> findById(String id);

    T findByIdOrThrow(String id);

    void save(T model);

    void deleteById(String id);
}
