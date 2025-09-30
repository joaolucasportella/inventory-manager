package dev.portella.inventory_manager.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import dev.portella.inventory_manager.model.StorageAreaModel;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;

@Repository
public class JpaStorageAreaDAO implements IDAO<StorageAreaModel> {

    @PersistenceContext
    private EntityManager entityManager;

    private CriteriaBuilder cb;
    private CriteriaQuery<StorageAreaModel> cq;
    private Root<StorageAreaModel> root;

    private void initializeCriteriaQuery() {
        cb = entityManager.getCriteriaBuilder();
        cq = cb.createQuery(StorageAreaModel.class);
        root = cq.from(StorageAreaModel.class);
    }

    @Override
    public Page<StorageAreaModel> findPaginated(Pageable pageable) {
        initializeCriteriaQuery();
        cq.select(root);
        cq.orderBy(cb.asc(root.get("id")));

        TypedQuery<StorageAreaModel> query = entityManager.createQuery(cq);
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<StorageAreaModel> countRoot = countQuery.from(StorageAreaModel.class);
        countQuery.select(cb.count(countRoot));
        long totalRecords = entityManager.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(query.getResultList(), pageable, totalRecords);
    }

    @Override
    public StorageAreaModel findById(Long id) {
        return entityManager.find(StorageAreaModel.class, id);
    }

    @Override
    public List<StorageAreaModel> findByField(String field, Object value) {
        initializeCriteriaQuery();

        Predicate predicate = cb.equal(root.get(field), value);
        cq.where(predicate);

        TypedQuery<StorageAreaModel> query = entityManager.createQuery(cq);
        return query.getResultList();
    }

    @Override
    @Transactional
    public void create(StorageAreaModel storageArea) {
        entityManager.persist(storageArea);
    }

    @Override
    @Transactional
    public void update(StorageAreaModel storageArea) {
        entityManager.merge(storageArea);
    }

    @Override
    @Transactional
    public void delete(StorageAreaModel storageArea) {
        entityManager.remove(storageArea);
    }
}