package org.squashleague.web.controller.league;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.test.web.servlet.MvcResult;
import org.squashleague.domain.account.User;
import org.squashleague.domain.league.Match;
import org.squashleague.domain.league.Player;
import org.squashleague.domain.league.Round;

import java.io.UnsupportedEncodingException;

import static org.junit.Assert.assertEquals;

/**
 * @author jamesdbloom
 */
public class LeagueTablePage {
    private final Document html;

    public LeagueTablePage(MvcResult response) throws UnsupportedEncodingException {
        html = Jsoup.parse(response.getResponse().getContentAsString());
    }

    public void hasRounds(Round... rounds) {
        Elements tableTitleElements = html.select(".table_title");
        Elements tableSubTitleElements = html.select(".table_subtitle");

        for (int i = 0; i < rounds.length; i++) {
            Round round = rounds[i];

            assertEquals("(" + round.getStartDate().toString("dd MMM yyyy") + " – " + round.getEndDate().toString("dd MMM yyyy") + ")", tableTitleElements.get(i).text());
            assertEquals(round.getDivision().getLeague().getClub().getName() + " – " + round.getDivision().getLeague().getName() + " – " + round.getDivision().getName(), tableSubTitleElements.get(i).text());
        }
    }

    public void hasMatches(Match[][] matches, Player[][] players, User user) {
        for (int i = 0; i < matches.length; i++) {  // rounds
            for (int j = 0; j < matches[i].length; j++) {  // matches
                Match match = matches[i][j];

                Element smallScreenScoreElement = html.select("#match_" + match.getPlayerOne().getId() + "_" + match.getPlayerTwo().getId() + "_smallScreenScore").first();
                assertEquals(scoreValue(match, user), smallScreenScoreElement.text());
                Element largeScreenScoreElement = html.select("#match_" + match.getPlayerOne().getId() + "_" + match.getPlayerTwo().getId() + "_largeScreenScore").first();
                assertEquals(scoreValue(match, user), largeScreenScoreElement.text());

                Element smallScreenPlayerOneElement = html.select("#match_" + i + "_" + j + "_smallScreenPlayerOne").first();
                assertEquals(match.getPlayerOne().getUser().getName(), smallScreenPlayerOneElement.text());
                Element smallScreenPlayerTwoElement = html.select("#match_" + i + "_" + j + "_smallScreenPlayerTwo").first();
                assertEquals(match.getPlayerTwo().getUser().getName(), smallScreenPlayerTwoElement.text());
            }

            for (int j = 0; j < players[i].length; j++) { // players
                Player player = players[i][j];

                Element largeScreenPlayerOneElement = html.select("#match_" + i + "_" + j + "_largeScreenPlayerOne").first();
                assertEquals(player.getUser().getName(), largeScreenPlayerOneElement.text());
                Element largeScreenPlayerTwoElement = html.select("#match_" + i + "_" + j + "_largeScreenPlayerTwo").first();
                assertEquals(player.getUser().getName(), largeScreenPlayerTwoElement.text());
            }

        }
    }

    private String scoreValue(Match match, User user) {
        boolean userMatch = match.getPlayerOne().getUser().getId().equals(user.getId()) || match.getPlayerTwo().getUser().getId().equals(user.getId());
        return (match.getScore() != null ? match.getScore() : (userMatch ? "enter" : ""));
    }
}
