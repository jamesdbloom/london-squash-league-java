package org.squashleague.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.squashleague.dao.league.DivisionDAO;
import org.squashleague.domain.league.Division;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@RequestMapping("/division")
@Controller
public class DivisionController {

    @Resource
    private DivisionDAO divisionDAO;

    @RequestMapping(params = "save", method = RequestMethod.POST)
    public String create(@Valid Division division, BindingResult bindingResult, Model uiModel, HttpSession session) {
        if (bindingResult.hasErrors()) {
            session.setAttribute("bindingResult", bindingResult);
            session.setAttribute("division", division);
            return "redirect:/page/administration";
        }
        session.removeAttribute("bindingResult");
        uiModel.asMap().clear();
        divisionDAO.save(division);
        return "redirect:/page/administration";
    }

    @RequestMapping(value = "update/{id}", method = RequestMethod.GET)
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("division", divisionDAO.findOne(id));
        return "division/page/update";
    }

    @RequestMapping(value = "update", method = RequestMethod.POST)
    public String update(@Valid Division division, BindingResult bindingResult, Model uiModel) {
        if (bindingResult.hasErrors()) {
            uiModel.addAttribute("division", division);
            return "division/page/update";
        }
        uiModel.asMap().clear();
        divisionDAO.update(division);
        return "redirect:/page/administration";
    }

    @RequestMapping(params = "delete/{id}", method = RequestMethod.GET)
    public String delete(@PathVariable("delete") Long id, Model uiModel) {
        divisionDAO.delete(id);
        uiModel.asMap().clear();
        return "redirect:/page/administration";
    }
}
