package org.squashleague.web.tasks.league;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import org.squashleague.dao.league.PlayerDAO;
import org.squashleague.domain.account.User;
import org.squashleague.domain.league.Match;
import org.squashleague.domain.league.Player;
import org.squashleague.domain.league.Round;

import java.util.*;
import java.util.concurrent.Callable;

/**
 * @author jamesdbloom
 */
public class LoadFullyPopulatedUser implements Callable<User> {

    private final PlayerDAO playerDAO;
    private final User user;
    private final boolean showAllDivisions;
    private final List<Match> matches;

    public LoadFullyPopulatedUser(PlayerDAO playerDAO, List<Match> matches, User user, boolean showAllDivisions) {
        this.playerDAO = playerDAO;
        this.user = user;
        this.showAllDivisions = showAllDivisions;
        this.matches = matches;
    }

    @Override
    public User call() throws Exception {
        user.setPlayers(playerDAO.findAllActiveByUser(user));
        Map<Long, Round> roundsById = new HashMap<>();
        for (Round round : Lists.transform(matches, new Function<Match, Round>() {
            public Round apply(Match match) {
                return match.getRound().addMatch(match);
            }
        })) {
            roundsById.put(round.getId(), round);
        }
        List<Round> allRounds = new ArrayList<>(roundsById.values());
        Collections.sort(allRounds);
        Collection<Round> usersPlayerRounds = Collections2.filter(allRounds, new Predicate<Round>() {
            public boolean apply(Round round) {
                for (Player userPlayer : user.getPlayers()) {
                    for (Player roundPlayer : round.getPlayers()) {
                        if (userPlayer.getId().equals(roundPlayer.getId())) {
                            return true;
                        }
                    }
                }
                return false;
            }
        });
        Collection<Round> usersLeagueRounds = Collections2.filter(allRounds, new Predicate<Round>() {
            public boolean apply(Round round) {
                for (Player player : user.getPlayers()) {
                    if (player.getLeague().getId().equals(round.getDivision().getLeague().getId())) {
                        return true;
                    }
                }
                return false;
            }
        });
        user.setRounds((showAllDivisions ? usersLeagueRounds : usersPlayerRounds));
        return user;
    }
}
