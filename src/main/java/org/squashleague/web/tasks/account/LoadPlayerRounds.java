package org.squashleague.web.tasks.account;

import org.squashleague.dao.league.LeagueDAO;
import org.squashleague.dao.league.RoundDAO;
import org.squashleague.domain.account.User;
import org.squashleague.domain.league.League;
import org.squashleague.domain.league.Round;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * @author jamesdbloom
 */
public class LoadPlayerRounds implements Callable<List<Round>> {

    private final RoundDAO roundDAO;
    private final User user;

    public LoadPlayerRounds(RoundDAO roundDAO, User user) {
        this.roundDAO = roundDAO;
        this.user = user;
    }

    @Override
    public List<Round> call() throws Exception {
        return roundDAO.findAllForUser(user);
    }
}
