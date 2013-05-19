package org.squashleague.service.security;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.squashleague.dao.account.UserDAO;
import org.squashleague.domain.account.User;

import javax.annotation.Resource;

/**
 * @author jamesdbloom
 */
@Component
public class SpringSecurityAuthenticationProvider implements AuthenticationProvider {

    @Resource
    private UserDAO userDAO;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;
        User user = userDAO.findByEmail(token.getName());
        if (user == null || !user.getPassword().equals(token.getCredentials())) {
            throw new UsernameNotFoundException("Invalid username/password");
        }
        return new UsernamePasswordAuthenticationToken(user, user.getPassword(), AuthorityUtils.createAuthorityList(user.getRoleNames()));
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.equals(authentication);
    }
}
