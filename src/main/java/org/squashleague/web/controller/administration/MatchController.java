package org.squashleague.web.controller.administration;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.squashleague.dao.league.MatchDAO;
import org.squashleague.domain.league.Match;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@RequestMapping("/match")
@Controller
public class MatchController {

    @Resource
    private MatchDAO matchDAO;

    @RequestMapping(value = "save", method = RequestMethod.POST)
    public String create(@Valid Match match, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("bindingResult", bindingResult);
            redirectAttributes.addFlashAttribute("match", match);
            return "redirect:/administration#matchs";
        }
        matchDAO.save(match);
        return "redirect:/administration";
    }

    @RequestMapping(value = "update/{id}", method = RequestMethod.GET)
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("match", matchDAO.findOne(id));
        return "page/match/update";
    }

    @RequestMapping(value = "update", method = RequestMethod.POST)
    public String update(@Valid Match match, BindingResult bindingResult, Model uiModel) {
        if (bindingResult.hasErrors()) {
            uiModel.addAttribute("match", match);
            return "page/match/update";
        }
        matchDAO.update(match);
        return "redirect:/administration";
    }

    @RequestMapping(value = "delete/{id}", method = RequestMethod.GET)
    public String delete(@PathVariable("id") Long id) {
        matchDAO.delete(id);
        return "redirect:/administration";
    }

}
