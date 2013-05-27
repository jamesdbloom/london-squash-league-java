package org.squashleague.dao.league;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.squashleague.dao.AbstractJpaDAO;
import org.squashleague.domain.account.User;
import org.squashleague.domain.league.Match;
import org.squashleague.domain.league.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jamesdbloom
 */
@Component
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class MatchDAO extends AbstractJpaDAO<Match> {

    public MatchDAO() {
        super(Match.class);
    }

    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN') or principal.id == #player.user.id or principal.id")
    public List<Match> findByPlayer(Player player) {
        try {
            return entityManager.createQuery("from Match as match where match.playerOne.id = " + player.getId() + " or match.playerTwo.id = " + player.getId(), Match.class).getResultList();
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN') or principal.id == #match.playerOne.user.id or principal.id == #match.playerTwo.user.id")
    public void update(Match match) {
        entityManager.merge(match);
    }

}
