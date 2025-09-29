package dev.portella.inventory_manager.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import dev.portella.inventory_manager.dao.ProductDAO;
import dev.portella.inventory_manager.model.ProductModel;

@Service
public class ProductService {

    private final ProductDAO productDAO;
    private static final String PRODUCT_NOT_FOUND_MESSAGE = "Produto não encontrado.";

    public ProductService(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    public Page<ProductModel> findPaginated(int page, int size) {
        if (size > 20 || size < 1) {
            size = 15;
        }

        Pageable pageable = PageRequest.of(page, size);
        return this.productDAO.findPaginated(pageable);
    }

    public Optional<ProductModel> findById(String id) {
        if (id == null || id.isEmpty()) {
            return Optional.empty();
        }

        try {
            Long parsedId = Long.parseLong(id);
            return Optional.ofNullable(this.productDAO.findById(parsedId));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    public ProductModel findByIdOrThrow(String id) {
        return findById(id).orElseThrow(() -> new IllegalArgumentException(PRODUCT_NOT_FOUND_MESSAGE));
    }

    public void save(ProductModel product) {
        if (product.getProductId() == null) {
            this.productDAO.create(product);
        } else {
            this.productDAO.update(product);
        }
    }

    public void deleteById(String id) {
        ProductModel product = findByIdOrThrow(id);
        this.productDAO.delete(product);
    }

    public boolean isUnique(Long id, String field, String value) {
        List<ProductModel> products = this.productDAO.findByField(field, value);
        products.removeIf(product -> product.getProductId().equals(id));

        return products.isEmpty();
    }
}