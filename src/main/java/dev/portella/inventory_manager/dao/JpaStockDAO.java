package dev.portella.inventory_manager.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import dev.portella.inventory_manager.model.StockModel;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;

@Repository
public class JpaStockDAO implements IDAO<StockModel> {

    @PersistenceContext
    private EntityManager entityManager;

    private CriteriaBuilder cb;
    private CriteriaQuery<StockModel> cq;
    private Root<StockModel> root;

    private void initializeCriteriaQuery() {
        cb = entityManager.getCriteriaBuilder();
        cq = cb.createQuery(StockModel.class);
        root = cq.from(StockModel.class);
    }

    @Override
    public Page<StockModel> findPaginated(Pageable pageable) {
        initializeCriteriaQuery();
        cq.select(root);
        cq.orderBy(cb.asc(root.get("id")));

        TypedQuery<StockModel> query = entityManager.createQuery(cq);
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<StockModel> countRoot = countQuery.from(StockModel.class);
        countQuery.select(cb.count(countRoot));
        long totalRecords = entityManager.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(query.getResultList(), pageable, totalRecords);
    }

    @Override
    public StockModel findById(Long id) {
        return entityManager.find(StockModel.class, id);
    }

    @Override
    public List<StockModel> findByField(String field, Object value) {
        initializeCriteriaQuery();

        Predicate predicate = cb.equal(root.get(field), value);
        cq.where(predicate);

        TypedQuery<StockModel> query = entityManager.createQuery(cq);
        return query.getResultList();
    }

    @Override
    @Transactional
    public void create(StockModel stock) {
        entityManager.persist(stock);
    }

    @Override
    @Transactional
    public void update(StockModel stock) {
        entityManager.merge(stock);
    }

    @Override
    @Transactional
    public void delete(StockModel stock) {
        entityManager.remove(stock);
    }
}