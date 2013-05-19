package org.squashleague.web.controller.administration;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.squashleague.dao.league.RoundDAO;
import org.squashleague.domain.league.Round;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@RequestMapping("/round")
@Controller
public class RoundController {

    @Resource
    private RoundDAO roundDAO;

    @RequestMapping(value = "save", method = RequestMethod.POST)
    public String create(@Valid Round round, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("bindingResult", bindingResult);
            redirectAttributes.addFlashAttribute("round", round);
            return "redirect:/administration";
        }
        roundDAO.save(round);
        return "redirect:/administration";
    }

    @RequestMapping(value = "update/{id}", method = RequestMethod.GET)
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("round", roundDAO.findOne(id));
        return "page/round/update";
    }

    @RequestMapping(value = "update", method = RequestMethod.POST)
    public String update(@Valid Round round, BindingResult bindingResult, Model uiModel) {
        if (bindingResult.hasErrors()) {
            uiModel.addAttribute("round", round);
            return "page/round/update";
        }
        roundDAO.update(round);
        return "redirect:/administration";
    }

    @RequestMapping(value = "delete/{id}", method = RequestMethod.GET)
    public String delete(@PathVariable("id") Long id) {
        roundDAO.delete(id);
        return "redirect:/administration";
    }

}
