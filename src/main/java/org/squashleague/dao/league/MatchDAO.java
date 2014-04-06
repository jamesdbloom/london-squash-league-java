package org.squashleague.dao.league;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Joiner;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.squashleague.dao.AbstractJpaDAO;
import org.squashleague.domain.league.Match;
import org.squashleague.domain.league.Player;
import org.squashleague.domain.league.PlayerStatus;
import org.squashleague.domain.league.Round;

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
    public List<Match> findAllByPlayer(Player player, Round round, int roundsBack) {
        List<Long> roundIds = getPreviousRoundIds(round, roundsBack);
        return entityManager.createQuery("from Match as match where " +
                "(match.playerOne.id = " + player.getId() + " or match.playerTwo.id = " + player.getId() + ")" +
                (round != null ? " and match.division.round.id in (" + Joiner.on(",").join(roundIds) + ")" : ""), Match.class).getResultList();
    }

    @Transactional
    public List<Match> findAllInRound(Round round, int roundsBack) {
        List<Long> roundIds = getPreviousRoundIds(round, roundsBack);
        return entityManager.createQuery("from Match as match where match.division.round.id in (" + Joiner.on(",").join(roundIds) + ")", Match.class).getResultList();
    }

    @VisibleForTesting
    protected List<Long> getPreviousRoundIds(Round round, int roundsBack) {
        List<Long> roundIds = new ArrayList<>();
        roundIds.add(round.getId());
        Round previousRound = round.getPreviousRound();
        for (int backCounter = 0; backCounter < roundsBack; backCounter++) {
            if (previousRound != null) {
                roundIds.add(previousRound.getId());
                previousRound = previousRound.getPreviousRound();
            } else {
                break;
            }
        }
        return roundIds;
    }

    @Override
    public List<Match> findAll() {
        // return entityManager.createQuery("from Match as match where match.playerOne.status = " + PlayerStatus.ACTIVE.ordinal() + " and match.playerTwo.status = " + PlayerStatus.ACTIVE.ordinal() + " order by match.playerOne.user.name", Match.class).getResultList();
        return entityManager.createQuery("from Match as match order by match.division.round.startDate desc, match.playerOne.user.name", Match.class).getResultList();
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
