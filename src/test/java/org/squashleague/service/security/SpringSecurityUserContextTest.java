package org.squashleague.service.security;

import org.junit.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.squashleague.domain.account.User;

import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author jamesdbloom
 */
public class SpringSecurityUserContextTest {

    @Test
    public void shouldGetCurrentUser() {
        // given
        UsernamePasswordAuthenticationToken token = mock(UsernamePasswordAuthenticationToken.class);
        User user = mock(User.class);
        when(token.getPrincipal()).thenReturn(user);
        SecurityContextHolder.getContext().setAuthentication(token);

        // then
        assertSame(user, new SpringSecurityUserContext().getCurrentUser());
    }

    @Test
    public void shouldReturnNullIfTokenNull() {
        // given
        SecurityContextHolder.getContext().setAuthentication(null);

        // then
        assertNull(new SpringSecurityUserContext().getCurrentUser());
    }

    @Test
    public void shouldReturnNullIfUserNull() {
        // given
        UsernamePasswordAuthenticationToken token = mock(UsernamePasswordAuthenticationToken.class);
        when(token.getPrincipal()).thenReturn(null);
        SecurityContextHolder.getContext().setAuthentication(token);

        // then
        assertNull(new SpringSecurityUserContext().getCurrentUser());
    }

    @Test
    public void shouldReturnNullIfAnonymousUser() {
        // given
        UsernamePasswordAuthenticationToken token = mock(UsernamePasswordAuthenticationToken.class);
        when(token.getPrincipal()).thenReturn("anonymousUser");
        SecurityContextHolder.getContext().setAuthentication(token);

        // then
        assertNull(new SpringSecurityUserContext().getCurrentUser());
    }

    @Test
    public void shouldSetUser() {
        // given
        User user = mock(User.class);
        String password = "password";
        when(user.getPassword()).thenReturn(password);
        String[] roles = {"roles"};
        when(user.getRoleNames()).thenReturn(roles);

        // when
        new SpringSecurityUserContext().setCurrentUser(user);

        // then
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertSame(user, authentication.getPrincipal());
        assertSame(password, authentication.getCredentials());
        assertEquals(AuthorityUtils.createAuthorityList(roles), authentication.getAuthorities());
    }
}
