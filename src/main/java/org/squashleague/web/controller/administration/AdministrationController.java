package org.squashleague.web.controller.administration;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.squashleague.dao.account.RoleDAO;
import org.squashleague.dao.account.UserDAO;
import org.squashleague.dao.league.*;
import org.squashleague.domain.account.MobilePrivacy;
import org.squashleague.domain.account.Role;
import org.squashleague.domain.account.User;
import org.squashleague.domain.league.*;

import javax.annotation.Resource;

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
    @Resource
    private RoleDAO roleDAO;

    @Resource
    private Environment environment;

    @RequestMapping(value = "/administration", method = RequestMethod.GET)
    public String list(Model uiModel) {
        uiModel.addAttribute("environment", environment);

        uiModel.addAttribute("roles", roleDAO.findAll());
        uiModel.addAttribute("users", userDAO.findAll());
        uiModel.addAttribute("mobilePrivacyOptions", MobilePrivacy.enumToFormOptionMap());
        uiModel.addAttribute("clubs", clubDAO.findAll());
        uiModel.addAttribute("leagues", leagueDAO.findAll());
        uiModel.addAttribute("divisions", divisionDAO.findAll());
        uiModel.addAttribute("rounds", roundDAO.findAll());
        uiModel.addAttribute("matches", matchDAO.findAll());
        uiModel.addAttribute("players", playerDAO.findAll());
        uiModel.addAttribute("playerStatuses", PlayerStatus.enumToFormOptionMap());

        if (!uiModel.containsAttribute("role")) {
            uiModel.addAttribute("role", new Role());
        }
        if (!uiModel.containsAttribute("user")) {
            uiModel.addAttribute("user", new User());
        }
        if (!uiModel.containsAttribute("club")) {
            uiModel.addAttribute("club", new Club());
        }
        if (!uiModel.containsAttribute("league")) {
            uiModel.addAttribute("league", new League());
        }
        if (!uiModel.containsAttribute("division")) {
            uiModel.addAttribute("division", new Division());
        }
        if (!uiModel.containsAttribute("round")) {
            uiModel.addAttribute("round", new Round());
        }
        if (!uiModel.containsAttribute("match")) {
            uiModel.addAttribute("match", new Match());
        }
        if (!uiModel.containsAttribute("player")) {
            uiModel.addAttribute("player", new Player());
        }
        return "page/administration/administration";
    }
}
