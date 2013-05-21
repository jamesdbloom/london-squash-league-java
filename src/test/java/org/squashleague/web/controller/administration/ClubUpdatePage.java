package org.squashleague.web.controller.administration;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.test.web.servlet.MvcResult;
import org.squashleague.domain.league.Club;

import java.io.UnsupportedEncodingException;

/**
 * @author jamesdbloom
 */
public class ClubUpdatePage {
    private final Document html;

    public ClubUpdatePage(MvcResult response) throws UnsupportedEncodingException {
        html = Jsoup.parse(response.getResponse().getContentAsString());
    }

    public void shouldHaveCorrectFields(Club object) {
        // todo - add tests in here
    }
}
