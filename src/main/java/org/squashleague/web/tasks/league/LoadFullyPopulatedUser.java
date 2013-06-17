package org.squashleague.web.tasks.league;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import org.squashleague.dao.league.PlayerDAO;
import org.squashleague.domain.account.User;
import org.squashleague.domain.league.Division;
import org.squashleague.domain.league.Match;
import org.squashleague.domain.league.Player;

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
        Map<Long, Division> divisionsById = new HashMap<>();
        for (Division division : Lists.transform(matches, new Function<Match, Division>() {
            public Division apply(Match match) {
                return match.getDivision().addMatches(match);
            }
        })) {
            divisionsById.put(division.getId(), division);
        }
        List<Division> allDivisions = new ArrayList<>(divisionsById.values());
        Collections.sort(allDivisions);
        Collection<Division> usersPlayerDivisions = Collections2.filter(allDivisions, new Predicate<Division>() {
            public boolean apply(Division division) {
                for (Player userPlayer : user.getPlayers()) {
                    for (Player roundPlayer : division.getPlayers()) {
                        if (userPlayer.getId().equals(roundPlayer.getId())) {
                            return true;
                        }
                    }
                }
                return false;
            }
        });
        Collection<Division> usersLeagueDivisions = Collections2.filter(allDivisions, new Predicate<Division>() {
            public boolean apply(Division division) {
                for (Player player : user.getPlayers()) {
                    if (player.getLeague().getId().equals(division.getRound().getLeague().getId())) {
                        return true;
                    }
                }
                return false;
            }
        });
        user.setDivisions((showAllDivisions ? usersLeagueDivisions : usersPlayerDivisions));
        return user;
    }
}
