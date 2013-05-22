package org.squashleague.dao.league;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.squashleague.domain.ModelObject;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * @author jamesdbloom
 */
public abstract class AbstractJpaDAO<T extends ModelObject> {

    private final Class<T> clazz;
    @PersistenceContext
    protected EntityManager entityManager;

    protected AbstractJpaDAO(Class<T> clazz) {
        this.clazz = clazz;
    }

    public T findById(Long id) {
        return entityManager.find(clazz, id);
    }

    public List<T> findAll() {
        return entityManager.createQuery("from " + clazz.getName()).getResultList();
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
        entityManager.merge(entity);
    }

    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void delete(Long id) {
        entityManager.remove(findById(id));
    }

    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void delete(T entity) {
        entityManager.remove(findById(entity.getId()));
    }
}
