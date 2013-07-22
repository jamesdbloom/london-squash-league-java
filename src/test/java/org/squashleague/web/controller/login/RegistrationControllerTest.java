package org.squashleague.web.controller.login;

import com.eaio.uuid.UUID;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.env.Environment;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.squashleague.dao.account.UserDAO;
import org.squashleague.domain.account.MobilePrivacy;
import org.squashleague.domain.account.User;
import org.squashleague.service.email.EmailService;
import org.squashleague.service.uuid.UUIDService;

import javax.servlet.http.HttpServletRequest;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * @author jamesdbloom
 */
@RunWith(MockitoJUnitRunner.class)
public class RegistrationControllerTest {

    @Mock
    private UserDAO userDAO;
    @Mock
    private Environment environment;
    @Mock
    private EmailService emailService;
    @Mock
    private UUIDService uuidService;
    @InjectMocks
    private RegistrationController registrationController = new RegistrationController();
    private User user;
    private HttpServletRequest request;
    private Model uiModel;
    private RedirectAttributes redirectAttributes;

    @Before
    public void setupFixture() {

        String oneTimeToken = new UUID().toString();
        when(uuidService.generateUUID()).thenReturn(oneTimeToken);

        user = new User().withOneTimeToken(oneTimeToken);

        request = mock(HttpServletRequest.class);
        when(request.getLocalName()).thenReturn("www.london-squash-league.com");
        when(request.getLocalPort()).thenReturn(8080);

        uiModel = mock(Model.class);
        redirectAttributes = mock(RedirectAttributes.class);
    }

    @Test
    public void shouldRetrieveRegistrationForm() throws Exception {
        // given
        Model uiModel = mock(Model.class);

        // when
        String page = registrationController.registerForm(uiModel);

        // then
        verify(uiModel).addAttribute("environment", environment);
        verify(uiModel).addAttribute("mobilePrivacyOptions", MobilePrivacy.enumToFormOptionMap());
        verify(uiModel).addAttribute("passwordPattern", User.PASSWORD_PATTERN);
        verify(uiModel).addAttribute("emailPattern", User.EMAIL_PATTERN);
        verify(uiModel).addAttribute("user", new User());
        assertEquals("page/user/register", page);
    }

    @Test
    public void shouldRegisterAdminUserAndRedirect() throws Exception {
        // given
        user.withEmail("jamesdbloom@gmail.com");

        // when
        String page = registrationController.register(user, mock(BindingResult.class), request, uiModel, redirectAttributes);

        // then
        verify(userDAO).register(user);
        verify(redirectAttributes).addFlashAttribute("message", "Your account has been created and an email has been sent to " + user.getEmail() + " with a link to create your password and login, please check your spam folder if you don't see the email within 5 minutes");
        verify(redirectAttributes).addFlashAttribute("title", "Account Created");
        assertEquals("redirect:/message", page);
    }

    @Test
    public void shouldRegisterNonAdminUserAndRedirect() throws Exception {
        // given
        user.withEmail("jamesdbloom@gmail.com");

        // when
        String page = registrationController.register(user, mock(BindingResult.class), request, uiModel, redirectAttributes);

        // then
        verify(userDAO).register(user);
        verify(redirectAttributes).addFlashAttribute("message", "Your account has been created and an email has been sent to " + user.getEmail() + " with a link to create your password and login, please check your spam folder if you don't see the email within 5 minutes");
        verify(redirectAttributes).addFlashAttribute("title", "Account Created");
        assertEquals("redirect:/message", page);
    }

    @Test
    public void shouldSendEmail() throws Exception {
        // given
        user.withEmail("jamesdbloom@gmail.com");

        // when
        registrationController.register(user, mock(BindingResult.class), request, uiModel, redirectAttributes);

        // then
        verify(emailService).sendRegistrationMessage(user, request);
    }

    @Test
    public void shouldAddBindingErrorsToModelAndForward() throws Exception {
        // given
        BindingResult bindingResult = mock(BindingResult.class);
        reset(bindingResult);
        when(bindingResult.hasErrors()).thenReturn(true);

        // when
        String page = registrationController.register(user, bindingResult, request, uiModel, redirectAttributes);

        // then
        verify(uiModel).addAttribute("bindingResult", bindingResult);
        verify(uiModel).addAttribute("environment", environment);
        verify(uiModel).addAttribute("mobilePrivacyOptions", MobilePrivacy.enumToFormOptionMap());
        verify(uiModel).addAttribute("passwordPattern", User.PASSWORD_PATTERN);
        verify(uiModel).addAttribute("emailPattern", User.EMAIL_PATTERN);
        verify(uiModel).addAttribute("bindingResult", bindingResult);
        verify(uiModel).addAttribute("user", user);
        assertEquals("page/user/register", page);
    }

}
