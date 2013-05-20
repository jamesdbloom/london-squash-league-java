package org.squashleague.web.controller.administration;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.squashleague.dao.account.RoleDAO;
import org.squashleague.domain.account.MobilePrivacy;
import org.squashleague.domain.account.Role;

import javax.annotation.Resource;
import javax.validation.Valid;

@RequestMapping("/role")
@Controller
public class RoleController {

    @Resource
    private RoleDAO roleDAO;

    @RequestMapping(value = "save", method = RequestMethod.POST)
    public String create(@Valid Role role, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("bindingResult", bindingResult);
            redirectAttributes.addFlashAttribute("role", role);
            return "redirect:/administration#roles";
        }
        roleDAO.save(role);
        return "redirect:/administration";
    }

    @RequestMapping(value = "update/{id}", method = RequestMethod.GET)
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("role", roleDAO.findOne(id));
        uiModel.addAttribute("mobilePrivacyOptions", MobilePrivacy.enumToFormOptionMap());
        return "page/role/update";
    }

    @RequestMapping(value = "update", method = RequestMethod.POST)
    public String update(@Valid Role role, BindingResult bindingResult, Model uiModel) {
        if (bindingResult.hasErrors()) {
            uiModel.addAttribute("bindingResult", bindingResult);
            uiModel.addAttribute("role", role);
            uiModel.addAttribute("mobilePrivacyOptions", MobilePrivacy.enumToFormOptionMap());
            return "page/role/update";
        }
        roleDAO.update(role);
        return "redirect:/account";
    }

    @RequestMapping(value = "delete/{id}", method = RequestMethod.GET)
    public String delete(@PathVariable("id") Long id) {
        roleDAO.delete(id);
        return "redirect:/administration";
    }

}
