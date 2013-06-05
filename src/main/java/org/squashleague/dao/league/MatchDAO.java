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
import org.squashleague.domain.league.PlayerStatus;

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
    public List<Match> findAllByUser(User user) {
        try {
            return entityManager.createQuery("from Match as match where " +
                    "(match.playerOne.status = " + PlayerStatus.ACTIVE.ordinal() + " and match.playerTwo.status = " + PlayerStatus.ACTIVE.ordinal() + ") and " +
                    "(match.playerOne.user.id = " + user.getId() + " or match.playerTwo.user.id = " + user.getId() + ")", Match.class).getResultList();
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }

    @Override
    public List<Match> findAll() {
        return entityManager.createQuery("from Match as match where match.playerOne.status = " + PlayerStatus.ACTIVE.ordinal() + " and match.playerTwo.status = " + PlayerStatus.ACTIVE.ordinal(), Match.class).getResultList();
    }

    @Override
    public Match findById(Long id) {
        return super.findById(id);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN') or principal.id == #match.playerOne.user.id or principal.id == #match.playerTwo.user.id")
    public void update(Match match) {
        super.update(match);
    }

}
