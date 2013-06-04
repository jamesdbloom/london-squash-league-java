package org.squashleague.web.controller.administration;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.squashleague.dao.account.RoleDAO;
import org.squashleague.dao.account.UserDAO;
import org.squashleague.domain.account.MobilePrivacy;
import org.squashleague.domain.account.Role;
import org.squashleague.domain.account.User;
import org.squashleague.service.http.RequestParser;
import org.squashleague.service.security.SpringSecurityUserContext;

import javax.annotation.Resource;
import javax.validation.Valid;

@Controller
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserDAO userDAO;
    @Resource
    private RoleDAO roleDAO;
    @Resource
    private Environment environment;
    @Resource
    private SpringSecurityUserContext securityUserContext;
    @Resource
    private RequestParser requestParser;

    private void setupModel(Model uiModel) {
        uiModel.addAttribute("mobilePrivacyOptions", MobilePrivacy.enumToFormOptionMap());
        uiModel.addAttribute("roles", roleDAO.findAll());
        uiModel.addAttribute("emailPattern", User.EMAIL_PATTERN);
        uiModel.addAttribute("environment", environment);
    }

    @RequestMapping(value = "save", method = RequestMethod.POST)
    public String create(@Valid User user, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        boolean userAlreadyExists = user.getEmail() != null && (userDAO.findByEmail(user.getEmail()) != null);
        if (bindingResult.hasErrors() || userAlreadyExists) {
            if (userAlreadyExists) {
                bindingResult.addError(new ObjectError("user", environment.getProperty("validation.user.alreadyExists")));
            }
            redirectAttributes.addFlashAttribute("bindingResult", bindingResult);
            redirectAttributes.addFlashAttribute("user", user);
            return "redirect:/administration#users";
        }
        userDAO.save(user);
        return "redirect:/administration";
    }

    @RequestMapping(value = "update/{id}", method = RequestMethod.GET)
    public String updateForm(@PathVariable("id") Long id, @RequestHeader(value = "Referer", required = false) String referer, Model uiModel) {
        setupModel(uiModel);
        uiModel.addAttribute("user", userDAO.findById(id));
        uiModel.addAttribute("referer", requestParser.parseRelativeURI(referer, "/account"));
        return "page/administration/user/update";
    }

    @Transactional
    @RequestMapping(value = "update", method = RequestMethod.POST)
    public String update(@Valid User user, BindingResult bindingResult, String referer, Model uiModel) {
        User currentUser = securityUserContext.getCurrentUser();
        boolean userAlreadyExists = user.getEmail() != null && !currentUser.getEmail().equals(user.getEmail()) && (userDAO.findByEmail(user.getEmail()) != null);
        if (bindingResult.hasErrors() || userAlreadyExists) {
            setupModel(uiModel);
            if (userAlreadyExists) {
                bindingResult.addError(new ObjectError("user", environment.getProperty("validation.user.alreadyExists")));
            }
            uiModel.addAttribute("bindingResult", bindingResult);
            uiModel.addAttribute("user", user);
            uiModel.addAttribute("referer", requestParser.parseRelativeURI(referer, "/account"));
            return "page/administration/user/update";
        }
        if (!currentUser.hasRole(Role.ROLE_ADMIN)) {
            user.setRoles(null);
        }
        userDAO.update(user);
        if (user.getId().equals(currentUser.getId())) {
            securityUserContext.setCurrentUser(userDAO.findById(user.getId()));
        }
        return "redirect:" + requestParser.parseRelativeURI(referer, "/account");
    }

    @RequestMapping(value = "delete/{id}", method = RequestMethod.GET)
    public String delete(@PathVariable("id") Long id) {
        userDAO.delete(id);
        return "redirect:/administration";
    }

}
