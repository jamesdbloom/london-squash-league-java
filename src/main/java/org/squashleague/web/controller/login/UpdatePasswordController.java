package org.squashleague.web.controller.login;

import com.eaio.uuid.UUID;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.squashleague.dao.account.UserDAO;
import org.squashleague.domain.account.User;
import org.squashleague.service.email.EmailService;
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

    @RequestMapping(value = "/retrievePassword", method = RequestMethod.GET)
    public String retrievePasswordForm(Model uiModel) throws MalformedURLException, UnsupportedEncodingException {
        uiModel.addAttribute("emailPattern", User.EMAIL_PATTERN);
        uiModel.addAttribute("environment", environment);
        return "page/user/retrievePassword";
    }

    @RequestMapping(value = "/sendUpdatePasswordEmail", method = {RequestMethod.GET, RequestMethod.POST})
    public String sendUpdatePasswordEmail(String email, final HttpServletRequest request, final RedirectAttributes redirectAttributes) throws MalformedURLException, UnsupportedEncodingException {
        final User user = userDAO.findByEmail(email);
        if (user != null) {
            userDAO.updateOneTimeToken(user.withOneTimeToken(uuidService.generateUUID()));
            emailService.sendRegistrationMessage(user, request);
        }
        redirectAttributes.addFlashAttribute("message", "Email has been sent");
        redirectAttributes.addFlashAttribute("title", "Message Sent");
        return "redirect:/message";
    }

    private boolean hasInvalidToken(User user, String oneTimeToken, RedirectAttributes redirectAttributes) throws UnsupportedEncodingException {
        if (!uuidService.hasMatchingUUID(user, oneTimeToken)) {
            redirectAttributes.addFlashAttribute("message", "Invalid email or one-time-token " + (user != null ? " - click <a href=\"/sendUpdatePasswordEmail?email=" + URLEncoder.encode(user.getEmail(), "UTF-8") + "\">resend email</a> to receive a new email" : ""));
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

    @RequestMapping(value = "/updatePassword", method = RequestMethod.POST)
    public String updatePassword(String email, String passwordOne, String passwordTwo, String oneTimeToken, Model uiModel, RedirectAttributes redirectAttributes) throws UnsupportedEncodingException {
        User user = userDAO.findByEmail(email);
        if (hasInvalidToken(user, oneTimeToken, redirectAttributes)) {
            return "redirect:/message";
        }
        boolean passwordFormatError = !PASSWORD_MATCHER.matcher(String.valueOf(passwordOne)).matches();
        boolean passwordsMatchError = !String.valueOf(passwordOne).equals(passwordTwo);
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
        userDAO.updatePassword(user.withPassword(passwordEncoder.encode(passwordOne)));
        securityUserContext.setCurrentUser(user);
        return "redirect:/";
    }
}
