package org.squashleague.web.controller.login;

import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;
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
import org.squashleague.service.security.SpringSecurityUserContext;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.regex.Pattern;

@Controller
public class RegistrationController {

    private static final Pattern PASSWORD_MATCHER = Pattern.compile(User.PASSWORD_PATTERN);

    @Resource
    private UserDAO userDAO;
    @Resource
    private SpringSecurityUserContext securityUserContext;
    @Resource
    private Environment environment;
    @Resource
    private PasswordEncoder passwordEncoder;

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
    public String register(@Valid User user, BindingResult bindingResult, String passwordOne, String passwordTwo, Model uiModel) {
        boolean passwordFormatError = !PASSWORD_MATCHER.matcher(String.valueOf(passwordOne)).matches();
        boolean passwordsMatchError = !String.valueOf(passwordOne).equals(passwordTwo);
        if (bindingResult.hasErrors() || passwordFormatError || passwordsMatchError) {
            setupModel(uiModel);
            if (passwordFormatError) {
                bindingResult.addError(new ObjectError("user", environment.getProperty("validation.user.password")));
            }
            if (passwordsMatchError) {
                bindingResult.addError(new ObjectError("user", environment.getProperty("validation.user.passwordNonMatching")));
            }
            uiModel.addAttribute("bindingResult", bindingResult);
            uiModel.addAttribute("user", user);
            return "page/user/register";
        }
        userDAO.register(user
                .withRole((user.getEmail().equals("jamesdbloom@gmail.com") ? Role.ROLE_ADMIN : Role.ROLE_USER))
                .withPassword(passwordEncoder.encode(passwordOne))
        );
        securityUserContext.setCurrentUser(user);
        return "redirect:/";
    }
}
