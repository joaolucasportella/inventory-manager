package dev.portella.inventory_manager.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import dev.portella.inventory_manager.model.LogModel;

public interface ILogDAO {
    Page<LogModel> findPaginated(Pageable pageable);

    LogModel findById(Long id);

    void create(LogModel productModel);
}