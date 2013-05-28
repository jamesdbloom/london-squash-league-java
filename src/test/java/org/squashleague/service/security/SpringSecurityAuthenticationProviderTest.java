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
import org.squashleague.dao.account.UserDAO;
import org.squashleague.domain.account.Role;
import org.squashleague.domain.account.User;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
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
        String[] roles = {"one", "two"};
        User user = new User()
                .withPassword("password")
                .withRoles(
                        new Role().withName(roles[0]),
                        new Role().withName(roles[1])
                );

        UsernamePasswordAuthenticationToken token = mock(UsernamePasswordAuthenticationToken.class);
        when(token.getName()).thenReturn(user.getEmail());
        when(token.getCredentials()).thenReturn(user.getPassword());
        when(userDAO.findByEmail(user.getEmail())).thenReturn(user);
        when(credentialValidation.credentialsMatch(user.getPassword(), user)).thenReturn(true);

        // when
        Authentication result = springSecurityAuthenticationProvider.authenticate(token);
        assertEquals(result.getAuthorities(), AuthorityUtils.createAuthorityList(roles));
    }

    @Test(expected = UsernameNotFoundException.class)
    public void shouldNotAuthenticateForPasswordDoesNotMatch() throws AuthenticationException {
        // given
        User user = new User()
                .withEmail("email")
                .withPassword("password");

        UsernamePasswordAuthenticationToken token = mock(UsernamePasswordAuthenticationToken.class);
        when(token.getName()).thenReturn(user.getEmail());
        when(token.getCredentials()).thenReturn(user.getPassword());

        when(userDAO.findByEmail(user.getEmail())).thenReturn(user);
        when(credentialValidation.credentialsMatch(user.getPassword(), user)).thenReturn(false);

        // when
        springSecurityAuthenticationProvider.authenticate(token);
    }

    @Test
    public void shouldSupportUsernamePasswordAuthenticationToken() {
        assertTrue(springSecurityAuthenticationProvider.supports(UsernamePasswordAuthenticationToken.class));
    }
}
