package dev.portella.inventory_manager.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import dev.portella.inventory_manager.model.ProductModel;

public interface ProductDAO {
    Page<ProductModel> findPaginated(Pageable pageable);

    ProductModel findById(Long id);

    List<ProductModel> findByField(String field, Object value);

    void create(ProductModel productModel);

    void update(ProductModel productModel);

    void delete(ProductModel productModel);
}