package org.squashleague.web.controller.league;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.squashleague.dao.league.MatchDAO;
import org.squashleague.dao.league.PlayerDAO;
import org.squashleague.domain.account.User;
import org.squashleague.domain.league.Match;
import org.squashleague.domain.league.Round;
import org.squashleague.service.security.SpringSecurityUserContext;
import org.squashleague.web.tasks.CommandHolder;
import org.squashleague.web.tasks.league.LoadAllMatches;
import org.squashleague.web.tasks.league.LoadFullyPopulatedUser;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.Future;

/**
 * @author jamesdbloom
 */
@Controller
public class LeagueTableController {

    @Resource
    private PlayerDAO playerDAO;
    @Resource
    private MatchDAO matchDAO;
    @Resource
    private SpringSecurityUserContext securityUserContext;
    @Resource
    private ThreadPoolTaskExecutor taskExecutor;

    @Transactional
    @RequestMapping(value = "/leagueTable", method = RequestMethod.GET)
    public String getPage(final boolean showAllDivisions, Model uiModel) {
        Future<User> user = taskExecutor.submit(
                new LoadFullyPopulatedUser(playerDAO, CommandHolder.newListInstance(taskExecutor.submit(new LoadAllMatches(matchDAO)), Match.class), securityUserContext.getCurrentUser(), showAllDivisions)
        );
        uiModel.addAttribute("showAllDivisions", showAllDivisions);
        uiModel.addAttribute("user", CommandHolder.newInstance(user, User.class));
        return "page/league/leagueTable";
    }

    @Transactional
    @RequestMapping(value = "/print", method = RequestMethod.GET)
    public String getPage(Model uiModel) {
        List<Match> matches = matchDAO.findAll();
        Map<Long, Round> roundsById = new HashMap<>();
        for (Round round : Lists.transform(matches, new Function<Match, Round>() {
            public Round apply(Match match) {
                return match.getDivision().addMatches(match).getRound();
            }
        })) {
            roundsById.put(round.getId(), round);
        }
        List<Round> allRounds = new ArrayList<>(roundsById.values());
        Collections.sort(allRounds);

        uiModel.addAttribute("print", true);
        uiModel.addAttribute("rounds", allRounds);
        return "page/league/print";
    }
}
