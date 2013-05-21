package org.squashleague.service.security;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.VerboseMockitoJUnitRunner;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.squashleague.dao.account.UserDAO;
import org.squashleague.domain.account.User;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author jamesdbloom
 */
@RunWith(VerboseMockitoJUnitRunner.class)
public class SpringSecurityAuthenticationProviderTest {

    @Mock
    private UserDAO userDAO;
    @Mock
    private PasswordEncoder passwordEncoder;
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
        when(userDAO.findByField(same(name), same(User.EMAIL_FIELD_NAME))).thenReturn(user);
        when(passwordEncoder.matches(same(credentials), same(password))).thenReturn(true);

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

        when(userDAO.findByField(same(name), same(User.EMAIL_FIELD_NAME))).thenReturn(user);
        when(passwordEncoder.matches(same(credentials), same(password))).thenReturn(false);

        // when
        Authentication result = springSecurityAuthenticationProvider.authenticate(token);
        assertEquals(result.getAuthorities(), AuthorityUtils.createAuthorityList(roles));
    }

    @Test(expected = UsernameNotFoundException.class)
    public void shouldNotAuthenticateForPasswordNull() throws AuthenticationException {
        // given
        String name = "name";
        String password = null;
        String credentials = "credentials";
        String[] roles = {"roles"};

        User user = mock(User.class);
        when(user.getPassword()).thenReturn(password);
        when(user.getRoleNames()).thenReturn(roles);

        UsernamePasswordAuthenticationToken token = mock(UsernamePasswordAuthenticationToken.class);
        when(token.getName()).thenReturn(name);
        when(token.getCredentials()).thenReturn(credentials);

        when(userDAO.findByField(same(name), same(User.EMAIL_FIELD_NAME))).thenReturn(user);

        // when
        springSecurityAuthenticationProvider.authenticate(token);
    }

    @Test(expected = UsernameNotFoundException.class)
    public void shouldNotAuthenticateForUserNotFound() throws AuthenticationException {
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

        when(userDAO.findByField(same(name), same(User.EMAIL_FIELD_NAME))).thenReturn(null);

        // when
        springSecurityAuthenticationProvider.authenticate(token);
    }

    @Test
    public void shouldSupportUsernamePasswordAuthenticationToken() {
        assertTrue(springSecurityAuthenticationProvider.supports(UsernamePasswordAuthenticationToken.class));
    }
}
