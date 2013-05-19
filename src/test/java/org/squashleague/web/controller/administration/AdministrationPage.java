package org.squashleague.web.controller.administration;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatterBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.test.web.servlet.MvcResult;
import org.squashleague.domain.account.MobilePrivacy;
import org.squashleague.domain.league.PlayerStatus;

import java.io.UnsupportedEncodingException;

import static org.junit.Assert.assertEquals;

/**
 * @author jamesdbloom
 */
public class AdministrationPage {
    private final Document html;

    public AdministrationPage(MvcResult response) throws UnsupportedEncodingException {
        html = Jsoup.parse(response.getResponse().getContentAsString());
    }

    public void hasErrors(String objectName, int errorCount) {
        Elements errorMessages = html.select("#validation_error_" + objectName + " .validation_error");
        assertEquals(errorCount, errorMessages.size());
    }

    public void hasRoleFields(String name, String description) {
        Element nameInputElement = html.select("#create_role #name").first();
        assertEquals(name, nameInputElement.val());
        Element addressInputElement = html.select("#create_role #description").first();
        assertEquals(description, addressInputElement.val());
    }

    public void hasUserFields(String name, String email, String mobile, MobilePrivacy mobilePrivacy, Long roleId) {
        Element nameInputElement = html.select("#create_user #name").first();
        assertEquals(name, nameInputElement.val());
        Element emailInputElement = html.select("#create_user #email").first();
        assertEquals(email, emailInputElement.val());
        Element mobileInputElement = html.select("#create_user #mobile").first();
        assertEquals(mobile, mobileInputElement.val());
        Element mobilePrivacyInputElement = html.select("#create_user #mobilePrivacy [selected=selected]").first();
        assertEquals(mobilePrivacy.name(), mobilePrivacyInputElement.attr("value"));
        Element roleInputElement = html.select("#create_user #roles [selected=selected]").first();
        assertEquals(roleId.toString(), roleInputElement.attr("value"));
    }

    public void hasClubFields(String name, String address) {
        Element nameInputElement = html.select("#create_club #name").first();
        assertEquals(name, nameInputElement.val());
        Element addressInputElement = html.select("#create_club #address").first();
        assertEquals(address, addressInputElement.val());
    }

    public void hasLeagueFields(String name) {
        Element nameInputElement = html.select("#create_league #name").first();
        assertEquals(name, nameInputElement.val());
    }

    public void hasDivisionFields(String name) {
        Element nameInputElement = html.select("#create_division #name").first();
        assertEquals(name, nameInputElement.val());
    }

    public void hasRoundFields(Long divisionId, String startDate, String endDate) {
        Element divisionInputElement = html.select("#create_round #division [selected=selected]").first();
        assertEquals(divisionId.toString(), divisionInputElement.attr("value"));
        Element startDateInputElement = html.select("#create_round #startDate").first();
        assertEquals(startDate, startDateInputElement.val());
        Element endDateInputElement = html.select("#create_round #endDate").first();
        assertEquals(endDate, endDateInputElement.val());
    }

    public void hasPlayerFields(Long userId, Long divisionId, PlayerStatus playerStatus) {
        Element userInputElement = html.select("#create_player #user [selected=selected]").first();
        assertEquals(userId.toString(), userInputElement.attr("value"));
        Element divisionInputElement = html.select("#create_player #currentDivision [selected=selected]").first();
        assertEquals(divisionId.toString(), divisionInputElement.attr("value"));
        Element statusInputElement = html.select("#create_player #status [selected=selected]").first();
        assertEquals(playerStatus.name(), statusInputElement.attr("value"));
    }

    public void hasMatchFields(Long roundId, Long playerOneId, Long playerTwoId, String score, String scoreEntered) {
        Element roundInputElement = html.select("#create_match #round [selected=selected]").first();
        assertEquals(roundId.toString(), roundInputElement.attr("value"));
        Element playerOneInputElement = html.select("#create_match #playerOne [selected=selected]").first();
        assertEquals(playerOneId.toString(), playerOneInputElement.attr("value"));
        Element playerTwoInputElement = html.select("#create_match #playerTwo [selected=selected]").first();
        assertEquals(playerTwoId.toString(), playerTwoInputElement.attr("value"));
        Element scoreInputElement = html.select("#create_match #score").first();
        assertEquals(score, scoreInputElement.val());
        Element scoreEnteredInputElement = html.select("#create_match #scoreEntered").first();
        assertEquals(scoreEntered, DateTime.parse(scoreEnteredInputElement.val(), new DateTimeFormatterBuilder().appendPattern("MM/dd/yy mm:ss a").toFormatter()).toString("yyyy-MM-dd"));
    }
}
