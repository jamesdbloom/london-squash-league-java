package org.squashleague.service.security;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.squashleague.domain.account.User;

import javax.annotation.Resource;

/**
 * @author jamesdbloom
 */
@Component
public class CredentialValidation {

    @Resource
    private PasswordEncoder passwordEncoder;

    protected boolean credentialsMatch(CharSequence password, User user) {
        return user != null && password != null && user.getPassword() != null && passwordEncoder.matches(password, user.getPassword());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or principal.id == #user.id")
    public boolean checkCredentials(CharSequence password, User user) {
        return credentialsMatch(password, user);
    }
}
