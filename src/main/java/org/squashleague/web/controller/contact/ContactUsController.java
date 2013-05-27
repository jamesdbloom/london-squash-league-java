package org.squashleague.web.controller.contact;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.squashleague.domain.account.User;
import org.squashleague.service.email.EmailService;
import org.squashleague.service.http.RequestParser;
import org.squashleague.service.security.SpringSecurityUserContext;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.regex.Pattern;

/**
 * @author jamesdbloom
 */
@Controller
public class ContactUsController {

    private static final Pattern MESSAGE_PATTERN = Pattern.compile("^[a-zA-Z0-9\\p{Punct}\\s]{1,2048}$");
    private static final Pattern USER_AGENT_PATTERN = Pattern.compile("^[a-zA-Z0-9\\p{Punct}\\s]{1,1024}$");
    @Resource
    private EmailService emailService;
    @Resource
    private SpringSecurityUserContext securityUserContext;
    @Resource
    private RequestParser requestParser;

    @RequestMapping(value = "/contact_us", method = RequestMethod.GET)
    public String contactUsPage(Model uiModel) {
        uiModel.addAttribute("user", securityUserContext.getCurrentUser());
        return "page/contact/contact_us";
    }

    @RequestMapping(value = "/contact_us", method = RequestMethod.POST)
    public String sendMessage(@RequestParam("message") String message, @RequestHeader("User-Agent") String userAgent, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        if (MESSAGE_PATTERN.matcher(message).matches()) {
            User user = securityUserContext.getCurrentUser();
            String address = (user != null ? user.getEmail() : "unknown");
            emailService.sendContactUsMessage(message, (USER_AGENT_PATTERN.matcher(userAgent).matches() ? userAgent : "too long"), requestParser.getIpAddress(request), address);
            redirectAttributes.addFlashAttribute("message", "Your message has been sent, a copy of your message has also been sent to " + securityUserContext.getCurrentUser().getEmail());
            redirectAttributes.addFlashAttribute("title", "Message Sent");
            return "redirect:/message";
        } else {
            redirectAttributes.addFlashAttribute("message", "Your message was too large please try a shorter message");
            redirectAttributes.addFlashAttribute("title", "Message Failure");
            return "redirect:/message";
        }
    }
}
