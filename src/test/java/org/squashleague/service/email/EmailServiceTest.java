package org.squashleague.service.email;

import org.junit.Before;
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
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.squashleague.domain.account.User;

import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

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
    @Mock
    private ThreadPoolTaskExecutor taskExecutor;
    @InjectMocks
    private EmailService emailService = new EmailService();
    private ArgumentCaptor<Runnable> runnableArgumentCaptor;
    private ArgumentCaptor<MimeMessagePreparator> preparatorArgumentCaptor;
    private MimeMessage mimeMessage;

    @Before
    public void setupMocks() {
        runnableArgumentCaptor = ArgumentCaptor.forClass(Runnable.class);
        doNothing().when(taskExecutor).execute(runnableArgumentCaptor.capture());

        preparatorArgumentCaptor = ArgumentCaptor.forClass(MimeMessagePreparator.class);
        doNothing().when(mailSender).send(preparatorArgumentCaptor.capture());

        mimeMessage = new JavaMailSenderImpl().createMimeMessage();
    }

    @Test
    public void shouldSendEmail() throws Exception {
        // given
        String from = "from@email.com";
        String[] to = {"to@first-email.com", "to@second-email.com"};
        String subject = "subject";
        String msg = "<html>msg</html>";

        // when
        emailService.sendMessage(from, to, subject, msg);
        runnableArgumentCaptor.getValue().run();
        preparatorArgumentCaptor.getValue().prepare(mimeMessage);

        // then
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

        // when
        emailService.sendContactUsMessage(message, userAgent, ip, from);
        runnableArgumentCaptor.getValue().run();
        preparatorArgumentCaptor.getValue().prepare(mimeMessage);

        // then
        assertEquals(from, mimeMessage.getFrom()[0].toString());
        assertEquals(leagueEmail, mimeMessage.getAllRecipients()[0].toString());
        assertEquals(from, mimeMessage.getAllRecipients()[1].toString());
        assertEquals(subject, mimeMessage.getSubject());
        assertEquals("<html><head><title>" + subject + "</title></head><body>\n" +
                "<p>A message has been submitted as follows:</p>\n" +
                "<p><b>Email:</b> " + from + "</p>\n" +
                "<p><b>User Agent:</b> " + userAgent + "</p>\n" +
                "<p><b>Remote Address:</b> " + ip + "</p>\n" +
                "<div style=\"width:100%; height: 1.5em;\"></div>\n" +
                "<p><b>Message:</b> " + message + "</p>\n" +
                "</body></html>", mimeMessage.getContent().toString());
    }

    @Test
    public void shouldSendRegistrationEmail() throws Exception {
        // given
        String token = "token";
        String email = "to@email.com";
        User user = new User()
                .withEmail(email)
                .withOneTimeToken(token);

        String hostName = "hostName";
        int port = 666;
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getLocalName()).thenReturn(hostName);
        when(request.getLocalPort()).thenReturn(port);

        String leagueEmail = "info@squash-league.com";
        when(environment.getProperty("email.contact.address")).thenReturn(leagueEmail);

        // when
        emailService.sendRegistrationMessage(user, request);
        runnableArgumentCaptor.getValue().run();
        preparatorArgumentCaptor.getValue().prepare(mimeMessage);

        // then
        String subject = EmailService.LONDON_SQUASH_LEAGUE_SUBJECT_PREFIX + "New Registration";
        assertEquals(leagueEmail, mimeMessage.getFrom()[0].toString());
        assertEquals(email, mimeMessage.getAllRecipients()[0].toString());
        assertEquals(subject, mimeMessage.getSubject());

        assertEquals("<html><head><title>" + subject + "</title></head><body>\n" +
                "<h1>" + subject + "</h1>\n" +
                "<p>A new has just been registered for " + email + "</p>\n" +
                "<p>To validate this email address please click on the following link <a href=https://" + hostName + ":" + port + "/updatePassword?email=to%40email.com&oneTimeToken=" + token + ">https://" + hostName + ":" + port + "/updatePassword?email=to%40email.com&oneTimeToken=" + token + "</a></p>\n" +
                "</body></html>", mimeMessage.getContent().toString());
    }
}
