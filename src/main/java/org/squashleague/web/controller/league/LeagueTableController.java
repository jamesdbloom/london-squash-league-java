package org.squashleague.web.controller.league;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
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
    public String getPage(Model uiModel) {
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
        List<Round> rounds = new ArrayList<>(roundsById.values());
        Collections.sort(rounds);
        Collection<Round> userRounds = Collections2.filter(rounds, new Predicate<Round>() {
            public boolean apply(Round round) {
                return CollectionUtils.containsAny(round.getPlayers(), user.getPlayers());
            }
        });

        uiModel.addAttribute("userRounds", userRounds);
        uiModel.addAttribute("rounds", rounds);
        uiModel.addAttribute("user", user);
        return "page/league/leagueTable";
    }
}
