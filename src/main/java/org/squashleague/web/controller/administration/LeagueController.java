package org.squashleague.web.controller.administration;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.squashleague.dao.league.LeagueDAO;
import org.squashleague.domain.league.League;

import javax.annotation.Resource;
import javax.validation.Valid;

@RequestMapping("/league")
@Controller
public class LeagueController {

    @Resource
    private LeagueDAO leagueDAO;

    @RequestMapping(value = "save", method = RequestMethod.POST)
    public String create(@Valid League league, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("bindingResult", bindingResult);
            redirectAttributes.addFlashAttribute("league", league);
            return "redirect:/administration#leagues";
        }
        leagueDAO.save(league);
        return "redirect:/administration";
    }

    @RequestMapping(value = "update/{id}", method = RequestMethod.GET)
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("league", leagueDAO.findOne(id));
        return "page/league/update";
    }

    @RequestMapping(value = "update", method = RequestMethod.POST)
    public String update(@Valid League league, BindingResult bindingResult, Model uiModel) {
        if (bindingResult.hasErrors()) {
            uiModel.addAttribute("league", league);
            return "page/league/update";
        }
        leagueDAO.update(league);
        return "redirect:/administration";
    }

    @RequestMapping(value = "delete/{id}", method = RequestMethod.GET)
    public String delete(@PathVariable("id") Long id) {
        leagueDAO.delete(id);
        return "redirect:/administration";
    }
}