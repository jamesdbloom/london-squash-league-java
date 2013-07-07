package org.squashleague.web.controller.administration;

import org.joda.time.DateTime;
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
public class RoundUpdatePage {
    private final Document html;

    public RoundUpdatePage(MvcResult response) throws UnsupportedEncodingException {
        html = Jsoup.parse(response.getResponse().getContentAsString());
    }

    public void hasErrors(String objectName, int errorCount) {
        Elements errorMessages = html.select("#validation_error_" + objectName + " .validation_error");
        assertEquals(errorMessages.toString(), errorCount, errorMessages.size());
    }

    public void hasRoundFields(Long id, Integer version, DateTime startDate, DateTime endDate, Long leagueId) {
        Element idElement = html.select("#id").first();
        assertNotNull(idElement);
        assertEquals(String.valueOf(id), idElement.val());

        Element versionElement = html.select("#version").first();
        assertNotNull(versionElement);
        assertEquals(String.valueOf(version), versionElement.val());

        Element startDateInputElement = html.select("#startDate").first();
        assertNotNull(startDateInputElement);
        assertEquals((startDate != null ? startDate.toString("yyyy-MM-dd") : ""), startDateInputElement.val());

        Element endDateInputElement = html.select("#endDate").first();
        assertNotNull(endDateInputElement);
        assertEquals((endDate != null ? endDate.toString("yyyy-MM-dd") : ""), endDateInputElement.val());

        Element divisionInputElement = html.select("#league [selected=selected]").first();
        if (leagueId != null) {
            assertNotNull(divisionInputElement);
            assertEquals(leagueId.toString(), divisionInputElement.val());
        } else {
            assertNull(divisionInputElement);
        }
    }
}
