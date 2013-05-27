package org.squashleague.web.controller.contact;

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
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.same;
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

    @Test
    public void shouldReturnCorrectContactUsFormAndAddUserToModel() {
        // given
        Model uiModel = mock(Model.class);
        User user = mock(User.class);
        when(securityUserContext.getCurrentUser()).thenReturn(user);

        // then
        assertEquals("page/contact/contact_us", contactUsController.contactUsPage(uiModel));
        verify(uiModel).addAttribute(eq("user"), same(user));
    }

    @Test
    public void shouldSendEmailWithCorrectData() {
        // given
        User user = mock(User.class);
        String address = "user@email.com";
        when(securityUserContext.getCurrentUser()).thenReturn(user);
        when(user.getEmail()).thenReturn(address);

        String ip = "127.0.0.1";
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(requestParser.getIpAddress(same(request))).thenReturn(ip);

        String message = "message";
        String userAgent = "userAgent";

        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);

        // then
        assertEquals("redirect:/message", contactUsController.sendMessage(message, userAgent, request, redirectAttributes));
        verify(redirectAttributes).addFlashAttribute("message", "Your message has been sent, a copy of your message has also been sent to " + securityUserContext.getCurrentUser().getEmail());
        verify(redirectAttributes).addFlashAttribute("title", "Message Sent");
        verify(emailService).sendContactUsMessage(same(message), same(userAgent), same(ip), same(address));
    }

    @Test
    public void shouldDisplayMessagePageIfMessageTooLarge() {
        // given
        User user = mock(User.class);
        String address = "user@email.com";
        when(securityUserContext.getCurrentUser()).thenReturn(user);
        when(user.getEmail()).thenReturn(address);

        String ip = "127.0.0.1";
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(requestParser.getIpAddress(same(request))).thenReturn(ip);

        byte[] randomBytes = new byte[4096];
        Arrays.fill(randomBytes, (byte) 'a');
        String userAgent = "userAgent";
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);

        // then
        assertEquals("redirect:/message", contactUsController.sendMessage(new String(randomBytes), userAgent, request, redirectAttributes));
        verify(redirectAttributes).addFlashAttribute("message", "Your message was too large please try a shorter message");
        verify(redirectAttributes).addFlashAttribute("title", "Message Failure");
        verifyNoMoreInteractions(emailService);
    }

}
