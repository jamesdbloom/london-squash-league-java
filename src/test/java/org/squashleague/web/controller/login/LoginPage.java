package org.squashleague.web.controller.login;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.test.web.servlet.MvcResult;

import java.io.UnsupportedEncodingException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author jamesdbloom
 */
public class LoginPage {
    private final Document html;

    public LoginPage(MvcResult response) throws UnsupportedEncodingException {
        html = Jsoup.parse(response.getResponse().getContentAsString());
    }

    public void shouldHaveCorrectFields() {
        hasCorrectUserNameField();
        hasCorrectPasswordField();
    }

    public void hasCorrectUserNameField() {
        Element emailElement = html.select("input[name=email]").first();
        assertNotNull(emailElement);
        assertEquals("email", emailElement.attr("type"));
    }

    public void hasCorrectPasswordField() {
        Element passwordElement = html.select("input[name=password]").first();
        assertNotNull(passwordElement);
        assertEquals("password", passwordElement.attr("type"));
    }
}
