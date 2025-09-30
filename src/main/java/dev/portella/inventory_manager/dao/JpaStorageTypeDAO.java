package dev.portella.inventory_manager.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import dev.portella.inventory_manager.model.StorageTypeModel;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;

@Repository
public class JpaStorageTypeDAO implements IDAO<StorageTypeModel> {

    @PersistenceContext
    private EntityManager entityManager;

    private CriteriaBuilder cb;
    private CriteriaQuery<StorageTypeModel> cq;
    private Root<StorageTypeModel> root;

    private void initializeCriteriaQuery() {
        cb = entityManager.getCriteriaBuilder();
        cq = cb.createQuery(StorageTypeModel.class);
        root = cq.from(StorageTypeModel.class);
    }

    @Override
    public Page<StorageTypeModel> findPaginated(Pageable pageable) {
        initializeCriteriaQuery();
        cq.select(root);
        cq.orderBy(cb.asc(root.get("id")));

        TypedQuery<StorageTypeModel> query = entityManager.createQuery(cq);
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<StorageTypeModel> countRoot = countQuery.from(StorageTypeModel.class);
        countQuery.select(cb.count(countRoot));
        long totalRecords = entityManager.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(query.getResultList(), pageable, totalRecords);
    }

    @Override
    public StorageTypeModel findById(Long id) {
        return entityManager.find(StorageTypeModel.class, id);
    }

    @Override
    public List<StorageTypeModel> findByField(String field, Object value) {
        initializeCriteriaQuery();

        Predicate predicate = cb.equal(root.get(field), value);
        cq.where(predicate);

        TypedQuery<StorageTypeModel> query = entityManager.createQuery(cq);
        return query.getResultList();
    }

    @Override
    @Transactional
    public void create(StorageTypeModel storageType) {
        entityManager.persist(storageType);
    }

    @Override
    @Transactional
    public void update(StorageTypeModel storageType) {
        entityManager.merge(storageType);
    }

    @Override
    @Transactional
    public void delete(StorageTypeModel storageType) {
        entityManager.remove(storageType);
    }
}