package org.squashleague.web.controller.contact;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;
import org.squashleague.domain.account.User;
import org.squashleague.service.email.EmailService;
import org.squashleague.service.security.SpringSecurityUserContext;

import javax.annotation.Resource;

/**
 * @author jamesdbloom
 */
@Controller
public class ContactUsController {

    @Resource
    private EmailService emailService;
    @Resource
    private Environment environment;
    @Resource
    private SpringSecurityUserContext userContext;

    @RequestMapping(value = "/confirmation", method = RequestMethod.GET)
    public String confirmationPage(Model uiModel) {
        uiModel.addAttribute("user", userContext.getCurrentUser());
        return "page/confirmation";
    }

    @RequestMapping(value = "/contact_us", method = RequestMethod.GET)
    public String contactUsPage(Model uiModel) {
        uiModel.addAttribute("user", userContext.getCurrentUser());
        return "page/contact_us";
    }

    @RequestMapping(value = "/contact_us", method = RequestMethod.POST)
    public String sendMessage(String contact_message, WebRequest webRequest) {
        User user = userContext.getCurrentUser();
        String address = (user != null ? user.getEmail() : "unknown");
        String subject = "London Squash League - Contact Us";
        String formattedMessage = "<html><head><title>" + subject + "</title></head><body>" +
                "<p>A message has been submitted as follows:</p>\n" +
                "<p>Email: " + address + "</p>\n" +
                "<p>Message: " + contact_message + "</p>\n" +
                "<p>User Agent: " + webRequest.getHeader("User-Agent") /* TODO NEED VALIDATION */ + "</p>\n" +
                "<p>Browser Language: " + "en-GB,en;q=0.8,en-US;q=0.6" + "</p>\n" +
                "<p>Remote Address: " + "78.149.117.123" + "</p>" +
                "</body></html>";
        emailService.sendMessage(address, new String[]{environment.getProperty("email.contact.address"), address}, subject, formattedMessage);
        return "redirect:/confirmation";
    }
}
