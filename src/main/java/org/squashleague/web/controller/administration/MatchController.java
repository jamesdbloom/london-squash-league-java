package org.squashleague.web.controller.administration;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.squashleague.dao.league.DivisionDAO;
import org.squashleague.dao.league.MatchDAO;
import org.squashleague.dao.league.PlayerDAO;
import org.squashleague.domain.league.Match;

import javax.annotation.Resource;
import javax.validation.Valid;

@RequestMapping("/match")
@Controller
public class MatchController {

    @Resource
    private MatchDAO matchDAO;
    @Resource
    private DivisionDAO divisionDAO;
    @Resource
    private PlayerDAO playerDAO;
    @Resource
    private Environment environment;

    private void setupModel(Model uiModel) {
        uiModel.addAttribute("divisions", divisionDAO.findAll());
        uiModel.addAttribute("players", playerDAO.findAll());
        uiModel.addAttribute("environment", environment);
    }

    @RequestMapping(value = "save", method = RequestMethod.POST)
    public String create(@Valid Match match, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        boolean playersIdentical = match.getPlayerOne() != null && match.getPlayerTwo() != null && match.getPlayerOne().equals(match.getPlayerTwo());
        if (bindingResult.hasErrors() || playersIdentical) {
            if (playersIdentical) {
                bindingResult.addError(new ObjectError("match", environment.getProperty("validation.match.playersIdentical")));
            }
            redirectAttributes.addFlashAttribute("bindingResult", bindingResult);
            redirectAttributes.addFlashAttribute("match", match);
            return "redirect:/administration#matches";
        }
        matchDAO.save(match);
        return "redirect:/administration";
    }

    @RequestMapping(value = "update/{id:[0-9]+}", method = RequestMethod.GET)
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        setupModel(uiModel);
        uiModel.addAttribute("match", matchDAO.findById(id));
        return "page/administration/match/update";
    }

    @RequestMapping(value = "update", method = RequestMethod.POST)
    public String update(@Valid Match match, BindingResult bindingResult, Model uiModel) {
        boolean playersIdentical = match.getPlayerOne() != null && match.getPlayerTwo() != null && match.getPlayerOne().equals(match.getPlayerTwo());
        if (bindingResult.hasErrors() || playersIdentical) {
            if (playersIdentical) {
                bindingResult.addError(new ObjectError("match", environment.getProperty("validation.match.playersIdentical")));
            }
            setupModel(uiModel);
            uiModel.addAttribute("bindingResult", bindingResult);
            uiModel.addAttribute("match", match);
            return "page/administration/match/update";
        }
        matchDAO.update(match);
        return "redirect:/administration";
    }

    @RequestMapping(value = "delete/{id:[0-9]+}", method = RequestMethod.GET)
    public String delete(@PathVariable("id") Long id) {
        matchDAO.delete(id);
        return "redirect:/administration";
    }

}
