package org.squashleague.web.controller.leagueTable;

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
import org.squashleague.domain.league.Division;
import org.squashleague.domain.league.Match;
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
        Map<Long, Division> roundsById = new HashMap<>();
        for (Division division : Lists.transform(matches, new Function<Match, Division>() {
            public Division apply(Match match) {
                return match.getDivision().addMatches(match);
            }
        })) {
            roundsById.put(division.getId(), division);
        }
        List<Division> allDivisions = new ArrayList<>(roundsById.values());
        Collections.sort(allDivisions);

        uiModel.addAttribute("print", true);
        uiModel.addAttribute("divisions", allDivisions);
        return "page/league/leagueTable";
    }
}
