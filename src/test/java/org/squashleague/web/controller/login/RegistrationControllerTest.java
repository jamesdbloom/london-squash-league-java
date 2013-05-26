package org.squashleague.web.controller.login;

import com.eaio.uuid.UUID;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.squashleague.dao.account.UserDAO;
import org.squashleague.domain.account.MobilePrivacy;
import org.squashleague.domain.account.Role;
import org.squashleague.domain.account.User;
import org.squashleague.service.email.EmailService;
import org.squashleague.service.uuid.UUIDService;

import javax.servlet.http.HttpServletRequest;
import java.net.URL;
import java.net.URLEncoder;

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

    @Before
    public void setupFixture() {

        String oneTimeToken = new UUID().toString();
        when(uuidService.generateUUID()).thenReturn(oneTimeToken);

        user = mock(User.class);
        when(user.withOneTimeToken(same(oneTimeToken))).thenReturn(user);
        when(user.getOneTimeToken()).thenReturn(oneTimeToken);

        request = mock(HttpServletRequest.class);
        when(request.getLocalName()).thenReturn("www.london-squash-league.com");
        when(request.getLocalPort()).thenReturn(8080);

        uiModel = mock(Model.class);
    }

    @Test
    public void shouldRetrieveRegistrationForm() throws Exception {
        // given
        Model uiModel = mock(Model.class);

        // when
        String page = registrationController.registerForm(uiModel);

        // then
        verify(uiModel).addAttribute(eq("environment"), same(environment));
        verify(uiModel).addAttribute("mobilePrivacyOptions", MobilePrivacy.enumToFormOptionMap());
        verify(uiModel).addAttribute(eq("passwordPattern"), same(User.PASSWORD_PATTERN));
        verify(uiModel).addAttribute(eq("emailPattern"), same(User.EMAIL_PATTERN));
        verify(uiModel).addAttribute(eq("user"), eq(new User()));
        assertEquals("page/user/register", page);
    }

    @Test
    public void shouldRegisterAdminUserAndRedirect() throws Exception {
        // given
        when(user.getEmail()).thenReturn("jamesdbloom@gmail.com");
        when(user.withRole(same(Role.ROLE_ADMIN))).thenReturn(user);

        // when
        String page = registrationController.register(user, mock(BindingResult.class), request, uiModel);

        // then
        verify(user).withRole(same(Role.ROLE_ADMIN));
        verify(userDAO).register(eq(user));
        assertEquals("redirect:/login", page);
    }

    @Test
    public void shouldRegisterNonAdminUserAndRedirect() throws Exception {
        // given
        when(user.getEmail()).thenReturn("user@email.com");
        when(user.withRole(same(Role.ROLE_USER))).thenReturn(user);

        // when
        String page = registrationController.register(user, mock(BindingResult.class), request, uiModel);

        // then
        verify(user).withRole(same(Role.ROLE_USER));
        verify(userDAO).register(eq(user));
        assertEquals("redirect:/login", page);
    }

    @Test
    public void shouldSendEmail() throws Exception {
        // given
        when(user.getEmail()).thenReturn("user@email.com");
        when(user.withRole(same(Role.ROLE_USER))).thenReturn(user);

        // when
        registrationController.register(user, mock(BindingResult.class), request, uiModel);

        // then
        verify(emailService).sendRegistrationMessage(same(user), same(request));
    }

    @Test
    public void shouldAddBindingErrorsToModelAndForward() throws Exception {
        // given
        BindingResult bindingResult = mock(BindingResult.class);
        reset(bindingResult);
        when(bindingResult.hasErrors()).thenReturn(true);

        // when
        String page = registrationController.register(user, bindingResult, request, uiModel);

        // then
        verify(uiModel).addAttribute(eq("bindingResult"), same(bindingResult));
        verify(uiModel).addAttribute(eq("environment"), same(environment));
        verify(uiModel).addAttribute("mobilePrivacyOptions", MobilePrivacy.enumToFormOptionMap());
        verify(uiModel).addAttribute(eq("passwordPattern"), same(User.PASSWORD_PATTERN));
        verify(uiModel).addAttribute(eq("emailPattern"), same(User.EMAIL_PATTERN));
        verify(uiModel).addAttribute(eq("bindingResult"), same(bindingResult));
        verify(uiModel).addAttribute(eq("user"), same(user));
        assertEquals("page/user/register", page);
    }

}
