package org.squashleague.web.controller.account;

import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.squashleague.dao.account.UserDAO;
import org.squashleague.domain.account.User;
import org.squashleague.service.email.EmailService;
import org.squashleague.service.security.CredentialValidation;
import org.squashleague.service.security.SpringSecurityUserContext;
import org.squashleague.service.uuid.UUIDService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Controller
public class UpdatePasswordController {

    private static final Pattern PASSWORD_MATCHER = Pattern.compile(User.PASSWORD_PATTERN);
    @Resource
    private Environment environment;
    @Resource
    private UserDAO userDAO;
    @Resource
    private PasswordEncoder passwordEncoder;
    @Resource
    private SpringSecurityUserContext securityUserContext;
    @Resource
    private EmailService emailService;
    @Resource
    private UUIDService uuidService;
    @Resource
    private CredentialValidation credentialValidation;

    @RequestMapping(value = "/retrievePassword", method = RequestMethod.GET)
    public String retrievePasswordForm(Model uiModel) throws MalformedURLException, UnsupportedEncodingException {
        uiModel.addAttribute("emailPattern", User.EMAIL_PATTERN);
        uiModel.addAttribute("environment", environment);
        return "page/user/retrievePassword";
    }

    @RequestMapping(value = "/sendUpdatePasswordEmail", method = {RequestMethod.GET, RequestMethod.POST})
    public String sendUpdatePasswordEmail(String email, HttpServletRequest request, RedirectAttributes redirectAttributes) throws MalformedURLException, UnsupportedEncodingException {
        User user = userDAO.findByEmail(email);
        if (user != null) {
            userDAO.updateOneTimeToken(user.withOneTimeToken(uuidService.generateUUID()));
            emailService.sendUpdatePasswordMessage(user, request);
        }
        redirectAttributes.addFlashAttribute("message", "An email has been sent to " + email + " with a link to create your password and login");
        redirectAttributes.addFlashAttribute("title", "Message Sent");
        return "redirect:/message";
    }

    private boolean hasInvalidToken(User user, String oneTimeToken, RedirectAttributes redirectAttributes) throws UnsupportedEncodingException {
        if (!uuidService.hasMatchingUUID(user, oneTimeToken)) {
            redirectAttributes.addFlashAttribute("message", "Invalid email or one-time-token" + (user != null ? " - click <a href=\"/sendUpdatePasswordEmail?email=" + URLEncoder.encode(user.getEmail(), "UTF-8") + "\">resend email</a> to receive a new email" : ""));
            redirectAttributes.addFlashAttribute("title", "Invalid Request");
            redirectAttributes.addFlashAttribute("error", true);
            return true;
        }
        return false;
    }

    @RequestMapping(value = "/updatePassword", method = RequestMethod.GET)
    public String updatePasswordForm(String email, String oneTimeToken, Model uiModel, RedirectAttributes redirectAttributes) throws UnsupportedEncodingException {
        if (hasInvalidToken(userDAO.findByEmail(email), oneTimeToken, redirectAttributes)) {
            return "redirect:/message";
        }
        uiModel.addAttribute("passwordPattern", User.PASSWORD_PATTERN);
        uiModel.addAttribute("environment", environment);
        uiModel.addAttribute("email", email);
        uiModel.addAttribute("oneTimeToken", oneTimeToken);
        return "page/user/updatePassword";
    }

    @Transactional
    @RequestMapping(value = "/updatePassword", method = RequestMethod.POST)
    public String updatePassword(String email, String password, String passwordConfirm, String oneTimeToken, Model uiModel, RedirectAttributes redirectAttributes) throws UnsupportedEncodingException {
        User user = userDAO.findByEmail(email);
        if (hasInvalidToken(user, oneTimeToken, redirectAttributes)) {
            return "redirect:/message";
        }
        boolean passwordFormatError = !PASSWORD_MATCHER.matcher(String.valueOf(password)).matches();
        boolean passwordsMatchError = !String.valueOf(password).equals(passwordConfirm);
        if (passwordFormatError || passwordsMatchError) {
            uiModel.addAttribute("passwordPattern", User.PASSWORD_PATTERN);
            uiModel.addAttribute("environment", environment);
            uiModel.addAttribute("email", email);
            uiModel.addAttribute("oneTimeToken", oneTimeToken);
            List<String> errors = new ArrayList<>();
            if (passwordFormatError) {
                errors.add(environment.getProperty("validation.user.password"));
            }
            if (passwordsMatchError) {
                errors.add(environment.getProperty("validation.user.passwordNonMatching"));
            }
            uiModel.addAttribute("validationErrors", errors);
            return "page/user/updatePassword";
        }
        userDAO.updatePassword(user.withPassword(passwordEncoder.encode(password)).resetLoginFailures());
        securityUserContext.setCurrentUser(user);
        return "redirect:/account";
    }

    @RequestMapping(value = "/account/updatePassword", method = RequestMethod.GET)
    public String authenticatedUpdatePasswordForm(Model uiModel) throws UnsupportedEncodingException {
        uiModel.addAttribute("passwordPattern", User.PASSWORD_PATTERN);
        uiModel.addAttribute("environment", environment);
        return "page/account/updatePassword";
    }

    @RequestMapping(value = "/account/updatePassword", method = RequestMethod.POST)
    public String authenticatedUpdatePassword(String existingPassword, String newPassword, String passwordConfirm, Model uiModel) throws UnsupportedEncodingException {
        User user = securityUserContext.getCurrentUser();
        boolean incorrectCredentials = !credentialValidation.checkCredentials(existingPassword, user);
        boolean passwordFormatError = !PASSWORD_MATCHER.matcher(String.valueOf(newPassword)).matches();
        boolean passwordsMatchError = !String.valueOf(newPassword).equals(passwordConfirm);
        if (incorrectCredentials || passwordFormatError || passwordsMatchError) {
            uiModel.addAttribute("passwordPattern", User.PASSWORD_PATTERN);
            uiModel.addAttribute("environment", environment);
            List<String> errors = new ArrayList<>();
            if (incorrectCredentials) {
                errors.add(environment.getProperty("validation.user.invalidCredentials"));
            }
            if (passwordFormatError) {
                errors.add(environment.getProperty("validation.user.password"));
            }
            if (passwordsMatchError) {
                errors.add(environment.getProperty("validation.user.passwordNonMatching"));
            }
            uiModel.addAttribute("validationErrors", errors);
            return "page/account/updatePassword";
        }
        userDAO.updatePassword(user.withPassword(passwordEncoder.encode(newPassword)));
        return "redirect:/account";
    }
}
