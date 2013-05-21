package org.squashleague.web.controller.login;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author jamesdbloom
 */
public class LoginControllerTest {

    @Test
    public void shouldReturnCorrectLogicalViewName() {
        Assert.assertEquals("page/login", new LoginController().getPage());
    }
}
