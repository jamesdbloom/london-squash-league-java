package org.squashleague.dao.league;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.squashleague.dao.AbstractJpaDAO;
import org.squashleague.domain.league.Round;

/**
 * @author jamesdbloom
 */
@Component
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class RoundDAO extends AbstractJpaDAO<Round> {

    public RoundDAO() {
        super(Round.class);
    }

}
