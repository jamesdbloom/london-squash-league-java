package org.squashleague.web.controller.administration;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.squashleague.dao.account.UserDAO;
import org.squashleague.domain.account.MobilePrivacy;
import org.squashleague.domain.account.User;

import javax.annotation.Resource;
import javax.validation.Valid;

@RequestMapping("/user")
@Controller
public class UserController {

    @Resource
    private UserDAO userDAO;
    @Resource
    private Environment environment;

    @RequestMapping(value = "save", method = RequestMethod.POST)
    public String create(@Valid User user, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("bindingResult", bindingResult);
            redirectAttributes.addFlashAttribute("user", user);
            return "redirect:/administration#users";
        }
        userDAO.save(user);
        return "redirect:/administration";
    }

    @RequestMapping(value = "update/{id}", method = RequestMethod.GET)
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("environment", environment);
        uiModel.addAttribute("user", userDAO.findById(id));
        uiModel.addAttribute("mobilePrivacyOptions", MobilePrivacy.enumToFormOptionMap());
        return "page/user/update";
    }

    @RequestMapping(value = "update", method = RequestMethod.POST)
    public String update(@Valid User user, BindingResult bindingResult, Model uiModel) {
        if (bindingResult.hasErrors()) {
            uiModel.addAttribute("environment", environment);
            uiModel.addAttribute("bindingResult", bindingResult);
            uiModel.addAttribute("user", user);
            uiModel.addAttribute("mobilePrivacyOptions", MobilePrivacy.enumToFormOptionMap());
            return "page/user/update";
        }
        userDAO.update(user);
        return "redirect:/account";
    }

    @RequestMapping(value = "delete/{id}", method = RequestMethod.GET)
    public String delete(@PathVariable("id") Long id) {
        userDAO.delete(id);
        return "redirect:/administration";
    }

}
