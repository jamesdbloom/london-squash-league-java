package org.squashleague.dao.account;

import com.google.common.annotations.VisibleForTesting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.squashleague.domain.account.Role;
import org.squashleague.domain.account.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jamesdbloom
 */
@Component
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserDAO {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());
    @PersistenceContext
    protected EntityManager entityManager;

    public User findByEmail(String email) {
        List<User> resultList = entityManager.createQuery("from User as user where user.email = '" + email + "'", User.class).getResultList();
        if (resultList.size() > 0) {
            return resultList.get(0);
        } else {
            return null;
        }
    }

    @VisibleForTesting
    protected Role findOrCreateRole(Role role) {
        List<Role> resultList = entityManager.createQuery("from Role as role where role.name = '" + role.getName() + "'", Role.class).getResultList();
        if (resultList.size() > 0) {
            return resultList.get(0);
        }
        entityManager.persist(role);
        return role;
    }

    @Transactional
    public void register(User user) {
        List<Role> roles = new ArrayList<>();
        for (Role role : user.getRoles()) {
            roles.add(findOrCreateRole(role));
        }
        user.withRoles(roles);
        entityManager.persist(user);
        entityManager.flush();
    }

    @Transactional
    public void updatePassword(User user) {
        entityManager.createQuery("update User as user set user.password = '" + user.getPassword() + "', user.oneTimeToken = '' where user.email = '" + user.getEmail() + "'").executeUpdate();
    }

    @Transactional
    public void updateOneTimeToken(User user) {
        entityManager.createQuery("update User as user set user.oneTimeToken = '" + user.getOneTimeToken() + "' where user.email = '" + user.getEmail() + "'").executeUpdate();
    }

    @Transactional
    public void incrementLoginFailures(User user) {
        entityManager.createQuery("update User as user set user.loginFailures = '" + (user.getLoginFailures() + 1) + "' where user.email = '" + user.getEmail() + "'").executeUpdate();
    }

    @Transactional
    public void resetLoginFailures(User user) {
        entityManager.createQuery("update User as user set user.loginFailures = 0 where user.email = '" + user.getEmail() + "'").executeUpdate();
    }

    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN') or principal.id == #id")
    public User findById(Long id) {
        try {
            return entityManager.find(User.class, id);
        } catch (Exception e) {
            logger.error(String.format("Exception while finding user with id %s", id), e);
        }
        return null;
    }

    public List<User> findAll() {
        return entityManager.createQuery("from " + User.class.getName(), User.class).getResultList();
    }

    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN') or principal.id == #user.id")
    public void save(User user) {
        entityManager.persist(user);
        entityManager.flush();
    }

    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN') or principal.id == #user.id")
    public void update(User user) {
        if (user != null && user.getId() != null) {
            entityManager.merge(findById(user.getId()).merge(user));
        }
    }

    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN') or principal.id == #id")
    public void delete(Long id) {
        entityManager.remove(findById(id));
    }

    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN') or principal.id == #user.id")
    public void delete(User user) {
        entityManager.remove(findById((user == null ? null : user.getId())));
    }
}
