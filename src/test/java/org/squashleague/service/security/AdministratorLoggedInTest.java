package org.squashleague.service.security;

import org.junit.Before;
import org.squashleague.domain.account.Role;
import org.squashleague.domain.account.User;

import javax.annotation.Resource;

/**
 * @author jamesdbloom
 */
public abstract class AdministratorLoggedInTest {

    @Resource
    protected SpringSecurityUserContext securityUserContext;

    protected static final String LOGGED_IN_USER_EMAIL = "user@email.com";

    @Before
    public void setupUser() {
        securityUserContext.setCurrentUser(new User().withRoles(Role.ROLE_ADMIN).withEmail(LOGGED_IN_USER_EMAIL));
    }
}
