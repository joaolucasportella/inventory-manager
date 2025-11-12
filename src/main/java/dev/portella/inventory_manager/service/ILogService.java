package dev.portella.inventory_manager.service;

import java.util.Optional;

import org.springframework.data.domain.Page;

import dev.portella.inventory_manager.model.LogModel;

public interface ILogService {
    Page<LogModel> findPaginated(int page, int size);

    Optional<LogModel> findById(String id);

    void save(LogModel model);
}
