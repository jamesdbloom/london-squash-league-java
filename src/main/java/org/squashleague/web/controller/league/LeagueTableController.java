package org.squashleague.web.controller.league;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.squashleague.dao.league.MatchDAO;
import org.squashleague.dao.league.PlayerDAO;
import org.squashleague.domain.ModelObject;
import org.squashleague.domain.account.User;
import org.squashleague.domain.league.Match;
import org.squashleague.domain.league.Player;
import org.squashleague.domain.league.Round;
import org.squashleague.service.security.SpringSecurityUserContext;

import javax.annotation.Resource;
import java.util.*;

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

    @Transactional
    @RequestMapping(value = "/leagueTable", method = RequestMethod.GET)
    public String getPage(boolean showAllDivisions, Model uiModel) {
        final User user = securityUserContext.getCurrentUser();
        user.setPlayers(playerDAO.findAllActiveByUser(user));

        List<Match> matches = matchDAO.findAll();
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
                return CollectionUtils.containsAny(round.getPlayers(), user.getPlayers());
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

        uiModel.addAttribute("showAllDivisions", showAllDivisions);
        uiModel.addAttribute("rounds", (showAllDivisions ? usersLeagueRounds : usersPlayerRounds));
        uiModel.addAttribute("user", user);
        return "page/league/leagueTable";
    }

    @Transactional
    @RequestMapping(value = "/print", method = RequestMethod.GET)
    public String getPage(Model uiModel) {
        List<Match> matches = matchDAO.findAll();
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

        uiModel.addAttribute("print", true);
        uiModel.addAttribute("rounds", allRounds);
        return "page/league/leagueTable";
    }
}
