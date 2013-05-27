package org.squashleague.web.controller.league;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.squashleague.dao.account.RoleDAO;
import org.squashleague.dao.account.UserDAO;
import org.squashleague.dao.league.*;
import org.squashleague.domain.league.Match;
import org.squashleague.domain.league.Player;
import org.squashleague.domain.league.Round;
import org.squashleague.service.security.SpringSecurityUserContext;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author jamesdbloom
 */
@Controller
public class LeagueTableController {

    @Resource
    private RoleDAO roleDAO;
    @Resource
    private UserDAO userDAO;
    @Resource
    private ClubDAO clubDAO;
    @Resource
    private LeagueDAO leagueDAO;
    @Resource
    private DivisionDAO divisionDAO;
    @Resource
    private RoundDAO roundDAO;
    @Resource
    private PlayerDAO playerDAO;
    @Resource
    private MatchDAO matchDAO;
    @Resource
    private SpringSecurityUserContext securityUserContext;

    @Transactional
    @RequestMapping(value = "/leagueTable", method = RequestMethod.GET)
    public String getPage(Model uiModel) {
        List<Match> matches = matchDAO.findAll();
        Map<Long, Map<Long, Map<Long, Match>>> roundMatches = new HashMap<>();
        Map<Long, Map<Long, Player>> roundPlayers = new HashMap<>();
        Map<Long, Round> rounds = new HashMap<>();
        for (Match match : matches) {
            Map<Long, Map<Long, Match>> matchGrid;
            Map<Long, Player> players;
            Round round = match.getRound();
            rounds.put(round.getId(), round);
            if(roundMatches.containsKey(round.getId())) {
                matchGrid = roundMatches.get(round.getId());
                players = roundPlayers.get(round.getId());
            } else {
                matchGrid = new HashMap<>();
                roundMatches.put(round.getId(), matchGrid);
                players = new HashMap<>();
                roundPlayers.put(round.getId(), players);
            }
            Player playerOne = match.getPlayerOne();
            Player playerTwo = match.getPlayerTwo();
            players.put(playerOne.getId(), playerOne);
            players.put(playerTwo.getId(), playerTwo);
            if (matchGrid.containsKey(playerOne.getId())) {
                matchGrid.get(playerOne.getId()).put(playerTwo.getId(), match);
            } else {
                Map<Long, Match> column = new HashMap<>();
                column.put(playerTwo.getId(), match);
                matchGrid.put(playerOne.getId(), column);
            }
        }
        uiModel.addAttribute("roundMatches", roundMatches);
        uiModel.addAttribute("roundPlayers", roundPlayers);
        uiModel.addAttribute("rounds", rounds);
        uiModel.addAttribute("user", securityUserContext.getCurrentUser());
        return "page/league/leagueTable";
    }
}
