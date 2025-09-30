package dev.portella.inventory_manager.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import dev.portella.inventory_manager.model.StockMovementModel;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;

@Repository
public class JpaStockMovementDAO implements IDAO<StockMovementModel> {

    @PersistenceContext
    private EntityManager entityManager;

    private CriteriaBuilder cb;
    private CriteriaQuery<StockMovementModel> cq;
    private Root<StockMovementModel> root;

    private void initializeCriteriaQuery() {
        cb = entityManager.getCriteriaBuilder();
        cq = cb.createQuery(StockMovementModel.class);
        root = cq.from(StockMovementModel.class);
    }

    @Override
    public Page<StockMovementModel> findPaginated(Pageable pageable) {
        initializeCriteriaQuery();
        cq.select(root);
        cq.orderBy(cb.asc(root.get("id")));

        TypedQuery<StockMovementModel> query = entityManager.createQuery(cq);
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<StockMovementModel> countRoot = countQuery.from(StockMovementModel.class);
        countQuery.select(cb.count(countRoot));
        long totalRecords = entityManager.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(query.getResultList(), pageable, totalRecords);
    }

    @Override
    public StockMovementModel findById(Long id) {
        return entityManager.find(StockMovementModel.class, id);
    }

    @Override
    public List<StockMovementModel> findByField(String field, Object value) {
        initializeCriteriaQuery();

        Predicate predicate = cb.equal(root.get(field), value);
        cq.where(predicate);

        TypedQuery<StockMovementModel> query = entityManager.createQuery(cq);
        return query.getResultList();
    }

    @Override
    @Transactional
    public void create(StockMovementModel stockMovement) {
        entityManager.persist(stockMovement);
    }

    @Override
    @Transactional
    public void update(StockMovementModel stockMovement) {
        entityManager.merge(stockMovement);
    }

    @Override
    @Transactional
    public void delete(StockMovementModel stockMovement) {
        entityManager.remove(stockMovement);
    }
}