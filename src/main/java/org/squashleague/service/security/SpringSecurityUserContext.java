package org.squashleague.service.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.squashleague.dao.account.UserDAO;
import org.squashleague.domain.account.Role;
import org.squashleague.domain.account.User;

import java.util.List;

/**
 * @author jamesdbloom
 */
@Component
public class SpringSecurityUserContext {
    private final UserDAO userDAO;
    private final UserDetailsService userDetailsService;

    @Autowired
    public SpringSecurityUserContext(UserDAO userDAO, UserDetailsService userDetailsService) {
        this.userDAO = userDAO;
        this.userDetailsService = userDetailsService;
    }

    public User getCurrentUser() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        if(authentication == null) {
            return null;
        }

        String email = authentication.getName();
        return userDAO.findByEmail(email);
    }

    public void setCurrentUser(User user) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, "password", userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

//        List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(Role.ROLE_USER.name());
//        UserDetails userDetails = new org.springframework.security.core.userdetails.User(user.getEmail(), "password", authorities);
//        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
//        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
