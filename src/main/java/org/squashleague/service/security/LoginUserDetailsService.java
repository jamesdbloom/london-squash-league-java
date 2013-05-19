package org.squashleague.service.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.squashleague.dao.account.UserDAO;
import org.squashleague.domain.account.User;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

/**
 * @author jamesdbloom
 */
@Component
public class LoginUserDetailsService implements UserDetailsService {
    @Resource
    private UserDAO userDAO;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDAO.findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("Invalid username/password.");
        }
        return new LoginUserDetails(user);
    }

    private class LoginUserDetails extends User implements UserDetails {

        public LoginUserDetails(User user) {
            setId(user.getId());
            setEmail(user.getEmail());
            setName(user.getName());
            setMobile(user.getMobile());
            setMobilePrivacy(user.getMobilePrivacy());
            setRoles(user.getRoles());
            setOneTimeToken(user.getOneTimeToken());
            setPlayers(user.getPlayers());
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return AuthorityUtils.createAuthorityList(getRoleNames());
        }

        @Override
        public String getPassword() {
            return super.getPassword();
        }

        @Override
        public String getUsername() {
            return super.getEmail();
        }

        @Override
        public boolean isAccountNonExpired() {
            return true;
        }

        @Override
        public boolean isAccountNonLocked() {
            return true;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }

        @Override
        public boolean isEnabled() {
            return true;
        }
    }
}
