package dev.portella.inventory_manager.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "stock_movement", schema = "inventory")
public class StockMovementModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "movement_id")
    private Long movementId;

    @NotNull(message = "{stockMovement.product.notNull}")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private ProductModel product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_area_id")
    private StorageAreaModel sourceArea;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destination_area_id")
    private StorageAreaModel destinationArea;

    @NotNull(message = "{stockMovement.quantity.notNull}")
    @Column(nullable = false)
    private Integer quantity;

    @NotNull(message = "{stockMovement.movementType.notNull}")
    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "movement_type", nullable = false, columnDefinition = "inventory.movement_type_enum")
    private MovementTypeEnum movementType;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "movement_date", nullable = false, updatable = false, insertable = false)
    private LocalDateTime movementDate;

    public Long getMovementId() {
        return movementId;
    }

    public void setMovementId(Long movementId) {
        this.movementId = movementId;
    }

    public ProductModel getProduct() {
        return product;
    }

    public void setProduct(ProductModel product) {
        this.product = product;
    }

    public StorageAreaModel getSourceArea() {
        return sourceArea;
    }

    public void setSourceArea(StorageAreaModel sourceArea) {
        this.sourceArea = sourceArea;
    }

    public StorageAreaModel getDestinationArea() {
        return destinationArea;
    }

    public void setDestinationArea(StorageAreaModel destinationArea) {
        this.destinationArea = destinationArea;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public MovementTypeEnum getMovementType() {
        return movementType;
    }

    public void setMovementType(MovementTypeEnum movementType) {
        this.movementType = movementType;
    }

    public LocalDateTime getMovementDate() {
        return movementDate;
    }

    public void setMovementDate(LocalDateTime movementDate) {
        this.movementDate = movementDate;
    }
}
