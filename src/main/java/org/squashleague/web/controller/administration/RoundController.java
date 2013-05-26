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
import org.squashleague.dao.league.RoundDAO;
import org.squashleague.domain.league.Round;

import javax.annotation.Resource;
import javax.validation.Valid;

@RequestMapping("/round")
@Controller
public class RoundController {

    @Resource
    private RoundDAO roundDAO;
    @Resource
    private DivisionDAO divisionDAO;
    @Resource
    private Environment environment;

    private void setupModel(Model uiModel) {
        uiModel.addAttribute("divisions", divisionDAO.findAll());
        uiModel.addAttribute("environment", environment);
    }

    @RequestMapping(value = "save", method = RequestMethod.POST)
    public String create(@Valid Round round, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        boolean startDateAfterEndDate = round.getStartDate() != null && round.getEndDate() != null && round.getStartDate().isAfter(round.getEndDate());
        if (bindingResult.hasErrors() || startDateAfterEndDate) {
            if(startDateAfterEndDate) {
                bindingResult.addError(new ObjectError("round", environment.getProperty("validation.round.startDateAfterEndDate")));
            }
            redirectAttributes.addFlashAttribute("bindingResult", bindingResult);
            redirectAttributes.addFlashAttribute("round", round);
            return "redirect:/administration#rounds";
        }
        roundDAO.save(round);
        return "redirect:/administration";
    }

    @RequestMapping(value = "update/{id}", method = RequestMethod.GET)
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        setupModel(uiModel);
        uiModel.addAttribute("round", roundDAO.findById(id));
        return "page/administration/round/update";
    }

    @RequestMapping(value = "update", method = RequestMethod.POST)
    public String update(@Valid Round round, BindingResult bindingResult, Model uiModel) {
        boolean startDateAfterEndDate = round.getStartDate() != null && round.getEndDate() != null && round.getStartDate().isAfter(round.getEndDate());
        if (bindingResult.hasErrors() || startDateAfterEndDate) {
            if(startDateAfterEndDate) {
                bindingResult.addError(new ObjectError("round", environment.getProperty("validation.round.startDateAfterEndDate")));
            }
            setupModel(uiModel);
            uiModel.addAttribute("bindingResult", bindingResult);
            uiModel.addAttribute("round", round);
            return "page/administration/round/update";
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
