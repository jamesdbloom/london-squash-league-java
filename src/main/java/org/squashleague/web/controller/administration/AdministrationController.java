package org.squashleague.web.controller.administration;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.squashleague.dao.account.UserDAO;
import org.squashleague.dao.league.*;
import org.squashleague.domain.account.User;
import org.squashleague.domain.league.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

/**
 * @author jamesdbloom
 */
@Controller
public class AdministrationController {

    @Resource
    private ClubDAO clubDAO;
    @Resource
    private LeagueDAO leagueDAO;
    @Resource
    private DivisionDAO divisionDAO;
    @Resource
    private RoundDAO roundDAO;
    @Resource
    private MatchDAO matchDAO;
    @Resource
    private PlayerDAO playerDAO;
    @Resource
    private UserDAO userDAO;

    @RequestMapping(value = "/administration", method = RequestMethod.GET)
    public String list(Model uiModel, HttpSession session) {
        uiModel.addAttribute("clubs", clubDAO.findAll());
        uiModel.addAttribute("leagues", leagueDAO.findAll());
        uiModel.addAttribute("divisions", divisionDAO.findAll());
        uiModel.addAttribute("rounds", roundDAO.findAll());
        uiModel.addAttribute("roundStatuses", RoundStatus.values());
        uiModel.addAttribute("matches", matchDAO.findAll());
        uiModel.addAttribute("players", playerDAO.findAll());
        uiModel.addAttribute("playerStatuses", PlayerStatus.values());
        uiModel.addAttribute("users", userDAO.findAll());

        uiModel.addAttribute("bindingResult", session.getAttribute("bindingResult"));
        uiModel.addAttribute("club", getSessionValueOrDefault(session, "club", new Club()));
        uiModel.addAttribute("league", getSessionValueOrDefault(session, "league", new League()));
        uiModel.addAttribute("division", getSessionValueOrDefault(session, "division", new Division()));
        uiModel.addAttribute("round", getSessionValueOrDefault(session, "round", new Round()));
        uiModel.addAttribute("match", getSessionValueOrDefault(session, "match", new Match()));
        uiModel.addAttribute("player", getSessionValueOrDefault(session, "player", new Player()));
        uiModel.addAttribute("user", getSessionValueOrDefault(session, "user", new User()));
        return "page/administration";
    }

    private Object getSessionValueOrDefault(HttpSession session, String key, Object defaultValue) {
        Object result = session.getAttribute(key);
        if (result == null) {
            result = defaultValue;
        }
        return result;
    }
}
