package org.squashleague.web.controller.administration;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
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

    @RequestMapping(value = "save", method = RequestMethod.POST)
    public String create(@Valid Player player, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("bindingResult", bindingResult);
            redirectAttributes.addFlashAttribute("player", player);
            return "redirect:/administration";
        }
        playerDAO.save(player);
        return "redirect:/administration";
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
        playerDAO.update(player);
        return "redirect:/administration";
    }

    @RequestMapping(value = "delete/{id}", method = RequestMethod.GET)
    public String delete(@PathVariable("id") Long id) {
        playerDAO.delete(id);
        return "redirect:/administration";
    }

}
