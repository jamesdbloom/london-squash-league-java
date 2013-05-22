package org.squashleague.dao.account;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.squashleague.domain.account.Role;
import org.squashleague.domain.account.User;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jamesdbloom
 */
@Component
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserDAO {

    @PersistenceContext
    protected EntityManager entityManager;

    @PreAuthorize("hasRole('ROLE_ADMIN') or principal.id == #id")
    public User findById(Long id) {
        return entityManager.find(User.class, id);
    }

    public List<User> findAll() {
        return entityManager.createQuery("from " + User.class.getName(), User.class).getResultList();
    }

    public User findByEmail(String email) {
        try {
            List<User> resultList = entityManager.createQuery("from User as user where user.email = '" + email + "'", User.class).getResultList();
            if (resultList.size() > 0) {
                return resultList.get(0);
            } else {
                return null;
            }
        } catch (NoResultException e) {
            return null;
        }
    }

    private Role findOrCreateRole(Role role) {
        try {
            List<Role> resultList = entityManager.createQuery("from Role as role where role.name = '" + role.getName() + "'", Role.class).getResultList();
            if (resultList.size() > 0) {
                return resultList.get(0);
            }
        } catch (NoResultException e) {
            // no existing role found
        }
        entityManager.persist(role);
        return role;
    }

    @Transactional
    public void register(User user) {
        List<Role> roles = new ArrayList<>();
        for(Role role : user.getRoles()) {
            roles.add(findOrCreateRole(role));
        }
        user.setRoles(roles);
        entityManager.persist(user);
        entityManager.flush();
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
        entityManager.merge(user);
    }

    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN') or principal.id == #id")
    public void delete(Long id) {
        entityManager.remove(findById(id));
    }

    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN') or principal.id == #user.id")
    public void delete(User user) {
        entityManager.remove(findById(user.getId()));
    }
}
