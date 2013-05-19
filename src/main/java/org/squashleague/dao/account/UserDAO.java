package org.squashleague.dao.account;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.squashleague.dao.league.AbstractJpaDAO;
import org.squashleague.domain.account.Role;
import org.squashleague.domain.account.User;

import javax.persistence.NoResultException;

/**
 * @author jamesdbloom
 */
@Component
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserDAO extends AbstractJpaDAO<User> {

    public UserDAO() {
        super(User.class);
    }

    @Transactional
    public void save(User user) {
        for (Role role : user.getRoles()) {
            if (role.getId() == null) {
                entityManager.persist(role);
            }
        }
        entityManager.persist(user);
        entityManager.flush();
    }

    public User findByEmail(String email) {
        try {
            return entityManager.createQuery("from User as user where user.email = '" + email + "'", User.class).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
