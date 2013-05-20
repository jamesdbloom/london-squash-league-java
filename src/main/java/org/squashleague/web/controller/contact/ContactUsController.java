package org.squashleague.web.controller.contact;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.squashleague.domain.account.User;
import org.squashleague.service.email.EmailService;
import org.squashleague.service.security.SpringSecurityUserContext;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Pattern;

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

    @RequestMapping(value = "/contact_us", method = RequestMethod.POST)  // todo add integration test to test all validation, etc
    public String sendMessage(@Pattern(regexp = "^[a-zA-Z0-9\\s._-]{1,500}$", message = "{validation.contact.message}") String contact_message,
                              @Pattern(regexp = "^[a-zA-Z0-9\\s._-]{1,500}$", message = "{validation.browser.user_agent}")
                              @RequestHeader("User-Agent") String userAgent,
                              HttpServletRequest request) {
        User user = userContext.getCurrentUser();
        String address = (user != null ? user.getEmail() : "unknown");
        String subject = "London Squash League - Contact Us";
        String formattedMessage = "<html><head><title>" + subject + "</title></head><body>" +
                "<p>A message has been submitted as follows:</p>\n" +
                "<p>Email: " + address + "</p>\n" +
                "<p>Message: " + contact_message + "</p>\n" +
                "<p>User Agent: " + userAgent + "</p>\n" +
                "<p>Remote Address: " + getIpAddress(request) + "</p>" +
                "</body></html>";
        emailService.sendMessage(address, new String[]{environment.getProperty("email.contact.address"), address}, subject, formattedMessage);
        return "redirect:/confirmation";
    }

    private static final String[] IP_FORWARDING_HEADERS = {"X-Ip", "X-Forwarded-For"};
    private static final java.util.regex.Pattern IP_PATTERN = java.util.regex.Pattern.compile("^\\[?[0-9\\.]+\\]?$"); // TODO improve pattern

    public String getIpAddress(HttpServletRequest request) {

        String ipInHeader = null;
        //check for presence of forwarding headers
        for (String header : IP_FORWARDING_HEADERS) {
            String ip = request.getHeader(header.trim());
            if (ip != null) {
                //so it exists but we need to make sure that it is not a comma separated list
                String[] tokens = ip.split(",");
                ipInHeader = tokens[0].trim();
                break;
            }
        }
        //if we didn't find one then get the remote IP
        if (ipInHeader == null) {
            ipInHeader = request.getRemoteAddr();
        }

        if(!IP_PATTERN.matcher(ipInHeader).find()) {
            return "";
        }

        return ipInHeader;
    }
}
