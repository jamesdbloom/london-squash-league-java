package org.squashleague.service.security;

import org.junit.Before;
import org.squashleague.domain.account.Role;
import org.squashleague.domain.account.User;

import javax.annotation.Resource;

/**
 * @author jamesdbloom
 */
public abstract class AdministratorLoggedInTest {

    protected static final User LOGGED_IN_USER = new User().withRoles(Role.ROLE_ADMIN).withEmail("admin@email.com");
    @Resource
    protected SpringSecurityUserContext securityUserContext;

    @Before
    public void setupUser() {
        securityUserContext.setCurrentUser(LOGGED_IN_USER);
    }
}
