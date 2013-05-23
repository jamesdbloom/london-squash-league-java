package org.squashleague.service.email;

import com.google.common.annotations.VisibleForTesting;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * @author jamesdbloom
 */
@Component
public class EmailService {

    @Resource
    private JavaMailSender mailSender;
    @Resource
    private Environment environment;

    @VisibleForTesting
    @PreAuthorize("isAuthenticated()")
    protected void sendMessage(final String from, final String[] to, final String subject, final String msg) {
        mailSender.send(new MimeMessagePreparator() {
            public void prepare(MimeMessage mimeMessage) throws MessagingException {
                MimeMessageHelper message = new MimeMessageHelper(mimeMessage, false, "UTF-8");
                message.setFrom(from);
                message.setTo(to);
                message.setSubject(subject);
                message.setText(msg, true);
            }
        });
    }

    public void sendContactUsMessage(String message, String userAgent, String ip, String subject, String address) {
        String formattedMessage = "<html><head><title>" + subject + "</title></head><body>" +
                "<p>A message has been submitted as follows:</p>\n" +
                "<p>Email: " + address + "</p>\n" +
                "<p>Message: " + message + "</p>\n" +
                "<p>User Agent: " + userAgent + "</p>\n" +
                "<p>Remote Address: " + ip + "</p>" +
                "</body></html>";
        sendMessage(address, new String[]{environment.getProperty("email.contact.address"), address}, subject, formattedMessage);
    }
}
