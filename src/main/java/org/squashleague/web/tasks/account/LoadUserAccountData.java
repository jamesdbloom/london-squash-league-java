package org.squashleague.web.tasks.account;

import com.google.common.collect.Maps;
import org.squashleague.dao.league.MatchDAO;
import org.squashleague.dao.league.PlayerDAO;
import org.squashleague.domain.ModelObject;
import org.squashleague.domain.account.User;
import org.squashleague.domain.league.Match;
import org.squashleague.domain.league.Player;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * @author jamesdbloom
 */
public class LoadUserAccountData implements Callable<User> {

    private final MatchDAO matchDAO;
    private final PlayerDAO playerDAO;
    private final User user;

    public LoadUserAccountData(MatchDAO matchDAO, PlayerDAO playerDAO, User user) {
        this.matchDAO = matchDAO;
        this.playerDAO = playerDAO;
        this.user = user;
    }

    @Override
    public User call() throws Exception {
        List<Match> matches = matchDAO.findAllByUser(user);
        List<Player> players = playerDAO.findAllByUser(user);
        Map<Long, Player> playersById = Maps.uniqueIndex(players, ModelObject.TO_MAP);
        for (Match match : matches) {
            if (playersById.containsKey(match.getPlayerOne().getId())) {
                playersById.get(match.getPlayerOne().getId()).addMatch(match);
            }
            if (playersById.containsKey(match.getPlayerTwo().getId())) {
                playersById.get(match.getPlayerTwo().getId()).addMatch(match);
            }
        }

        user.setPlayers(players);
        return user;
    }
}
