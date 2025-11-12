package dev.portella.inventory_manager.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import dev.portella.inventory_manager.model.LogModel;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;

@Repository
public class JpaLogDAO implements ILogDAO {

    @PersistenceContext
    private EntityManager entityManager;

    private CriteriaBuilder cb;
    private CriteriaQuery<LogModel> cq;
    private Root<LogModel> root;

    private void initializeCriteriaQuery() {
        cb = entityManager.getCriteriaBuilder();
        cq = cb.createQuery(LogModel.class);
        root = cq.from(LogModel.class);
    }

    @Override
    public Page<LogModel> findPaginated(Pageable pageable) {
        initializeCriteriaQuery();
        cq.select(root);
        cq.orderBy(cb.asc(root.get("id")));

        TypedQuery<LogModel> query = entityManager.createQuery(cq);
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<LogModel> countRoot = countQuery.from(LogModel.class);
        countQuery.select(cb.count(countRoot));
        long totalRecords = entityManager.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(query.getResultList(), pageable, totalRecords);
    }

    @Override
    public LogModel findById(Long id) {
        return entityManager.find(LogModel.class, id);
    }

    @Override
    @Transactional
    public void create(LogModel logModel) {
        entityManager.persist(logModel);
    }
}