package dev.portella.inventory_manager.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import dev.portella.inventory_manager.dao.JpaStorageTypeDAO;
import dev.portella.inventory_manager.model.StorageTypeModel;

@Service
public class StorageTypeService {

    private final JpaStorageTypeDAO storageTypeDAO;
    private static final String NOT_FOUND_MESSAGE = "Tipo de armazenamento não encontrado.";

    public StorageTypeService(JpaStorageTypeDAO storageTypeDAO) {
        this.storageTypeDAO = storageTypeDAO;
    }

    public Page<StorageTypeModel> findPaginated(int page, int size) {
        if (size > 20 || size < 1) {
            size = 15;
        }

        Pageable pageable = PageRequest.of(page, size);
        return this.storageTypeDAO.findPaginated(pageable);
    }

    public Optional<StorageTypeModel> findById(String id) {
        if (id == null || id.isEmpty()) {
            return Optional.empty();
        }

        try {
            Long parsedId = Long.parseLong(id);
            return Optional.ofNullable(this.storageTypeDAO.findById(parsedId));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    public StorageTypeModel findByIdOrThrow(String id) {
        return findById(id).orElseThrow(() -> new IllegalArgumentException(NOT_FOUND_MESSAGE));
    }

    public void save(StorageTypeModel storageTypeModel) {
        if (storageTypeModel.getStorageTypeId() == null) {
            this.storageTypeDAO.create(storageTypeModel);
        } else {
            this.storageTypeDAO.update(storageTypeModel);
        }
    }

    public void deleteById(String id) {
        StorageTypeModel storageTypeModel = findByIdOrThrow(id);
        this.storageTypeDAO.delete(storageTypeModel);
    }
}
