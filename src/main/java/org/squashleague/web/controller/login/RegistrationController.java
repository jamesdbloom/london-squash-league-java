package org.squashleague.web.controller.login;

import com.eaio.uuid.UUID;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
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
import javax.validation.constraints.Pattern;

@Controller
public class RegistrationController {

    public static final String PASSWORD_PATTERN = "^.*(?=.{8,})(?=.*\\d)(?=.*(\\£|\\!|\\@|\\#|\\$|\\%|\\^|\\&|\\*|\\(|\\)|\\-|\\_|\\[|\\]|\\{|\\}|\\<|\\>|\\~|\\`|\\+|\\=|\\,|\\.|\\;|\\:|\\/|\\?|\\|))(?=.*[a-zA-Z]).*$";
    public static final String EMAIL_PATTERN = "^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$";
    @Resource
    private UserDAO userDAO;
    @Resource
    private SpringSecurityUserContext userContext;
    @Resource
    private Environment environment;
    @Resource
    private StandardPasswordEncoder passwordEncoder;

    private void setupModel(Model uiModel) {
        uiModel.addAttribute("mobilePrivacyOptions", MobilePrivacy.enumToFormOptionMap());
        uiModel.addAttribute("passwordPattern", PASSWORD_PATTERN);
        uiModel.addAttribute("emailPattern", EMAIL_PATTERN);
        uiModel.addAttribute("environment", environment);
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String registerForm(Model uiModel) {
        setupModel(uiModel);
        uiModel.addAttribute("user", new User());
        return "page/user/register";
    }

    // todo add tests for registration controller and registration integration

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String register(@Valid User user, BindingResult bindingResult, String passwordOne, String passwordTwo, Model uiModel) {
        if (bindingResult.hasErrors()) {
            setupModel(uiModel);
            if (!PASSWORD_PATTERN.matches(passwordOne)) {
                bindingResult.addError(new ObjectError("user", environment.getProperty("validation.user.password")));
            }
            if (!passwordOne.equals(passwordTwo)) {
                bindingResult.addError(new ObjectError("user", environment.getProperty("validation.user.passwordNonMatching")));
            }
            uiModel.addAttribute("bindingResult", bindingResult);
            uiModel.addAttribute("user", user);
            return "page/user/register";
        }
        // add to DB
        String salt = new UUID().toString();
        userDAO.save(user
                .withRole((user.getEmail().startsWith("admin") ? Role.ROLE_ADMIN : Role.ROLE_USER))
                .withPassword(passwordEncoder.encode(passwordOne))
        );
        // mark user as logged in
        userContext.setCurrentUser(user);
        return "redirect:/";
    }
}
