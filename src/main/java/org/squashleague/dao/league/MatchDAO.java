package org.squashleague.dao.league;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.squashleague.dao.AbstractJpaDAO;
import org.squashleague.domain.league.Match;

/**
 * @author jamesdbloom
 */
@Component
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class MatchDAO extends AbstractJpaDAO<Match> {

    public MatchDAO() {
        super(Match.class);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN') or principal.id == #match.playerOne.user.id or principal.id == #match.playerTwo.user.id")
    public void update(Match match) {
        entityManager.merge(match);
    }

}
