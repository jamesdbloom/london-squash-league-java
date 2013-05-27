package org.squashleague.service.security;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.squashleague.dao.account.UserDAO;
import org.squashleague.domain.account.User;

import javax.annotation.Resource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author jamesdbloom
 */
@RunWith(MockitoJUnitRunner.class)
public class SpringSecurityAuthenticationProviderTest {

    @Mock
    private UserDAO userDAO;
    @Mock
    private Environment environment;
    @Mock
    private CredentialValidation credentialValidation;
    @InjectMocks
    private SpringSecurityAuthenticationProvider springSecurityAuthenticationProvider = new SpringSecurityAuthenticationProvider();

    @Test
    public void shouldAuthenticate() throws AuthenticationException {
        // given
        String name = "name";
        String password = "password";
        String credentials = "credentials";
        String[] roles = {"roles"};

        User user = mock(User.class);
        when(user.getPassword()).thenReturn(password);
        when(user.getRoleNames()).thenReturn(roles);

        UsernamePasswordAuthenticationToken token = mock(UsernamePasswordAuthenticationToken.class);
        when(token.getName()).thenReturn(name);
        when(token.getCredentials()).thenReturn(credentials);
        when(userDAO.findByEmail(same(name))).thenReturn(user);
        when(credentialValidation.credentialsMatch(same(credentials), same(user))).thenReturn(true);

        // when
        Authentication result = springSecurityAuthenticationProvider.authenticate(token);
        assertEquals(result.getAuthorities(), AuthorityUtils.createAuthorityList(roles));
    }

    @Test(expected = UsernameNotFoundException.class)
    public void shouldNotAuthenticateForPasswordDoesNotMatch() throws AuthenticationException {
        // given
        String name = "name";
        String password = "password";
        String credentials = "credentials";
        String[] roles = {"roles"};

        User user = mock(User.class);
        when(user.getPassword()).thenReturn(password);
        when(user.getRoleNames()).thenReturn(roles);

        UsernamePasswordAuthenticationToken token = mock(UsernamePasswordAuthenticationToken.class);
        when(token.getName()).thenReturn(name);
        when(token.getCredentials()).thenReturn(credentials);

        when(userDAO.findByEmail(same(name))).thenReturn(user);
        when(credentialValidation.credentialsMatch(same(credentials), same(user))).thenReturn(false);

        // when
        springSecurityAuthenticationProvider.authenticate(token);
    }

    @Test
    public void shouldSupportUsernamePasswordAuthenticationToken() {
        assertTrue(springSecurityAuthenticationProvider.supports(UsernamePasswordAuthenticationToken.class));
    }
}
