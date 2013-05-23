package org.squashleague.web.controller.administration;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.squashleague.dao.account.UserDAO;
import org.squashleague.dao.league.DivisionDAO;
import org.squashleague.dao.league.LeagueDAO;
import org.squashleague.dao.league.PlayerDAO;
import org.squashleague.domain.account.MobilePrivacy;
import org.squashleague.domain.league.Player;
import org.squashleague.domain.league.PlayerStatus;

import javax.annotation.Resource;
import javax.validation.Valid;

@RequestMapping("/player")
@Controller
public class PlayerController {

    @Resource
    private PlayerDAO playerDAO;
    @Resource
    private UserDAO userDAO;
    @Resource
    private DivisionDAO divisionDAO;
    @Resource
    private LeagueDAO leagueDAO;
    @Resource
    private Environment environment;

    private void setupModel(Model uiModel) {
        uiModel.addAttribute("playerStatuses", PlayerStatus.enumToFormOptionMap());
        uiModel.addAttribute("users", userDAO.findAll());
        uiModel.addAttribute("divisions", divisionDAO.findAll());
        uiModel.addAttribute("leagues", leagueDAO.findAll());
        uiModel.addAttribute("environment", environment);
    }

    @RequestMapping(value = "save", method = RequestMethod.POST)
    public String create(@Valid Player player, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("bindingResult", bindingResult);
            redirectAttributes.addFlashAttribute("player", player);
            return "redirect:/administration#players";
        }
        playerDAO.save(player);
        return "redirect:/administration";
    }

    @RequestMapping(value = "update/{id}", method = RequestMethod.GET)
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        setupModel(uiModel);
        uiModel.addAttribute("player", playerDAO.findById(id));
        return "page/player/update";
    }

    @RequestMapping(value = "update", method = RequestMethod.POST)
    public String update(@Valid Player player, BindingResult bindingResult, Model uiModel) {
        if (bindingResult.hasErrors()) {
            setupModel(uiModel);
            uiModel.addAttribute("bindingResult", bindingResult);
            uiModel.addAttribute("player", player);
            return "page/player/update";
        }
        playerDAO.update(player);
        return "redirect:/administration";
    }

    @RequestMapping(value = "delete/{id}", method = RequestMethod.GET)
    public String delete(@PathVariable("id") Long id) {
        playerDAO.delete(id);
        return "redirect:/administration";
    }

}
