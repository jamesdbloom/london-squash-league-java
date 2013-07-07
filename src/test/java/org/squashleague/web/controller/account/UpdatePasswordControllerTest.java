package org.squashleague.web.controller.account;

import com.eaio.uuid.UUID;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.squashleague.dao.account.UserDAO;
import org.squashleague.domain.account.User;
import org.squashleague.service.email.EmailService;
import org.squashleague.service.security.SpringSecurityUserContext;
import org.squashleague.service.uuid.UUIDService;
import org.squashleague.web.controller.account.UpdatePasswordController;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * @author jamesdbloom
 */
@RunWith(MockitoJUnitRunner.class)
public class UpdatePasswordControllerTest {

    @Mock
    private Environment environment;
    @Mock
    private UserDAO userDAO;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private SpringSecurityUserContext securityUserContext;
    @Mock
    private EmailService emailService;
    @Mock
    private UUIDService uuidService;
    @InjectMocks
    private UpdatePasswordController updatePasswordController = new UpdatePasswordController();
    private User user;
    private Model uiModel;

    @Before
    public void setupFixture() {
        String password = "abd123$%^";
        when(passwordEncoder.encode(password)).thenReturn(password);

        String oneTimeToken = new UUID().toString();

        user = mock(User.class);
        when(user.withPassword(password)).thenReturn(user);
        when(user.getPassword()).thenReturn(password);
        when(user.withOneTimeToken(oneTimeToken)).thenReturn(user);
        when(user.getOneTimeToken()).thenReturn(oneTimeToken);

        uiModel = mock(Model.class);
    }

    @Test
    public void shouldDisplayRetrievePasswordForm() throws Exception {
        // given
        Model uiModel = mock(Model.class);

        // then
        assertEquals("page/user/retrievePassword", updatePasswordController.retrievePasswordForm(uiModel));
        verify(uiModel).addAttribute("emailPattern", User.EMAIL_PATTERN);
        verify(uiModel).addAttribute("environment", environment);
    }

    @Test
    public void shouldDisplayUpdatePasswordForm() throws Exception {
        // given
        String email = "user@email.com";
        Model uiModel = mock(Model.class);
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);
        when(uuidService.hasMatchingUUID(user, user.getOneTimeToken())).thenReturn(true);
        when(userDAO.findByEmail(email)).thenReturn(user);

        // when
        String page = updatePasswordController.updatePasswordForm(email, user.getOneTimeToken(), uiModel, redirectAttributes);

