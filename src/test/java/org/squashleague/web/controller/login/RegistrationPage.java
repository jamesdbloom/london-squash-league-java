package org.squashleague.web.controller.login;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.test.web.servlet.MvcResult;
import org.squashleague.domain.account.MobilePrivacy;

import java.io.UnsupportedEncodingException;

import static org.junit.Assert.*;

/**
 * @author jamesdbloom
 */
public class RegistrationPage {
    private final Document html;

    public RegistrationPage(MvcResult response) throws UnsupportedEncodingException {
        html = Jsoup.parse(response.getResponse().getContentAsString());
    }

    public void hasErrors(String objectName, int errorCount) {
        Elements errorMessages = html.select("#validation_error_" + objectName + " .validation_error");
        assertEquals(errorMessages.toString(), errorCount, errorMessages.size());
    }

    public void hasRegistrationFields(String name, String email, String mobile, MobilePrivacy mobilePrivacy) {
        Element nameInputElement = html.select("#name").first();
        assertNotNull(nameInputElement);
        assertEquals(name, nameInputElement.val());

        Element emailInputElement = html.select("#email").first();
        assertNotNull(emailInputElement);
        assertEquals(email, emailInputElement.val());

        Element mobileInputElement = html.select("#mobile").first();
        assertNotNull(mobileInputElement);
        assertEquals(mobile, mobileInputElement.val());

        Element mobilePrivacyInputElement = html.select("#mobilePrivacy [selected=selected]").first();
        if (mobilePrivacy != null) {
            assertNotNull(mobilePrivacyInputElement);
            assertEquals(mobilePrivacy.name(), mobilePrivacyInputElement.attr("value"));
        } else {
            assertNull(mobilePrivacyInputElement);
        }
    }
}
