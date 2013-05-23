package org.squashleague.web.controller.login;

import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.squashleague.dao.account.RoleDAO;
import org.squashleague.dao.account.UserDAO;
import org.squashleague.domain.account.MobilePrivacy;
import org.squashleague.domain.account.Role;
import org.squashleague.domain.account.User;
import org.squashleague.service.security.SpringSecurityUserContext;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

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
    @InjectMocks
    private RegistrationController registrationController = new RegistrationController();

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
        Model uiModel = mock(Model.class);
        User user = mock(User.class);
        when(user.getEmail()).thenReturn("jamesdbloom@gmail.com");
        String password = "abd123$%^";
        when(passwordEncoder.encode(same(password))).thenReturn(password);
        when(user.withRole(same(Role.ROLE_ADMIN))).thenReturn(user);
        when(user.withPassword(same(password))).thenReturn(user);

        // when
        String page = registrationController.register(user, mock(BindingResult.class), password, password, uiModel);

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
        Model uiModel = mock(Model.class);
        User user = mock(User.class);
        when(user.getEmail()).thenReturn("user@email.com");
        String password = "abd123$%^";
        when(passwordEncoder.encode(same(password))).thenReturn(password);
        when(user.withRole(same(Role.ROLE_USER))).thenReturn(user);
        when(user.withPassword(same(password))).thenReturn(user);

        // when
        String page = registrationController.register(user, mock(BindingResult.class), password, password, uiModel);

        // then
        verify(passwordEncoder).encode(same(password));
        verify(user).withRole(same(Role.ROLE_USER));
        verify(user).withPassword(same(password));
        verify(userDAO).register(eq(user));
        verify(securityUserContext).setCurrentUser(eq(user));
        assertEquals("redirect:/", page);
    }

    @Test
    public void shouldAddBindingErrorsToModelAndForward() throws Exception {
        // given
        Model uiModel = mock(Model.class);
        User user = new User();
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);
        String password = "abd123$%^";

        // when
        String page = registrationController.register(user, bindingResult, password, password, uiModel);

        // then
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
        Model uiModel = mock(Model.class);
        User user = new User();
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);
        String password = "not_correct_format";
        String errorMessage = "passwordNotCorrectFormat";
        when(environment.getProperty("validation.user.password")).thenReturn(errorMessage);

        // when
        String page = registrationController.register(user, bindingResult, password, password, uiModel);

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
        Model uiModel = mock(Model.class);
        User user = new User();
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);
        String errorMessage = "passwordNonMatching";
        when(environment.getProperty("validation.user.passwordNonMatching")).thenReturn(errorMessage);

        // when
        String page = registrationController.register(user, bindingResult, "password_one", "password_two", uiModel);

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
