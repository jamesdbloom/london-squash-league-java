package org.squashleague.service.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.squashleague.domain.account.User;

/**
 * @author jamesdbloom
 */
@Component
public class SpringSecurityUserContext {

    public User getCurrentUser() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        if (authentication == null || authentication.getPrincipal().equals("anonymousUser")) {
            return null;
        }
        return (User) authentication.getPrincipal();
    }

    public void setCurrentUser(User user) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(user, user.getPassword(), AuthorityUtils.createAuthorityList(user.getRoleNames()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
