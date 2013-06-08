package org.squashleague.web.controller.administration;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.squashleague.dao.league.DivisionDAO;
import org.squashleague.dao.league.LeagueDAO;
import org.squashleague.domain.league.Division;

import javax.annotation.Resource;
import javax.validation.Valid;

@RequestMapping("/division")
@Controller
public class DivisionController {

    @Resource
    private DivisionDAO divisionDAO;
    @Resource
    private LeagueDAO leagueDAO;
    @Resource
    private Environment environment;

    private void setupModel(Model uiModel) {
        uiModel.addAttribute("leagues", leagueDAO.findAll());
        uiModel.addAttribute("environment", environment);
    }

    @RequestMapping(value = "save", method = RequestMethod.POST)
    public String create(@Valid Division division, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("bindingResult", bindingResult);
            redirectAttributes.addFlashAttribute("division", division);
            return "redirect:/administration#divisions";
        }
        divisionDAO.save(division);
        return "redirect:/administration";
    }

    @RequestMapping(value = "update/{id:[0-9]+}", method = RequestMethod.GET)
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        setupModel(uiModel);
        uiModel.addAttribute("division", divisionDAO.findById(id));
        return "page/administration/division/update";
    }

    @RequestMapping(value = "update", method = RequestMethod.POST)
    public String update(@Valid Division division, BindingResult bindingResult, Model uiModel) {
        if (bindingResult.hasErrors()) {
            setupModel(uiModel);
            uiModel.addAttribute("bindingResult", bindingResult);
            uiModel.addAttribute("division", division);
            return "page/administration/division/update";
        }
        divisionDAO.update(division);
        return "redirect:/administration";
    }

    @RequestMapping(value = "delete/{id:[0-9]+}", method = RequestMethod.GET)
    public String delete(@PathVariable("id") Long id) {
        divisionDAO.delete(id);
        return "redirect:/administration";
    }
}
