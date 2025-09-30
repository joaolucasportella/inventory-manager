package dev.portella.inventory_manager.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import dev.portella.inventory_manager.dao.JpaStorageAreaDAO;
import dev.portella.inventory_manager.model.StorageAreaModel;

@Service
public class StorageAreaService {

    private final JpaStorageAreaDAO storageAreaDAO;
    private static final String NOT_FOUND_MESSAGE = "Tipo de armazenamento não encontrado.";

    public StorageAreaService(JpaStorageAreaDAO storageAreaDAO) {
        this.storageAreaDAO = storageAreaDAO;
    }

    public Page<StorageAreaModel> findPaginated(int page, int size) {
        if (size > 20 || size < 1) {
            size = 15;
        }

        Pageable pageable = PageRequest.of(page, size);
        return this.storageAreaDAO.findPaginated(pageable);
    }

    public Optional<StorageAreaModel> findById(String id) {
        if (id == null || id.isEmpty()) {
            return Optional.empty();
        }

        try {
            Long parsedId = Long.parseLong(id);
            return Optional.ofNullable(this.storageAreaDAO.findById(parsedId));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    public StorageAreaModel findByIdOrThrow(String id) {
        return findById(id).orElseThrow(() -> new IllegalArgumentException(NOT_FOUND_MESSAGE));
    }

    public void save(StorageAreaModel storageAreaModel) {
        if (storageAreaModel.getAreaId() == null) {
            this.storageAreaDAO.create(storageAreaModel);
        } else {
            this.storageAreaDAO.update(storageAreaModel);
        }
    }

    public void deleteById(String id) {
        StorageAreaModel storageAreaModel = findByIdOrThrow(id);
        this.storageAreaDAO.delete(storageAreaModel);
    }
}
