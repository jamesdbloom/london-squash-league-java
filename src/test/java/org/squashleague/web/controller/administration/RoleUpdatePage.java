package org.squashleague.web.controller.administration;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.test.web.servlet.MvcResult;

import java.io.UnsupportedEncodingException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author jamesdbloom
 */
public class RoleUpdatePage {
    private final Document html;

    public RoleUpdatePage(MvcResult response) throws UnsupportedEncodingException {
        html = Jsoup.parse(response.getResponse().getContentAsString());
    }

    public void hasErrors(String objectName, int errorCount) {
        Elements errorMessages = html.select("#validation_error_" + objectName + " .validation_error");
        assertEquals(errorCount, errorMessages.size());
    }

    public void hasRoleFields(Long id, Integer version, String name, String description) {
        Element idElement = html.select("#id").first();
        assertNotNull(idElement);
        assertEquals(String.valueOf(id), idElement.val());

        Element versionElement = html.select("#version").first();
        assertNotNull(versionElement);
        assertEquals(String.valueOf(version), versionElement.val());

        Element nameInputElement = html.select("#name").first();
        assertNotNull(nameInputElement);
        assertEquals(name, nameInputElement.val());

        Element descriptionInputElement = html.select("#description").first();
        assertNotNull(descriptionInputElement);
        assertEquals(description, descriptionInputElement.val());
    }
}
