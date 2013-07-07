package org.squashleague.web.controller.administration;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.test.web.servlet.MvcResult;

import java.io.UnsupportedEncodingException;

import static org.junit.Assert.*;

/**
 * @author jamesdbloom
 */
public class LeagueUpdatePage {
    private final Document html;

    public LeagueUpdatePage(MvcResult response) throws UnsupportedEncodingException {
        html = Jsoup.parse(response.getResponse().getContentAsString());
    }

    public void hasErrors(String objectName, int errorCount) {
        Elements errorMessages = html.select("#validation_error_" + objectName + " .validation_error");
        assertEquals(errorMessages.toString(), errorCount, errorMessages.size());
    }

    public void hasLeagueFields(Long id, Integer version, String name, Long clubId) {
        Element idElement = html.select("#id").first();
        assertNotNull(idElement);
        assertEquals(String.valueOf(id), idElement.val());

        Element versionElement = html.select("#version").first();
        assertNotNull(versionElement);
        assertEquals(String.valueOf(version), versionElement.val());

        Element nameInputElement = html.select("#name").first();
        assertNotNull(nameInputElement);
        assertEquals(name, nameInputElement.val());

        Element clubInputElement = html.select("#club [selected=selected]").first();
        if (clubId != null) {
            assertNotNull(clubInputElement);
            assertEquals(clubId.toString(), clubInputElement.val());
        } else {
            assertNull(clubInputElement);
        }
    }
}
