package org.squashleague.web.controller.league;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.squashleague.dao.league.ClubDAO;
import org.squashleague.service.security.SpringSecurityUserContext;

import javax.annotation.Resource;

/**
 * @author jamesdbloom
 */
@Controller
public class LeagueTableController {

    @Resource
    private SpringSecurityUserContext securityUserContext;
    @Resource
    private ClubDAO clubDAO;

    @RequestMapping(value = "/leagueTable", method = RequestMethod.GET)
    public String getPage(Model uiModel) {
        uiModel.addAttribute("clubs", clubDAO.findAll());
        return "page/league/league";
    }
}
