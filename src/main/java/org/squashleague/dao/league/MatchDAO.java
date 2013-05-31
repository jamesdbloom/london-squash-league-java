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
    @PreAuthorize("hasRole('ROLE_ADMIN') or principal.id == #player.user.id or principal.id")
    public List<Match> findAllByPlayer(Player player) {
        try {
            return entityManager.createQuery("from Match as match where " +
                    "(match.playerOne.status = " + PlayerStatus.ACTIVE.ordinal() + " and match.playerTwo.status = " + PlayerStatus.ACTIVE.ordinal() + ") and " +
                    "(match.playerOne.id = " + player.getId() + " or match.playerTwo.id = " + player.getId() + ")", Match.class).getResultList();
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }

    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN') or principal.id == #userId or principal.id")
    public List<Match> findAllByUserId(Long userId) {
        try {
            return entityManager.createQuery("from Match as match where " +
                    "(match.playerOne.status = " + PlayerStatus.ACTIVE.ordinal() + " and match.playerTwo.status = " + PlayerStatus.ACTIVE.ordinal() + ") and " +
                    "(match.playerOne.user.id = " + userId + " or match.playerTwo.user.id = " + userId + ")", Match.class).getResultList();
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }

    @Override
    public List<Match> findAll() {
        return entityManager.createQuery("from Match as match where match.playerOne.status = " + PlayerStatus.ACTIVE.ordinal() + " and match.playerTwo.status = " + PlayerStatus.ACTIVE.ordinal(), Match.class).getResultList();
    }

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN') or principal.id == #match.playerOne.user.id or principal.id == #match.playerTwo.user.id")
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
