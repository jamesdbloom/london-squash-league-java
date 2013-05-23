package org.squashleague.web.controller.error;

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
public class ErrorPage {
    private final Document html;

    public ErrorPage(MvcResult response) throws UnsupportedEncodingException {
        html = Jsoup.parse(response.getResponse().getContentAsString());
    }

    public void shouldDisplayCorrectMessageAndTitle(String title, String message) {
        Element titleElement = html.select("#header").first();
        assertNotNull(titleElement);
        assertEquals(titleElement.text(), title);
        Element messageElement = html.select(".message").first();
        assertNotNull(messageElement);
        assertEquals(messageElement.text(), message);
    }
}
