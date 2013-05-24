package org.squashleague.service.email;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessagePreparator;

import javax.mail.internet.MimeMessage;
import java.net.URI;
import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * @author jamesdbloom
 */
@RunWith(MockitoJUnitRunner.class)
public class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;
    @Mock
    private Environment environment;
    @InjectMocks
    private EmailService emailService = new EmailService();

    @Test
    public void shouldSendEmail() throws Exception {
        // given
        String from = "from@email.com";
        String[] to = {"to@first-email.com", "to@second-email.com"};
        String subject = "subject";
        String msg = "<html>msg</html>";
        ArgumentCaptor<MimeMessagePreparator> preparatorArgumentCaptor = ArgumentCaptor.forClass(MimeMessagePreparator.class);
        doNothing().when(mailSender).send(preparatorArgumentCaptor.capture());

        MimeMessage mimeMessage = new JavaMailSenderImpl().createMimeMessage();

        // when
        emailService.sendMessage(from, to, subject, msg);

        // then
        MimeMessagePreparator result = preparatorArgumentCaptor.getValue();
        result.prepare(mimeMessage);

        assertEquals(from, mimeMessage.getFrom()[0].toString());
        assertEquals(to[0], mimeMessage.getAllRecipients()[0].toString());
        assertEquals(to[1], mimeMessage.getAllRecipients()[1].toString());
        assertEquals(subject, mimeMessage.getSubject());
        assertEquals(msg, mimeMessage.getContent().toString());
    }

    @Test
    public void shouldSendContactUsEmail() throws Exception {
        // given
        String message = "msg";
        String userAgent = "userAgent";
        String ip = "ip";
        String subject = EmailService.LONDON_SQUASH_LEAGUE_SUBJECT_PREFIX + "Contact Us";
        String from = "from@email.com";
        String leagueEmail = "info@squash-league.com";
        when(environment.getProperty("email.contact.address")).thenReturn(leagueEmail);

        ArgumentCaptor<MimeMessagePreparator> preparatorArgumentCaptor = ArgumentCaptor.forClass(MimeMessagePreparator.class);
        doNothing().when(mailSender).send(preparatorArgumentCaptor.capture());
        MimeMessage mimeMessage = new JavaMailSenderImpl().createMimeMessage();

        // when
        emailService.sendContactUsMessage(message, userAgent, ip, from);

        // then
        MimeMessagePreparator result = preparatorArgumentCaptor.getValue();
        result.prepare(mimeMessage);

        assertEquals(from, mimeMessage.getFrom()[0].toString());
        assertEquals(leagueEmail, mimeMessage.getAllRecipients()[0].toString());
        assertEquals(from, mimeMessage.getAllRecipients()[1].toString());
        assertEquals(subject, mimeMessage.getSubject());
        assertEquals("<html><head><title>" + subject + "</title></head><body>" +
                "<p>A message has been submitted as follows:</p>\n" +
                "<p>Email: " + from + "</p>\n" +
                "<p>Message: " + message + "</p>\n" +
                "<p>User Agent: " + userAgent + "</p>\n" +
                "<p>Remote Address: " + ip + "</p>" +
                "</body></html>", mimeMessage.getContent().toString());
    }

    @Test
    public void shouldSendRegistrationEmail() throws Exception {
        // given
        String subject = EmailService.LONDON_SQUASH_LEAGUE_SUBJECT_PREFIX + "New Registration";
        String to = "to@email.com";
        URL url = new URL("http", "www.london-squash-league.com", "path");
        String leagueEmail = "info@squash-league.com";
        when(environment.getProperty("email.contact.address")).thenReturn(leagueEmail);

        ArgumentCaptor<MimeMessagePreparator> preparatorArgumentCaptor = ArgumentCaptor.forClass(MimeMessagePreparator.class);
        doNothing().when(mailSender).send(preparatorArgumentCaptor.capture());
        MimeMessage mimeMessage = new JavaMailSenderImpl().createMimeMessage();

        // when
        emailService.sendRegistrationMessage(to, url);

        // then
        MimeMessagePreparator result = preparatorArgumentCaptor.getValue();
        result.prepare(mimeMessage);

        assertEquals(leagueEmail, mimeMessage.getFrom()[0].toString());
        assertEquals(to, mimeMessage.getAllRecipients()[0].toString());
        assertEquals(subject, mimeMessage.getSubject());
        assertEquals("<html><head><title>" + subject + "</title></head><body>" +
                "<p>A new has just been registered for " + to + "</p>\n" +
                "<p>To validate this email please click on the following link <a href=" + url + ">" + url + "</a></p>\n" +
                "</body></html>", mimeMessage.getContent().toString());
    }
}
