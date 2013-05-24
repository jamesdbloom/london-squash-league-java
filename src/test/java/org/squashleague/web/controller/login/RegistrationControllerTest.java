package org.squashleague.web.controller.login;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.squashleague.dao.account.UserDAO;
import org.squashleague.domain.account.MobilePrivacy;
import org.squashleague.domain.account.Role;
import org.squashleague.domain.account.User;
import org.squashleague.service.email.EmailService;
import org.squashleague.service.security.SpringSecurityUserContext;
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
    private SpringSecurityUserContext securityUserContext;
    @Mock
    private Environment environment;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private EmailService emailService;
    @Mock
    private UUIDService uuidService;
    @InjectMocks
    private RegistrationController registrationController = new RegistrationController();
    private HttpServletRequest request;
    private Model uiModel;
    private String password;
    private User user;

    @Before
    public void setupFixture() {
        password = "abd123$%^";
        when(passwordEncoder.encode(same(password))).thenReturn(password);

        String oneTimeToken = "oneTimeToken";
        when(uuidService.generateUUID()).thenReturn(oneTimeToken);

        user = mock(User.class);
        when(user.withPassword(same(password))).thenReturn(user);
        when(user.withOneTimeToken(same(oneTimeToken))).thenReturn(user);

        request = mock(HttpServletRequest.class);
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
        String page = registrationController.register(user, mock(BindingResult.class), password, password, request, uiModel);

        // then
        verify(passwordEncoder).encode(same(password));
        verify(user).withRole(same(Role.ROLE_ADMIN));
        verify(user).withPassword(same(password));
        verify(userDAO).register(eq(user));
        verify(securityUserContext).setCurrentUser(eq(user));
        assertEquals("redirect:/", page);
    }

    @Test
    public void shouldRegisterNonAdminUserAndRedirect() throws Exception {
        // given
        when(user.getEmail()).thenReturn("user@email.com");
        when(user.withRole(same(Role.ROLE_USER))).thenReturn(user);

        // when
        String page = registrationController.register(user, mock(BindingResult.class), password, password, request, uiModel);

        // then
        verify(passwordEncoder).encode(same(password));
        verify(user).withRole(same(Role.ROLE_USER));
        verify(user).withPassword(same(password));
        verify(userDAO).register(eq(user));
        verify(securityUserContext).setCurrentUser(eq(user));
        assertEquals("redirect:/", page);
    }

    @Test
    public void shouldSendEmail() throws Exception {
        // given
        when(user.getEmail()).thenReturn("user@email.com");
        when(user.withRole(same(Role.ROLE_USER))).thenReturn(user);
        when(request.getLocalName()).thenReturn("www.london-squash-league.com");

        // when
        registrationController.register(user, mock(BindingResult.class), password, password, request, uiModel);


        // then
        verify(emailService).sendRegistrationMessage(
                user.getEmail(),
                new URL(
                        "https",
                        request.getLocalName(),
                        URLEncoder.encode("validate?user=" + user.getEmail() + "&token=" + user.getOneTimeToken(), "UTF-8")
                )
        );
    }

    @Test
    public void shouldAddBindingErrorsToModelAndForward() throws Exception {
        // given
        BindingResult bindingResult = mock(BindingResult.class);
        reset(bindingResult);
        when(bindingResult.hasErrors()).thenReturn(true);

        // when
        String page = registrationController.register(user, bindingResult, password, password, request, uiModel);

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

    @Test
    public void shouldValidatePasswordCorrectFormat() throws Exception {
        // given
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);
        String password = "not_correct_format";
        String errorMessage = "passwordNotCorrectFormat";
        when(environment.getProperty("validation.user.password")).thenReturn(errorMessage);

        // when
        String page = registrationController.register(user, bindingResult, password, password, request, uiModel);

        // then
        verify(uiModel).addAttribute(eq("environment"), same(environment));
        verify(uiModel).addAttribute("mobilePrivacyOptions", MobilePrivacy.enumToFormOptionMap());
        verify(uiModel).addAttribute(eq("passwordPattern"), same(User.PASSWORD_PATTERN));
        verify(uiModel).addAttribute(eq("emailPattern"), same(User.EMAIL_PATTERN));
        verify(bindingResult).addError(new ObjectError("user", errorMessage));
        verify(uiModel).addAttribute(eq("bindingResult"), same(bindingResult));
        verify(uiModel).addAttribute(eq("user"), same(user));
        assertEquals("page/user/register", page);
    }

    @Test
    public void shouldValidatePasswordsDoNotMatch() throws Exception {
        // given
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);
        String errorMessage = "passwordNonMatching";
        when(environment.getProperty("validation.user.passwordNonMatching")).thenReturn(errorMessage);

        // when
        String page = registrationController.register(user, bindingResult, "password_one", "password_two", request, uiModel);

        // then
        verify(uiModel).addAttribute(eq("environment"), same(environment));
        verify(uiModel).addAttribute("mobilePrivacyOptions", MobilePrivacy.enumToFormOptionMap());
        verify(uiModel).addAttribute(eq("passwordPattern"), same(User.PASSWORD_PATTERN));
        verify(uiModel).addAttribute(eq("emailPattern"), same(User.EMAIL_PATTERN));
        verify(bindingResult).addError(new ObjectError("user", errorMessage));
        verify(uiModel).addAttribute(eq("bindingResult"), same(bindingResult));
        verify(uiModel).addAttribute(eq("user"), same(user));
        assertEquals("page/user/register", page);
    }

}
