package org.squashleague.web.controller.administration;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.test.web.servlet.MvcResult;

import java.io.UnsupportedEncodingException;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author jamesdbloom
 */
public class DivisionUpdatePage {
    private final Document html;

    public DivisionUpdatePage(MvcResult response) throws UnsupportedEncodingException {
        html = Jsoup.parse(response.getResponse().getContentAsString());
    }

    public void hasErrors(String objectName, int errorCount) {
        Elements errorMessages = html.select("#validation_error_" + objectName + " .validation_error");
        assertEquals(errorCount, errorMessages.size());
    }

    public void hasDivisionFields(Long id, Integer version, String name, Long roundId) {
        Element idElement = html.select("#id").first();
        assertNotNull(idElement);
        assertEquals(String.valueOf(id), idElement.val());

        Element versionElement = html.select("#version").first();
        assertNotNull(versionElement);
        assertEquals(String.valueOf(version), versionElement.val());

        Element nameInputElement = html.select("#name").first();
        assertNotNull(nameInputElement);
        assertEquals(name, nameInputElement.val());

        Element leagueInputElement = html.select("#round [selected=selected]").first();
        if (roundId != null) {
            assertNotNull(leagueInputElement);
            assertEquals(roundId.toString(), leagueInputElement.val());
        } else {
            assertNull(leagueInputElement);
        }
    }
}
