package org.squashleague.web.controller.contact;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.squashleague.domain.account.User;
import org.squashleague.service.email.EmailService;
import org.squashleague.service.http.RequestParser;
import org.squashleague.service.security.SpringSecurityUserContext;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * @author jamesdbloom
 */
@RunWith(MockitoJUnitRunner.class)
public class ContactUsControllerTest {

    @Mock
    private EmailService emailService;
    @Mock
    private SpringSecurityUserContext securityUserContext;
    @Mock
    private RequestParser requestParser;
    @InjectMocks
    private ContactUsController contactUsController = new ContactUsController();


    @Mock
    private HttpServletRequest request;
    private static final String IP = "127.0.0.1";
    private static final String EMAIL = "user@email.com";
    private static final String MESSAGE = "message";
    private static final String USER_AGENT = "userAgent";

    @Before
    public void setupMock() {
        when(securityUserContext.getCurrentUser()).thenReturn(new User().withEmail(EMAIL));
        when(requestParser.getIpAddress(request)).thenReturn(IP);
    }

    @Test
    public void shouldReturnCorrectContactUsFormAndAddUserToModel() {
        // given
        Model uiModel = mock(Model.class);

        // then
        assertEquals("page/contact/contact_us", contactUsController.contactUsPage(uiModel));
        verify(uiModel).addAttribute("user", securityUserContext.getCurrentUser());
    }

    @Test
    public void shouldSendEmailWithCorrectData() {
        // given
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);

        // then
        assertEquals("redirect:/message", contactUsController.sendMessage(MESSAGE, USER_AGENT, request, redirectAttributes));
        verify(redirectAttributes).addFlashAttribute("message", "Your message has been sent, a copy of your message has also been sent to " + securityUserContext.getCurrentUser().getEmail());
        verify(redirectAttributes).addFlashAttribute("title", "Message Sent");
        verify(emailService).sendContactUsMessage(MESSAGE, USER_AGENT, IP, EMAIL);
    }

    @Test
    public void shouldDisplayMessagePageIfMessageTooLarge() {
        // given
        byte[] randomBytes = new byte[4096];
        Arrays.fill(randomBytes, (byte) 'a');
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);

        // then
        assertEquals("redirect:/message", contactUsController.sendMessage(new String(randomBytes), "userAgent", mock(HttpServletRequest.class), redirectAttributes));
        verify(redirectAttributes).addFlashAttribute("message", "Your message was too large please try a shorter message");
        verify(redirectAttributes).addFlashAttribute("title", "Message Failure");
        verifyNoMoreInteractions(emailService);
    }

}
