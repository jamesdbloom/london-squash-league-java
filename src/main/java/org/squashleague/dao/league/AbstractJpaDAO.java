package org.squashleague.dao.league;

import org.springframework.transaction.annotation.Transactional;
import org.squashleague.domain.ModelObject;

import javax.persistence.EntityManager;
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

    public T findOne(Long id) {
        return entityManager.find(clazz, id);
    }

    public List<T> findAll() {
        return entityManager.createQuery("from " + clazz.getName()).getResultList();
    }

    @Transactional
    public void save(T entity) {
        entityManager.persist(entity);
        entityManager.flush();
    }

    @Transactional
    public void update(T entity) {
        entityManager.merge(entity);
    }

    @Transactional
    public void delete(Long id) {
        entityManager.remove(findOne(id));
    }

    @Transactional
    public void delete(T entity) {
        entityManager.remove(findOne(entity.getId()));
    }
}
