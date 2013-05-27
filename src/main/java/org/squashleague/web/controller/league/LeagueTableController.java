package org.squashleague.web.controller.league;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.squashleague.dao.account.RoleDAO;
import org.squashleague.dao.account.UserDAO;
import org.squashleague.dao.league.*;
import org.squashleague.domain.account.User;
import org.squashleague.domain.league.Division;
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
        User user = userDAO.findById(securityUserContext.getCurrentUser().getId());
        Map<Player, List<Match>> matches = new HashMap<>();
        for (Player player : user.getPlayers()) {
            matches.put(player, matchDAO.findByPlayer(player));
            for (Division division : player.getLeague().getDivisions()) {
                for (Round round : division.getRounds()) {
                    for (Match match : round.getMatches()) {
                        match.toString();
                    }
                }
            }
        }
        uiModel.addAttribute("user", user);
        uiModel.addAttribute("matches", matches);
        return "page/league/leagueTable";
    }

    @Transactional
    @RequestMapping(value = "/account", method = RequestMethod.GET)
    public String getAccountPage(Model uiModel) {
        User user = userDAO.findById(securityUserContext.getCurrentUser().getId());
        Map<Long, List<Match>> matches = new HashMap<>();
        for (Player player : user.getPlayers()) {
            matches.put(player.getId(), matchDAO.findByPlayer(player));
            for (Division division : player.getLeague().getDivisions()) {
                for (Round round : division.getRounds()) {
                    for (Match match : round.getMatches()) {
                        match.toString();
                    }
                }
            }
        }
        uiModel.addAttribute("user", user);
        uiModel.addAttribute("playerToMatches", matches);
        return "page/account/account";
    }
}