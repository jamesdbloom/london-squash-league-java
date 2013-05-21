package org.squashleague.service.security;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    @Resource
    private PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;
        User user = userDAO.findByField(token.getName(), User.EMAIL_FIELD_NAME);
        if (user == null || token.getCredentials() == null || !passwordEncoder.matches((CharSequence) token.getCredentials(), user.getPassword())) {
            throw new UsernameNotFoundException("Invalid username & password combination");
        }
        return new UsernamePasswordAuthenticationToken(user, user.getPassword(), AuthorityUtils.createAuthorityList(user.getRoleNames()));
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.equals(authentication);
    }
}
