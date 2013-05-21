package org.squashleague.dao.league;

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

    public T findByField(String value, String fieldName) {
        return findByField(value, fieldName, clazz);
    }

    protected <O> O findByField(String value, String fieldName, Class<O> clazz) {
        try {
            List<O> resultList = entityManager.createQuery("from " + clazz.getName() + " as obj where obj." + fieldName + " = '" + value + "'", clazz).getResultList();
            if (resultList.size() > 0) {
                return resultList.get(0);
            } else {
                return null;
            }
        } catch (NoResultException e) {
            return null;
        }
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
        entityManager.remove(findById(id));
    }

    @Transactional
    public void delete(T entity) {
        entityManager.remove(findById(entity.getId()));
    }
}
