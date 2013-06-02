package org.squashleague.web.tasks.league;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.squashleague.dao.league.MatchDAO;
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
