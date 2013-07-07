package org.squashleague.web.controller.administration;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.test.web.servlet.MvcResult;
import org.squashleague.domain.league.PlayerStatus;

import java.io.UnsupportedEncodingException;

import static org.junit.Assert.*;

/**
 * @author jamesdbloom
 */
public class PlayerUpdatePage {
    private final Document html;

    public PlayerUpdatePage(MvcResult response) throws UnsupportedEncodingException {
        html = Jsoup.parse(response.getResponse().getContentAsString());
    }

    public void hasErrors(String objectName, int errorCount) {
        Elements errorMessages = html.select("#validation_error_" + objectName + " .validation_error");
        assertEquals(errorMessages.toString(), errorCount, errorMessages.size());
    }

    public void hasPlayerFields(Long id, Integer version, Long userId, Long currentDivisionId, Long leagueId, PlayerStatus status) {
        Element idElement = html.select("#id").first();
        assertNotNull(idElement);
        assertEquals(String.valueOf(id), idElement.val());

        Element versionElement = html.select("#version").first();
        assertNotNull(versionElement);
        assertEquals(String.valueOf(version), versionElement.val());

        Element userInputElement = html.select("#user [selected=selected]").first();
        if (userId != null) {
            assertNotNull(userInputElement);
            assertEquals(userId.toString(), userInputElement.val());
        } else {
            assertNull(userInputElement);
        }

        Element currentDivisionInputElement = html.select("#currentDivision [selected=selected]").first();
        if (currentDivisionId != null) {
            assertNotNull(currentDivisionInputElement);
            assertEquals(currentDivisionId.toString(), currentDivisionInputElement.val());
        } else {
            assertNull(currentDivisionInputElement);
        }

        Element leagueInputElement = html.select("#league [selected=selected]").first();
        if (leagueId != null) {
            assertNotNull(leagueInputElement);
            assertEquals(leagueId.toString(), leagueInputElement.val());
        } else {
            assertNull(leagueInputElement);
        }

        Element statusInputElement = html.select("#status [selected=selected]").first();
        if (status != null) {
            assertNotNull(statusInputElement);
            assertEquals(status.name(), statusInputElement.val());
        } else {
            assertNull(statusInputElement);
        }
    }
}
