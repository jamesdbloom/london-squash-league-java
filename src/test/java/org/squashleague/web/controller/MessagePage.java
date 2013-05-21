package org.squashleague.web.controller;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.test.web.servlet.MvcResult;

import java.io.UnsupportedEncodingException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.squashleague.Asserts.assertEndsWith;

/**
 * @author jamesdbloom
 */
public class MessagePage {
    private final Document html;

    public MessagePage(MvcResult response) throws UnsupportedEncodingException {
        html = Jsoup.parse(response.getResponse().getContentAsString());
    }

    public void hasConfirmationMessage(String message) {
        Element messageElement = html.select(".message").first();
        assertNotNull(messageElement);
        assertEquals(messageElement.text(), message);
    }

    public void hasTitle(String title) {
        Element headerElement = html.select("#header").first();
        assertNotNull(headerElement);
        assertEquals(headerElement.text(), title);

        Element titleElement = html.select("title").first();
        assertNotNull(titleElement);
        assertEndsWith(titleElement.text(), title);
    }
}
