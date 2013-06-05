package org.squashleague.web.tasks.league;

import org.squashleague.dao.league.MatchDAO;
import org.squashleague.domain.league.Match;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * @author jamesdbloom
 */
public class LoadAllMatches implements Callable<List<Match>> {

    private final MatchDAO matchDAO;

    public LoadAllMatches(MatchDAO matchDAO) {
        this.matchDAO = matchDAO;
    }

    @Override
    public List<Match> call() throws Exception {
        return matchDAO.findAll();
    }
}
