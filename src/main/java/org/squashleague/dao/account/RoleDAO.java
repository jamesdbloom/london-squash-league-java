package org.squashleague.dao.account;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;
import org.squashleague.dao.AbstractJpaDAO;
import org.squashleague.domain.account.Role;

import javax.persistence.NoResultException;
import java.util.List;

/**
 * @author jamesdbloom
 */
@Component
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class RoleDAO extends AbstractJpaDAO<Role> {

    public RoleDAO() {
        super(Role.class);
    }

    public Role findByName(String name) {
        try {
            List<Role> resultList = entityManager.createQuery("from Role as role where role.name = '" + name + "'", Role.class).getResultList();
            if (resultList.size() > 0) {
                return resultList.get(0);
            } else {
                return null;
            }
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

}
