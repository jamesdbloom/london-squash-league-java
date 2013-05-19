package org.squashleague.web.controller.administration;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.squashleague.dao.league.ClubDAO;
import org.squashleague.domain.league.Club;

import javax.annotation.Resource;
import javax.validation.Valid;

@RequestMapping("/club")
@Controller
public class ClubController {

    @Resource
    private ClubDAO clubDAO;

    @RequestMapping(value = "save", method = RequestMethod.POST)
    public String create(@Valid Club club, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("bindingResult", bindingResult);
            redirectAttributes.addFlashAttribute("club", club);
        } else {
            clubDAO.save(club);
        }
        return "redirect:/administration";
    }

    @RequestMapping(value = "update/{id}", method = RequestMethod.GET)
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("club", clubDAO.findOne(id));
        return "page/club/update";
    }

    @RequestMapping(value = "update", method = RequestMethod.POST)
    public String update(@Valid Club club, BindingResult bindingResult, Model uiModel) {
        if (bindingResult.hasErrors()) {
            uiModel.addAttribute("club", club);
            return "page/club/update";
        }
        clubDAO.update(club);
        return "redirect:/administration";
    }

    @RequestMapping(value = "delete/{id}", method = RequestMethod.GET)
    public String delete(@PathVariable("delete") Long id) {
        clubDAO.delete(id);
        return "redirect:/administration";
    }

}
