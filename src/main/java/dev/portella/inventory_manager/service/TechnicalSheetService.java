package dev.portella.inventory_manager.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import dev.portella.inventory_manager.dao.JpaTechnicalSheetDAO;
import dev.portella.inventory_manager.model.TechnicalSheetModel;

@Service
public class TechnicalSheetService {

    private final JpaTechnicalSheetDAO technicalSheetDAO;
    private static final String NOT_FOUND_MESSAGE = "Ficha técnica não encontrada.";

    public TechnicalSheetService(JpaTechnicalSheetDAO technicalSheetDAO) {
        this.technicalSheetDAO = technicalSheetDAO;
    }

    public Page<TechnicalSheetModel> findPaginated(int page, int size) {
        if (size > 20 || size < 1) {
            size = 15;
        }

        Pageable pageable = PageRequest.of(page, size);
        return this.technicalSheetDAO.findPaginated(pageable);
    }

    public Optional<TechnicalSheetModel> findById(String id) {
        if (id == null || id.isEmpty()) {
            return Optional.empty();
        }

        try {
            Long parsedId = Long.parseLong(id);
            return Optional.ofNullable(this.technicalSheetDAO.findById(parsedId));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    public TechnicalSheetModel findByIdOrThrow(String id) {
        return findById(id).orElseThrow(() -> new IllegalArgumentException(NOT_FOUND_MESSAGE));
    }

    public void save(TechnicalSheetModel technicalSheetModel) {
        if (technicalSheetModel.getSheetId() == null) {
            this.technicalSheetDAO.create(technicalSheetModel);
        } else {
            this.technicalSheetDAO.update(technicalSheetModel);
        }
    }

    public void deleteById(String id) {
        TechnicalSheetModel technicalSheetModel = findByIdOrThrow(id);
        this.technicalSheetDAO.delete(technicalSheetModel);
    }
}
