package org.squashleague.web.controller.login;

import org.squashleague.web.controller.LoginPageController;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author squashleague
 */
public class LoginPageControllerTest {

    @Test
    public void testShouldReturnCorrectLogicalViewName() {
        Assert.assertEquals("page/login", new LoginPageController().getPage());
    }
}
