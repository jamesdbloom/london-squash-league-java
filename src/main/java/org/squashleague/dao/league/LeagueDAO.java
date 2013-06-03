package org.squashleague.dao.league;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.squashleague.dao.AbstractJpaDAO;
import org.squashleague.domain.account.User;
import org.squashleague.domain.league.League;

import java.util.ArrayList;
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
    public List<League> findAllUnregisteredLeagues(User user) {
        try {
            return entityManager.createQuery("from League league where league.id NOT IN (select player.league.id from Player player where player.user.id = " + user.getId() + ")", League.class).getResultList();
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }

}
