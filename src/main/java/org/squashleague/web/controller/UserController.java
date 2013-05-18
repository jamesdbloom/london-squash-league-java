package org.squashleague.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.squashleague.dao.account.UserDAO;
import org.squashleague.domain.account.MobilePrivacy;
import org.squashleague.domain.account.User;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@RequestMapping("/user")
@Controller
public class UserController {

    @Resource
    private UserDAO userDAO;

    @RequestMapping(value = "register", method = RequestMethod.GET)
    public String registerForm(Model uiModel) {
        uiModel.addAttribute("user", new User());
        uiModel.addAttribute("mobilePrivacyOptions", MobilePrivacy.enumToFormOptionMap());
        return "/page/user/register";
    }

    @RequestMapping(value = "register", method = RequestMethod.POST)
    public String register(@Valid User user, BindingResult bindingResult, Model uiModel) {
        if (bindingResult.hasErrors()) {
            uiModel.addAttribute("bindingResult", bindingResult);
            uiModel.addAttribute("user", user);
            uiModel.addAttribute("mobilePrivacyOptions", MobilePrivacy.enumToFormOptionMap());
            return "/page/user/register";
        }
        uiModel.asMap().clear();
        userDAO.save(user);
        return "redirect:/login";
    }

    @RequestMapping(value = "update/{id}", method = RequestMethod.GET)
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("user", userDAO.findOne(id));
        uiModel.addAttribute("mobilePrivacyOptions", MobilePrivacy.enumToFormOptionMap());
        return "/page/user/update";
    }

    @RequestMapping(value = "update", method = RequestMethod.POST)
    public String update(@Valid User user, BindingResult bindingResult, Model uiModel) {
        if (bindingResult.hasErrors()) {
            uiModel.addAttribute("bindingResult", bindingResult);
            uiModel.addAttribute("user", user);
            uiModel.addAttribute("mobilePrivacyOptions", MobilePrivacy.enumToFormOptionMap());
            return "/page/user/update";
        }
        uiModel.asMap().clear();
        userDAO.update(user);
        return "redirect:/account";
    }

    @RequestMapping(params = "delete/{id}", method = RequestMethod.GET)
    public String delete(@PathVariable("id") Long id, Model uiModel) {
        userDAO.delete(id);
        uiModel.asMap().clear();
        return "redirect:/page/administration";
    }

}
