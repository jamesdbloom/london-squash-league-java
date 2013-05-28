package org.squashleague.service.security;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.squashleague.domain.account.User;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

/**
 * @author jamesdbloom
 */
@RunWith(MockitoJUnitRunner.class)
public class CredentialValidationTest {

    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private CredentialValidation credentialValidation = new CredentialValidation();

    private final User user = new User().withPassword("password");

    @Test
    public void shouldAuthenticate() throws AuthenticationException {
        // given
        when(passwordEncoder.matches(user.getPassword(), user.getPassword())).thenReturn(true);

        // then
        assertTrue(credentialValidation.credentialsMatch(user.getPassword(), user));
    }

    @Test
    public void shouldNotAuthenticateForPasswordDoesNotMatch() throws AuthenticationException {
        // given
        when(passwordEncoder.matches(user.getPassword(), user.getPassword())).thenReturn(false);

        // then
        assertFalse(credentialValidation.credentialsMatch(user.getPassword(), user));
    }

    @Test
    public void shouldNotAuthenticateForPasswordNull() throws AuthenticationException {
        // given
        when(passwordEncoder.matches(user.getPassword(), user.getPassword())).thenReturn(true);

        // then
        assertFalse(credentialValidation.credentialsMatch(null, user));
    }

    @Test
    public void shouldNotAuthenticateForUserNotFound() throws AuthenticationException {
        // given
        String password = "password";

        when(passwordEncoder.matches(password, password)).thenReturn(true);

        // then
        assertFalse(credentialValidation.credentialsMatch(password, null));
    }
}
