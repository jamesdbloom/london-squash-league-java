package org.squashleague.web.tasks.account;

import org.squashleague.dao.league.LeagueDAO;
import org.squashleague.domain.account.User;
import org.squashleague.domain.league.League;
import org.squashleague.domain.league.Match;
import org.squashleague.domain.league.Player;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * @author jamesdbloom
 */
public class LoadUnregisteredLeagues implements Callable<List<League>> {

    private final LeagueDAO leagueDAO;
    private final User user;

    public LoadUnregisteredLeagues(LeagueDAO leagueDAO, User user) {
        this.leagueDAO = leagueDAO;
        this.user = user;
    }

    @Override
    public List<League> call() throws Exception {
        return leagueDAO.findAllUnregisteredLeagues(user);
    }
}
