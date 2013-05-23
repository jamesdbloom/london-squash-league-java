package org.squashleague.web.controller.contact;

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
public class ContactUsPage {
    private final Document html;

    public ContactUsPage(MvcResult response) throws UnsupportedEncodingException {
        html = Jsoup.parse(response.getResponse().getContentAsString());
    }

    public void hasReadOnlyEmailField(String email) {
        Element emailInputElement = html.select("input#email").first();
        assertNotNull(emailInputElement);
        assertEquals(emailInputElement.val(), email);
    }
}
