package dev.portella.inventory_manager.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import dev.portella.inventory_manager.dao.JpaLogDAO;
import dev.portella.inventory_manager.model.LogModel;

@Service
public class LogService implements ILogService {

    private final JpaLogDAO logDAO;

    public LogService(JpaLogDAO logDAO) {
        this.logDAO = logDAO;
    }

    public Page<LogModel> findPaginated(int page, int size) {
        if (size > 20 || size < 1) {
            size = 15;
        }

        Pageable pageable = PageRequest.of(page, size);
        return this.logDAO.findPaginated(pageable);
    }

    public Optional<LogModel> findById(String id) {
        if (id == null || id.isEmpty()) {
            return Optional.empty();
        }

        try {
            Long parsedId = Long.parseLong(id);
            return Optional.ofNullable(this.logDAO.findById(parsedId));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    public void save(LogModel logModel) {
        if (logModel.getLogId() == null) {
            this.logDAO.create(logModel);
        }
    }
}
