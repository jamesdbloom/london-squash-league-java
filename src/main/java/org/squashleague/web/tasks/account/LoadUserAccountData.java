package org.squashleague.web.tasks.account;

import org.squashleague.dao.league.MatchDAO;
import org.squashleague.dao.league.PlayerDAO;
import org.squashleague.domain.account.User;
import org.squashleague.domain.league.Division;
import org.squashleague.domain.league.Match;
import org.squashleague.domain.league.Player;
import org.squashleague.domain.league.Round;

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
        List<Player> players = playerDAO.findAllByUser(user);
        for (Player player : players) {
            Division currentDivision = player.getCurrentDivision();
            if (currentDivision != null) {
                List<Match> matches = matchDAO.findAllByPlayer(player, currentDivision.getRound(), 2);
                player.withMatches(matches);
            }
        }
        user.setPlayers(players);
        return user;
    }
}
