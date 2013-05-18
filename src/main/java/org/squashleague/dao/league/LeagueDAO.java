package org.squashleague.dao.league;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.squashleague.domain.league.League;

/**
 * @author squashleague
 */
@Component
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class LeagueDAO extends AbstractJpaDAO<League> {

    public LeagueDAO() {
        super(League.class);
    }

}
