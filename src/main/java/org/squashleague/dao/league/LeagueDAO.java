package org.squashleague.dao.league;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.squashleague.dao.AbstractJpaDAO;
import org.squashleague.domain.account.User;
import org.squashleague.domain.league.League;

import java.util.List;

/**
 * @author jamesdbloom
 */
@Component
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class LeagueDAO extends AbstractJpaDAO<League> {

    public LeagueDAO() {
        super(League.class);
    }

    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN') or principal.id == #userId or principal.id")
    public List<League> findAllUnregisteredLeagues(Long userId) {
        return entityManager.createQuery("from League league where league.id NOT IN (select player.league.id from Player player where player.user.id = " + userId + ")", League.class).getResultList();
    }

}
