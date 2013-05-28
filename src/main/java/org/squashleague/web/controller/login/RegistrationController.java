package org.squashleague.web.controller.login;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.squashleague.dao.account.UserDAO;
import org.squashleague.domain.account.MobilePrivacy;
import org.squashleague.domain.account.Role;
import org.squashleague.domain.account.User;
import org.squashleague.service.email.EmailService;
import org.squashleague.service.uuid.UUIDService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;

@Controller
public class RegistrationController {

    protected Logger logger = LoggerFactory.getLogger(getClass());
    @Resource
    private UserDAO userDAO;
    @Resource
    private Environment environment;
    @Resource
    private EmailService emailService;
    @Resource
    private UUIDService uuidService;

    private void setupModel(Model uiModel) {
        uiModel.addAttribute("mobilePrivacyOptions", MobilePrivacy.enumToFormOptionMap());
        uiModel.addAttribute("passwordPattern", User.PASSWORD_PATTERN);
        uiModel.addAttribute("emailPattern", User.EMAIL_PATTERN);
        uiModel.addAttribute("environment", environment);
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String registerForm(Model uiModel) {
        setupModel(uiModel);
        uiModel.addAttribute("user", new User());
        return "page/user/register";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String register(@Valid final User user, BindingResult bindingResult, final HttpServletRequest request, Model uiModel) throws MalformedURLException, UnsupportedEncodingException {

        boolean userAlreadyExists = user.getEmail() != null && (userDAO.findByEmail(user.getEmail()) != null);
        if (bindingResult.hasErrors() || userAlreadyExists) {
            setupModel(uiModel);
            if (userAlreadyExists) {
                bindingResult.addError(new ObjectError("user", environment.getProperty("validation.user.alreadyExists")));
            }
            uiModel.addAttribute("bindingResult", bindingResult);
            uiModel.addAttribute("user", user);
            return "page/user/register";
        }

        userDAO.register(user
                .withRoles(("jamesdbloom@gmail.com".equals(user.getEmail()) ? Role.ROLE_ADMIN : Role.ROLE_USER))
                .withOneTimeToken(uuidService.generateUUID())
        );
        emailService.sendRegistrationMessage(user, request);
        return "redirect:/login";
    }
}
