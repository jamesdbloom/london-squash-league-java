package org.squashleague.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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

    @RequestMapping(params = "save", method = RequestMethod.POST)
    public String create(@Valid Match match, BindingResult bindingResult, Model uiModel, HttpSession session) {
        if (bindingResult.hasErrors()) {
            session.setAttribute("bindingResult", bindingResult);
            session.setAttribute("match", match);
            return "redirect:/page/administration";
        }
        session.removeAttribute("bindingResult");
        uiModel.asMap().clear();
        matchDAO.save(match);
        return "redirect:/page/administration";
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
        uiModel.asMap().clear();
        matchDAO.update(match);
        return "redirect:/page/administration";
    }

    @RequestMapping(params = "delete/{id}", method = RequestMethod.GET)
    public String delete(@PathVariable("delete") Long id, Model uiModel) {
        matchDAO.delete(id);
        uiModel.asMap().clear();
        return "redirect:/page/administration";
    }

}
