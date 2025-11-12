package dev.portella.inventory_manager.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.portella.inventory_manager.dao.JpaStockMovementDAO;
import dev.portella.inventory_manager.dao.JpaStockDAO;
import dev.portella.inventory_manager.model.MovementTypeEnum;
import dev.portella.inventory_manager.model.StockModel;
import dev.portella.inventory_manager.model.StockMovementModel;

@Service
public class StockMovementService implements CrudService<StockMovementModel> {

    private final JpaStockMovementDAO stockMovementDAO;
    private final JpaStockDAO stockDAO;

    private static final String NOT_FOUND_MESSAGE = "Movimentação de estoque não encontrada.";
    private static final String INSUFFICIENT_STOCK_MESSAGE = "Quantidade insuficiente em estoque para realizar a transferência.";

    public StockMovementService(JpaStockMovementDAO stockMovementDAO, JpaStockDAO stockDAO) {
        this.stockMovementDAO = stockMovementDAO;
        this.stockDAO = stockDAO;
    }

    @Override
    public Page<StockMovementModel> findPaginated(int page, int size) {
        if (size > 20 || size < 1) {
            size = 15;
        }

        Pageable pageable = PageRequest.of(page, size);
        return this.stockMovementDAO.findPaginated(pageable);
    }

    @Override
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

    @Override
    public StockMovementModel findByIdOrThrow(String id) {
        return findById(id).orElseThrow(() -> new IllegalArgumentException(NOT_FOUND_MESSAGE));
    }

    @Override
    @Transactional
    public void save(StockMovementModel movement) {
        if (movement == null || movement.getProduct() == null) {
            throw new IllegalArgumentException("Movimentação ou produto inválido.");
        }

        List<StockModel> stockByProduct = stockDAO.findByField(
            "product.product_id",
            movement.getProduct().getProductId()
        );

        StockModel sourceStock = null;
        for (StockModel stockModel : stockByProduct) {
            if (stockModel.getArea().equals(movement.getSourceArea())) {
                sourceStock = stockModel;
                break;
            }
        }

        if (sourceStock == null) {
            throw new IllegalArgumentException("Produto não encontrado no estoque de origem.");
        }

        Integer quantity = movement.getQuantity();
        MovementTypeEnum type = movement.getMovementType();

        switch (type) {
            case entry:
                sourceStock.setQuantity(sourceStock.getQuantity() + quantity);
                break;

            case exit:
                if (sourceStock.getQuantity() < quantity) {
                    throw new IllegalArgumentException(INSUFFICIENT_STOCK_MESSAGE);
                }
                sourceStock.setQuantity(sourceStock.getQuantity() - quantity);
                break;

            case transfer:
                if (sourceStock.getQuantity() < quantity) {
                    throw new IllegalArgumentException(INSUFFICIENT_STOCK_MESSAGE);
                }

                if (movement.getDestinationArea() == null) {
                    throw new IllegalArgumentException("Área de destino não informada para a transferência.");
                }
                
                sourceStock.setQuantity(sourceStock.getQuantity() - quantity);

                StockModel destinationStock = null;
                for (StockModel stockModel : stockByProduct) {
                    if (stockModel.getArea().equals(movement.getDestinationArea())) {
                        destinationStock = stockModel;
                        break;
                    }
                }

                if (destinationStock == null) {
                    destinationStock = new StockModel();
                    destinationStock.setProduct(sourceStock.getProduct());
                    destinationStock.setArea(movement.getDestinationArea());
                    destinationStock.setQuantity(quantity);
                    stockDAO.create(destinationStock);
                } else {
                    destinationStock.setQuantity(destinationStock.getQuantity() + quantity);
                    stockDAO.update(destinationStock);
                }

                break;
        }

        stockDAO.update(sourceStock);

        if (movement.getMovementId() == null) {
            this.stockMovementDAO.create(movement);
        } else {
            this.stockMovementDAO.update(movement);
        }
    }

    @Override
    public void deleteById(String id) {
        StockMovementModel movement = findByIdOrThrow(id);
        this.stockMovementDAO.delete(movement);
    }
}
