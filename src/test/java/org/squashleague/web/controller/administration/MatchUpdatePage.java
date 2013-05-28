package org.squashleague.web.controller.administration;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.test.web.servlet.MvcResult;

import java.io.UnsupportedEncodingException;

import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author jamesdbloom
 */
public class MatchUpdatePage {
    private final Document html;

    public MatchUpdatePage(MvcResult response) throws UnsupportedEncodingException {
        html = Jsoup.parse(response.getResponse().getContentAsString());
    }

    public void hasErrors(String objectName, int errorCount) {
        Elements errorMessages = html.select("#validation_error_" + objectName + " .validation_error");
        assertEquals(errorCount, errorMessages.size());
    }

    public void hasMatchFields(Long id, Integer version, Long playerOneId, Long playerTwoId, Long roundId) {
        Element idElement = html.select("#id").first();
        assertNotNull(idElement);
        assertEquals(String.valueOf(id), idElement.val());

        Element versionElement = html.select("#version").first();
        assertNotNull(versionElement);
        assertEquals(String.valueOf(version), versionElement.val());

        Element playerOneInputElement = html.select("#playerOne [selected=selected]").first();
        if (playerOneId != null) {
            assertNotNull(playerOneInputElement);
            assertEquals(playerOneId.toString(), playerOneInputElement.val());
        } else {
            assertNull(playerOneInputElement);
        }

        Element playerTwoInputElement = html.select("#playerTwo [selected=selected]").first();
        if (playerTwoId != null) {
            assertNotNull(playerTwoInputElement);
            assertEquals(playerTwoId.toString(), playerTwoInputElement.val());
        } else {
            assertNull(playerTwoInputElement);
        }

        Element roundInputElement = html.select("#round [selected=selected]").first();
        if (roundId != null) {
            assertNotNull(roundInputElement);
            assertEquals(roundId.toString(), roundInputElement.val());
        } else {
            assertNull(roundInputElement);
        }
    }
}
