package org.squashleague.dao.league;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.squashleague.dao.AbstractJpaDAO;
import org.squashleague.domain.account.User;
import org.squashleague.domain.league.Round;

import java.util.List;

/**
 * @author jamesdbloom
 */
@Component
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class RoundDAO extends AbstractJpaDAO<Round> {

    public RoundDAO() {
        super(Round.class);
    }

    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN') or principal.id == #user.id")
    public List<Round> findAllForUser(User user) {
        return entityManager.createQuery(
                "from Round round " +
                        "where round.division.id IN (" +
                        "    select division.id from Division division " +
                        "    where division.league.id IN (" +
                        "        select league.id from League league " +
                        "        where league.id IN (" +
                        "            select player.league.id from Player player " +
                        "            where player.user.id = " + user.getId() +
                        "        )" +
                        "    )" +
                        ")", Round.class).getResultList();
    }

}
