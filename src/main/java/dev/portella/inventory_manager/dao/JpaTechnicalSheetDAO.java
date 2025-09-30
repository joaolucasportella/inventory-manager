package dev.portella.inventory_manager.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import dev.portella.inventory_manager.model.TechnicalSheetModel;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;

@Repository
public class JpaTechnicalSheetDAO implements IDAO<TechnicalSheetModel> {

    @PersistenceContext
    private EntityManager entityManager;

    private CriteriaBuilder cb;
    private CriteriaQuery<TechnicalSheetModel> cq;
    private Root<TechnicalSheetModel> root;

    private void initializeCriteriaQuery() {
        cb = entityManager.getCriteriaBuilder();
        cq = cb.createQuery(TechnicalSheetModel.class);
        root = cq.from(TechnicalSheetModel.class);
    }

    @Override
    public Page<TechnicalSheetModel> findPaginated(Pageable pageable) {
        initializeCriteriaQuery();
        cq.select(root);
        cq.orderBy(cb.asc(root.get("id")));

        TypedQuery<TechnicalSheetModel> query = entityManager.createQuery(cq);
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<TechnicalSheetModel> countRoot = countQuery.from(TechnicalSheetModel.class);
        countQuery.select(cb.count(countRoot));
        long totalRecords = entityManager.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(query.getResultList(), pageable, totalRecords);
    }

    @Override
    public TechnicalSheetModel findById(Long id) {
        return entityManager.find(TechnicalSheetModel.class, id);
    }

    @Override
    public List<TechnicalSheetModel> findByField(String field, Object value) {
        initializeCriteriaQuery();

        Predicate predicate = cb.equal(root.get(field), value);
        cq.where(predicate);

        TypedQuery<TechnicalSheetModel> query = entityManager.createQuery(cq);
        return query.getResultList();
    }

    @Override
    @Transactional
    public void create(TechnicalSheetModel product) {
        entityManager.persist(product);
    }

    @Override
    @Transactional
    public void update(TechnicalSheetModel product) {
        entityManager.merge(product);
    }

    @Override
    @Transactional
    public void delete(TechnicalSheetModel product) {
        entityManager.remove(product);
    }
}