package dev.portella.inventory_manager.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IDAO<T> {
    Page<T> findPaginated(Pageable pageable);

    T findById(Long id);

    List<T> findByField(String field, Object value);

    void create(T productModel);

    void update(T productModel);

    void delete(T productModel);
}