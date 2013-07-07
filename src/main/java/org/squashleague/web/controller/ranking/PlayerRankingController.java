package org.squashleague.web.controller.ranking;

import com.google.common.base.Function;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.squashleague.dao.league.MatchDAO;
import org.squashleague.domain.league.Match;
import org.squashleague.domain.league.Round;
import org.squashleague.web.controller.round.NewDivisionService;

import javax.annotation.Resource;

/**
 * @author jamesdbloom
 */
@Controller
public class PlayerRankingController {

    private final NewDivisionService divisionSizeService = new NewDivisionService();
    protected Logger logger = LoggerFactory.getLogger(this.getClass());
    @Resource
    private MatchDAO matchDAO;
    @Resource
    private Environment environment;

    @RequestMapping(value = "/ranking", method = RequestMethod.GET)
    public String list(Model uiModel) {
        Multimap<Round, Match> matchesByRound = Multimaps.index(matchDAO.findAll(), new Function<Match, Round>() {
            @Override
            public Round apply(Match match) {
                return match.getDivision().getRound();
            }
        });
        for (Round round : matchesByRound.keySet()) {
            round.setPlayerSortedByScore(divisionSizeService.sortPlayersByScore(matchesByRound.get(round)));
        }

        uiModel.addAttribute("rounds", matchesByRound.keySet());
        return "page/ranking/playerRanking";
    }


}
