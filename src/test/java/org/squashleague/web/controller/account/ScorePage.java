package org.squashleague.web.controller.account;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.test.web.servlet.MvcResult;
import org.squashleague.domain.league.Match;

import java.io.UnsupportedEncodingException;

import static org.junit.Assert.fail;

/**
 * @author jamesdbloom
 */
public class ScorePage {
    private final Document html;

    public ScorePage(MvcResult response) throws UnsupportedEncodingException {
        html = Jsoup.parse(response.getResponse().getContentAsString());
    }

    public void hasMessage(Match matchOne) {
        Element messageElement = html.select(".message:not(#loading)").first();
        String message = messageElement.text();
        String playerOneName = matchOne.getPlayerOne().getUser().getName();
        if (!message.contains(playerOneName)) {
            fail("[" + playerOneName + "] not contained in [" + message + "]");
        }
        String playerTwoName = matchOne.getPlayerTwo().getUser().getName();
        if (!message.contains(playerOneName)) {
            fail("[" + playerTwoName + "] not contained in [" + message + "]");
        }
    }
}
