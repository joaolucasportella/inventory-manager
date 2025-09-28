package dev.portella.inventory_manager.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import dev.portella.inventory_manager.model.ProductModel;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;

@Repository
public class JpaProductDAO implements ProductDAO {

    @PersistenceContext
    private EntityManager entityManager;

    private CriteriaBuilder cb;
    private CriteriaQuery<ProductModel> cq;
    private Root<ProductModel> root;

    private void initializeCriteriaQuery() {
        cb = entityManager.getCriteriaBuilder();
        cq = cb.createQuery(ProductModel.class);
        root = cq.from(ProductModel.class);
    }

    @Override
    public Page<ProductModel> findPaginated(Pageable pageable) {
        initializeCriteriaQuery();
        cq.select(root);
        cq.orderBy(cb.asc(root.get("id")));

        TypedQuery<ProductModel> query = entityManager.createQuery(cq);
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<ProductModel> countRoot = countQuery.from(ProductModel.class);
        countQuery.select(cb.count(countRoot));
        long totalRecords = entityManager.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(query.getResultList(), pageable, totalRecords);
    }

    @Override
    public ProductModel findById(Long id) {
        return entityManager.find(ProductModel.class, id);
    }

    @Override
    public List<ProductModel> findByField(String field, Object value) {
        initializeCriteriaQuery();

        Predicate predicate = cb.equal(root.get(field), value);
        cq.where(predicate);

        TypedQuery<ProductModel> query = entityManager.createQuery(cq);
        return query.getResultList();
    }

    @Override
    @Transactional
    public void create(ProductModel customer) {
        entityManager.persist(customer);
    }

    @Override
    @Transactional
    public void update(ProductModel customer) {
        entityManager.merge(customer);
    }

    @Override
    @Transactional
    public void delete(ProductModel customer) {
        entityManager.remove(customer);
    }
}