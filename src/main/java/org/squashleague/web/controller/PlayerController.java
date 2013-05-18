package org.squashleague.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.squashleague.dao.league.PlayerDAO;
import org.squashleague.domain.league.Player;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@RequestMapping("/player")
@Controller
public class PlayerController {

    @Resource
    private PlayerDAO playerDAO;

    @RequestMapping(params = "save", method = RequestMethod.POST)
    public String create(@Valid Player player, BindingResult bindingResult, Model uiModel, HttpSession session) {
        if (bindingResult.hasErrors()) {
            session.setAttribute("bindingResult", bindingResult);
            session.setAttribute("player", player);
            return "redirect:/page/administration";
        }
        session.removeAttribute("bindingResult");
        uiModel.asMap().clear();
        playerDAO.save(player);
        return "redirect:/page/administration";
    }

    @RequestMapping(value = "update/{id}", method = RequestMethod.GET)
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("player", playerDAO.findOne(id));
        return "page/player/update";
    }

    @RequestMapping(value = "update", method = RequestMethod.POST)
    public String update(@Valid Player player, BindingResult bindingResult, Model uiModel) {
        if (bindingResult.hasErrors()) {
            uiModel.addAttribute("player", player);
            return "page/player/update";
        }
        uiModel.asMap().clear();
        playerDAO.update(player);
        return "redirect:/page/administration";
    }

    @RequestMapping(params = "delete/{id}", method = RequestMethod.GET)
    public String delete(@PathVariable("delete") Long id, Model uiModel) {
        playerDAO.delete(id);
        uiModel.asMap().clear();
        return "redirect:/page/administration";
    }

}
