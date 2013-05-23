package org.squashleague.web.controller.administration;

import org.joda.time.DateTime;
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
public class RoundUpdatePage {
    private final Document html;

    public RoundUpdatePage(MvcResult response) throws UnsupportedEncodingException {
        html = Jsoup.parse(response.getResponse().getContentAsString());
    }

    public void hasErrors(String objectName, int errorCount) {
        Elements errorMessages = html.select("#validation_error_" + objectName + " .validation_error");
        assertEquals(errorCount, errorMessages.size());
    }

    public void hasRoundFields(Long id, Integer version, DateTime startDate, DateTime endDate, Long divisionId) {
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

        Element divisionInputElement = html.select("#division [selected=selected]").first();
        if (divisionId != null) {
            assertNotNull(divisionInputElement);
            assertEquals(divisionId.toString(), divisionInputElement.val());
        } else {
            assertNull(divisionInputElement);
        }
    }
}