        // then
        verify(uiModel).addAttribute("passwordPattern", User.PASSWORD_PATTERN);
        verify(uiModel).addAttribute("environment", environment);
        verify(uiModel).addAttribute("email", email);
        verify(uiModel).addAttribute("oneTimeToken", user.getOneTimeToken());
        assertEquals("page/user/updatePassword", page);
    }

    @Test
    public void shouldValidateTokenIncorrectWhenRetrievingUpdatePasswordForm() throws Exception {
        // given
        String email = "user@email.com";
        String oneTimeToken = "oneTimeToken";
        Model uiModel = mock(Model.class);
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);
        when(uuidService.hasMatchingUUID(user, user.getOneTimeToken())).thenReturn(false);

        // when
        String page = updatePasswordController.updatePasswordForm(email, oneTimeToken, uiModel, redirectAttributes);

        // then
        verify(redirectAttributes).addFlashAttribute(eq("message"), any(String.class));
        verify(redirectAttributes).addFlashAttribute("title", "Invalid Request");
        verify(redirectAttributes).addFlashAttribute("error", true);
        assertEquals("redirect:/message", page);
    }

    @Test
    public void shouldUpdatePassword() throws UnsupportedEncodingException {
        // given
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);
        when(user.getEmail()).thenReturn("user@email.com");
        when(user.getOneTimeToken()).thenReturn(new UUID().toString());
        when(userDAO.findByEmail(user.getEmail())).thenReturn(user);
        when(uuidService.hasMatchingUUID(user, user.getOneTimeToken())).thenReturn(true);

        // when
        String page = updatePasswordController.updatePassword(user.getEmail(), user.getPassword(), user.getPassword(), user.getOneTimeToken(), uiModel, redirectAttributes);

        // then
        verify(passwordEncoder).encode(user.getPassword());
        verify(user, times(3)).withPassword(user.getPassword());
        verify(userDAO).updatePassword(user);
        verify(securityUserContext).setCurrentUser(user);
        assertEquals("redirect:/account", page);

    }

    @Test
    public void shouldValidateTokenIncorrectWhenUpdatingPasswords() throws Exception {
        // given
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);
        when(user.getEmail()).thenReturn("user@email.com");
        when(user.getOneTimeToken()).thenReturn(new UUID().toString());
        when(userDAO.findByEmail(user.getEmail())).thenReturn(user);
        when(uuidService.hasMatchingUUID(user, "wrong_token")).thenReturn(false);

        // when
        String page = updatePasswordController.updatePassword(user.getEmail(), user.getPassword(), user.getPassword(), "wrong_token", uiModel, redirectAttributes);

        // then
        verify(redirectAttributes).addFlashAttribute("message", "Invalid email or one-time-token - click <a href=\"/sendUpdatePasswordEmail?email=user%40email.com\">resend email</a> to receive a new email");
        verify(redirectAttributes).addFlashAttribute("title", "Invalid Request");
        verify(redirectAttributes).addFlashAttribute("error", true);
        assertEquals("redirect:/message", page);
    }

    @Test
    public void shouldValidatePasswordCorrectFormat() throws Exception {
        // given
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);
        when(environment.getProperty("validation.user.password")).thenReturn("password");

        when(user.getEmail()).thenReturn("user@email.com");
        when(user.getOneTimeToken()).thenReturn(new UUID().toString());
        when(userDAO.findByEmail(user.getEmail())).thenReturn(user);
        when(uuidService.hasMatchingUUID(user, user.getOneTimeToken())).thenReturn(true);

        // when
        String page = updatePasswordController.updatePassword(user.getEmail(), "not_correct_format", "not_correct_format", user.getOneTimeToken(), uiModel, redirectAttributes);

        // then
        verify(uiModel).addAttribute("passwordPattern", User.PASSWORD_PATTERN);
        verify(uiModel).addAttribute("environment", environment);
        verify(uiModel).addAttribute("email", "user@email.com");
        verify(uiModel).addAttribute("oneTimeToken", user.getOneTimeToken());
        verify(uiModel).addAttribute("validationErrors", Arrays.asList(environment.getProperty("validation.user.password")));
        assertEquals("page/user/updatePassword", page);
    }

    @Test
    public void shouldValidatePasswordsDoNotMatch() throws Exception {
        // given
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);
        when(environment.getProperty("validation.user.passwordNonMatching")).thenReturn("passwordNonMatching");

        when(user.getEmail()).thenReturn("user@email.com");
        when(user.getOneTimeToken()).thenReturn(new UUID().toString());
        when(userDAO.findByEmail(user.getEmail())).thenReturn(user);
        when(uuidService.hasMatchingUUID(user, user.getOneTimeToken())).thenReturn(true);

        // when
        String page = updatePasswordController.updatePassword(user.getEmail(), "abc123$%^", "123abc$%^", user.getOneTimeToken(), uiModel, redirectAttributes);

        // then
        verify(uiModel).addAttribute("passwordPattern", User.PASSWORD_PATTERN);
        verify(uiModel).addAttribute("environment", environment);
        verify(uiModel).addAttribute("email", "user@email.com");
        verify(uiModel).addAttribute("oneTimeToken", user.getOneTimeToken());
        verify(uiModel).addAttribute("validationErrors", Arrays.asList(environment.getProperty("validation.user.passwordNonMatching")));
        assertEquals("page/user/updatePassword", page);
    }

}
