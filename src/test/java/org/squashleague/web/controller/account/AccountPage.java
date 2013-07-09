package org.squashleague.web.controller.account;

import com.google.common.base.Joiner;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.test.web.servlet.MvcResult;
import org.squashleague.domain.account.MobilePrivacy;
import org.squashleague.domain.account.User;
import org.squashleague.domain.league.*;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

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

    public void hasPlayers(List<League> unregisteredLeagues, List<Player> players) {
        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);

            Element clubElement = html.select("#player_" + i + "_club").first();
            assertEquals(player.getLeague().getClub().getName(), clubElement.text());

            Element leagueElement = html.select("#player_" + i + "_league").first();
            assertEquals(player.getLeague().getName(), leagueElement.text());

            Element currentDivisionElement = html.select("#player_" + i + "_currentDivision").first();
            assertEquals(player.getCurrentDivision() != null ? String.valueOf(player.getCurrentDivision().getName()) : "", currentDivisionElement.text());

            Element statusElement = html.select("#player_" + i + "_status").first();
            assertEquals(player.getStatus().name(), statusElement.text());

            Element registerElement = html.select("#player_" + i + "_register").first();
            assertEquals((player.getStatus() == PlayerStatus.ACTIVE ? "Unregister" : "Register"), registerElement.text());

            Elements unregisteredLeaguesOptions = html.select("#unregisteredLeagues option");
            assertEquals("Please select", unregisteredLeaguesOptions.get(0).text());
            for (int j = 1; j < unregisteredLeaguesOptions.size(); j++) {
                assertEquals(unregisteredLeagues.get(j - 1).getClub().getName() + " – " + unregisteredLeagues.get(j - 1).getName(), unregisteredLeaguesOptions.get(j).text());
            }
        }
    }

    public void hasRounds(List<Round> rounds) {
        for (int i = 0; i < rounds.size(); i++) {
            Round round = rounds.get(i);

            Element divisionElement = html.select("#round_" + i + "_division").first();
            assertEquals(round.getLeague().getClub().getName() + " – " + round.getLeague().getName(), divisionElement.text());

            Element statusElement = html.select("#round_" + i + "_status").first();
            assertEquals(round.getStatus().name(), statusElement.text());

            Element startDateElement = html.select("#round_" + i + "_startDate").first();
            assertEquals(round.getStartDate().toString("dd MMM yyyy"), startDateElement.text());

            Element endDateElement = html.select("#round_" + i + "_endDate").first();
            assertEquals(round.getEndDate().toString("dd MMM yyyy"), endDateElement.text());
        }
    }

    public void hasMatches(User currentUser, List<Match>... matches) {
        for (int i = 0; i < matches.length; i++) {
            for (int j = 0; j < matches[i].size(); j++) {
                Match match = matches[i].get(j);

                Element divisionElement = html.select("#match_" + i + "_" + j + "_division").first();
                assertEquals(match.getDivision().getName().toString(), divisionElement.text());

                Element dateElement = html.select("#match_" + i + "_" + j + "_date").first();
                assertEquals(match.getDivision().getRound().getStartDate().toString("dd MMM yyyy") + " – " + match.getDivision().getRound().getEndDate().toString("dd MMM yyyy"), dateElement.text());

                Element playerOneElement = html.select("#match_" + i + "_" + j + "_playerOne").first();
                assertEquals(buildPlayerContactDetails(match.getPlayerOne().getUser(), currentUser), playerOneElement.text());

                Element playerTwoElement = html.select("#match_" + i + "_" + j + "_playerTwo").first();
                assertEquals(buildPlayerContactDetails(match.getPlayerTwo().getUser(), currentUser), playerTwoElement.text());

                Element scoreEnteredElement = html.select("#match_" + i + "_" + j + "_scoreEntered").first();
                assertEquals((match.getScoreEntered() != null ? match.getScoreEntered().toString("dd MMM yyyy") : ""), scoreEnteredElement.text());

                Element scoreElement = html.select("#match_" + i + "_" + j + "_score").first();
                assertEquals((match.getScore() != null ? match.getScore() : "enter"), scoreElement.text());
            }

            Element mailtoElement = html.select("#mailto_" + i).first();
            assertEquals(emailList(matches[i], currentUser), mailtoElement.attr("href"));

        }
    }

    private String emailList(List<Match> matches, User currentUser) {
        List<String> emails = new ArrayList<>();
        for (Match match : matches) {
            User userOne = match.getPlayerOne().getUser();
            if (!userOne.getId().equals(currentUser.getId()) && !emails.contains(userOne.getEmail())) {
                emails.add(userOne.getEmail());
            }
            User userTwo = match.getPlayerTwo().getUser();
            if (!userTwo.getId().equals(currentUser.getId()) && !emails.contains(userTwo.getEmail())) {
                emails.add(userTwo.getEmail());
            }
        }
        return "mailto:" + Joiner.on(",").join(emails);
    }

    private String buildPlayerContactDetails(User user, User currentUser) {
        if (user.equals(currentUser)) {
            return "You";
        } else {
            return user.getName() + (user.getMobilePrivacy() == MobilePrivacy.SHOW_ALL || user.getMobilePrivacy() == MobilePrivacy.SHOW_OPPONENTS ? " " + user.getMobile() + " " : " ") + user.getEmail() + "email";
        }
    }
}
