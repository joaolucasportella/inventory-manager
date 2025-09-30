package dev.portella.inventory_manager.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import dev.portella.inventory_manager.dao.JpaStockMovementDAO;
import dev.portella.inventory_manager.model.StockMovementModel;

@Service
public class StockMovementService {

    private final JpaStockMovementDAO stockMovementDAO;
    private static final String NOT_FOUND_MESSAGE = "Estoque não encontrado.";

    public StockMovementService(JpaStockMovementDAO stockMovementDAO) {
        this.stockMovementDAO = stockMovementDAO;
    }

    public Page<StockMovementModel> findPaginated(int page, int size) {
        if (size > 20 || size < 1) {
            size = 15;
        }

        Pageable pageable = PageRequest.of(page, size);
        return this.stockMovementDAO.findPaginated(pageable);
    }

    public Optional<StockMovementModel> findById(String id) {
        if (id == null || id.isEmpty()) {
            return Optional.empty();
        }

        try {
            Long parsedId = Long.parseLong(id);
            return Optional.ofNullable(this.stockMovementDAO.findById(parsedId));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    public StockMovementModel findByIdOrThrow(String id) {
        return findById(id).orElseThrow(() -> new IllegalArgumentException(NOT_FOUND_MESSAGE));
    }

    public void save(StockMovementModel stockMovement) {
        if (stockMovement.getMovementId() == null) {
            this.stockMovementDAO.create(stockMovement);
        } else {
            this.stockMovementDAO.update(stockMovement);
        }
    }

    public void deleteById(String id) {
        StockMovementModel stockMovement = findByIdOrThrow(id);
        this.stockMovementDAO.delete(stockMovement);
    }
}
