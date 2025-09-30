package dev.portella.inventory_manager.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import dev.portella.inventory_manager.dao.JpaStockDAO;
import dev.portella.inventory_manager.model.StockModel;

@Service
public class StockService {

    private final JpaStockDAO stockDAO;
    private static final String NOT_FOUND_MESSAGE = "Estoque não encontrado.";

    public StockService(JpaStockDAO stockDAO) {
        this.stockDAO = stockDAO;
    }

    public Page<StockModel> findPaginated(int page, int size) {
        if (size > 20 || size < 1) {
            size = 15;
        }

        Pageable pageable = PageRequest.of(page, size);
        return this.stockDAO.findPaginated(pageable);
    }

    public Optional<StockModel> findById(String id) {
        if (id == null || id.isEmpty()) {
            return Optional.empty();
        }

        try {
            Long parsedId = Long.parseLong(id);
            return Optional.ofNullable(this.stockDAO.findById(parsedId));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    public StockModel findByIdOrThrow(String id) {
        return findById(id).orElseThrow(() -> new IllegalArgumentException(NOT_FOUND_MESSAGE));
    }

    public void save(StockModel stock) {
        if (stock.getStockId() == null) {
            this.stockDAO.create(stock);
        } else {
            this.stockDAO.update(stock);
        }
    }

    public void deleteById(String id) {
        StockModel stock = findByIdOrThrow(id);
        this.stockDAO.delete(stock);
    }
}
