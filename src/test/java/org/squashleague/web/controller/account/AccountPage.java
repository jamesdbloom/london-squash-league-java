package org.squashleague.web.controller.account;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.test.web.servlet.MvcResult;
import org.squashleague.domain.account.User;
import org.squashleague.domain.league.Match;

import java.io.UnsupportedEncodingException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * @author jamesdbloom
 */
public class AccountPage {
    private final Document html;

    public AccountPage(MvcResult response) throws UnsupportedEncodingException {
        html = Jsoup.parse(response.getResponse().getContentAsString());
    }

    public void hasUserDetails(User user) {
        Element nameElement = html.select("#user_name").first();
        assertEquals(user.getName(), nameElement.text());
        Element emailElement = html.select("#user_email").first();
        assertEquals(user.getEmail(), emailElement.text());
        Element mobileElement = html.select("#user_mobile").first();
        assertEquals(user.getMobile(), mobileElement.text());
        Element mobilePrivacyElement = html.select("#user_mobilePrivacy").first();
        assertEquals(user.getMobilePrivacy().name(), mobilePrivacyElement.text());
    }
}
