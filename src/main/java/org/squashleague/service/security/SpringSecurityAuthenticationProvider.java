package org.squashleague.service.security;

import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
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
    private Environment environment;
    @Resource
    private CredentialValidation credentialValidation;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;
        User user = userDAO.findByEmail(token.getName());
        if (user != null && user.getLoginFailures() >= environment.getProperty("login.failures.maximum", Integer.class)) {
            throw new LockedException(environment.getProperty("validation.user.maxFailures"));
        } else if (!credentialValidation.credentialsMatch((CharSequence) token.getCredentials(), user)) {
            if (user != null) {
                userDAO.incrementLoginFailures(user);
            }
            throw new UsernameNotFoundException(environment.getProperty("validation.user.invalidCredentials"));
        }
        userDAO.resetLoginFailures(user);
        return new UsernamePasswordAuthenticationToken(user, user.getPassword(), AuthorityUtils.createAuthorityList(user.getRoleNames()));
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.equals(authentication);
    }
}
