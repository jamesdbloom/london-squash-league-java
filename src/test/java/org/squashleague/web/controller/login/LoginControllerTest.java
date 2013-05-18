package org.squashleague.web.controller.login;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author jamesdbloom
 */
public class LoginControllerTest {

    @Test
    public void testShouldReturnCorrectLogicalViewName() {
        Assert.assertEquals("page/login", new LoginController().getPage());
    }
}
