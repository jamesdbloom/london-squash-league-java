package org.squashleague.dao.league;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.squashleague.dao.AbstractJpaDAO;
import org.squashleague.domain.account.User;
import org.squashleague.domain.league.Division;
import org.squashleague.domain.league.League;

import java.util.List;

/**
 * @author jamesdbloom
 */
@Component
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class DivisionDAO extends AbstractJpaDAO<Division> {

    public DivisionDAO() {
        super(Division.class);
    }

    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN') or principal.id == #user.id")
    public List<Division> findAllForUser(User user) {
        return entityManager.createQuery(
                "from Division division " +
                        "where division.id IN (" +
                        "    select currentDivision.id from Player player " +
                        "    where player.user.id = " + user.getId() +
                        ")", Division.class).getResultList();
    }

}
