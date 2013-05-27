package org.squashleague.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.squashleague.domain.ModelObject;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * @author jamesdbloom
 */
public abstract class AbstractJpaDAO<T extends ModelObject<T>> {
    private final Class<T> clazz;
    protected Logger logger = LoggerFactory.getLogger(this.getClass());
    @PersistenceContext
    protected EntityManager entityManager;

    protected AbstractJpaDAO(Class<T> clazz) {
        this.clazz = clazz;
    }

    public T findById(Long id) {
        try {
            return entityManager.find(clazz, id);
        } catch (Exception e) {
            logger.error(String.format("Exception while finding " + clazz.getSimpleName() + " with id %s", id), e);
        }
        return null;
    }

    public List<T> findAll() {
        return entityManager.createQuery("from " + clazz.getSimpleName(), clazz).getResultList();
    }

    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void save(T entity) {
        entityManager.persist(entity);
        entityManager.flush();
    }

    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void update(T entity) {
        if (entity != null && entity.getId() != null) {
            entityManager.merge(findById(entity.getId()).merge(entity));
        }
    }

    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void delete(Long id) {
        entityManager.remove(findById(id));
    }

    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void delete(T entity) {
        entityManager.remove(findById((entity == null ? null : entity.getId())));
    }
}
