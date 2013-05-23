package org.squashleague.web.controller.administration;

import org.joda.time.DateTime;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.test.web.servlet.MvcResult;
import org.squashleague.domain.account.MobilePrivacy;
import org.squashleague.domain.league.PlayerStatus;

import java.io.UnsupportedEncodingException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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
        assertNotNull(nameInputElement);
        assertEquals(name, nameInputElement.val());

        Element addressInputElement = html.select("#create_role #description").first();
        assertNotNull(addressInputElement);
        assertEquals(description, addressInputElement.val());
    }

    public void hasUserFields(String name, String email, String mobile, MobilePrivacy mobilePrivacy, String roleName) {
        Element nameInputElement = html.select("#create_user #name").first();
        assertNotNull(nameInputElement);
        assertEquals(name, nameInputElement.val());

        Element emailInputElement = html.select("#create_user #email").first();
        assertNotNull(emailInputElement);
        assertEquals(email, emailInputElement.val());

        Element mobileInputElement = html.select("#create_user #mobile").first();
        assertNotNull(mobileInputElement);
        assertEquals(mobile, mobileInputElement.val());

        Element mobilePrivacyInputElement = html.select("#create_user #mobilePrivacy [selected=selected]").first();
        assertNotNull(mobilePrivacyInputElement);
        assertEquals(mobilePrivacy.name(), mobilePrivacyInputElement.attr("value"));

        Element roleInputElement = html.select("#create_user #roles [selected=selected]").first();
        assertNotNull(roleInputElement);
        assertEquals(roleName, roleInputElement.attr("value"));
    }

    public void hasClubFields(String name, String address) {
        Element nameInputElement = html.select("#create_club #name").first();
        assertNotNull(nameInputElement);
        assertEquals(name, nameInputElement.val());

        Element addressInputElement = html.select("#create_club #address").first();
        assertNotNull(addressInputElement);
        assertEquals(address, addressInputElement.val());
    }

    public void hasLeagueFields(String name) {
        Element nameInputElement = html.select("#create_league #name").first();
        assertNotNull(nameInputElement);
        assertEquals(name, nameInputElement.val());
    }

    public void hasDivisionFields(String name) {
        Element nameInputElement = html.select("#create_division #name").first();
        assertNotNull(nameInputElement);
        assertEquals(name, nameInputElement.val());
    }

    public void hasRoundFields(Long divisionId, DateTime startDate, DateTime endDate) {
        Element divisionInputElement = html.select("#create_round #division [selected=selected]").first();
        assertNotNull(divisionInputElement);
        assertEquals(divisionId.toString(), divisionInputElement.attr("value"));

        Element startDateInputElement = html.select("#create_round #startDate").first();
        assertNotNull(startDateInputElement);
        assertEquals(startDate.toString("yyyy-MM-dd"), startDateInputElement.val());

        Element endDateInputElement = html.select("#create_round #endDate").first();
        assertNotNull(endDateInputElement);
        assertEquals(endDate.toString("yyyy-MM-dd"), endDateInputElement.val());
    }

    public void hasPlayerFields(Long userId, Long divisionId, PlayerStatus playerStatus) {
        Element userInputElement = html.select("#create_player #user [selected=selected]").first();
        assertNotNull(userInputElement);
        assertEquals(userId.toString(), userInputElement.attr("value"));

        Element divisionInputElement = html.select("#create_player #currentDivision [selected=selected]").first();
        assertNotNull(divisionInputElement);
        assertEquals(divisionId.toString(), divisionInputElement.attr("value"));

        Element statusInputElement = html.select("#create_player #status [selected=selected]").first();
        assertNotNull(statusInputElement);
        assertEquals(playerStatus.name(), statusInputElement.attr("value"));
    }

    public void hasMatchFields(Long roundId, Long playerOneId, Long playerTwoId, String score) {
        Element roundInputElement = html.select("#create_match #round [selected=selected]").first();
        assertNotNull(roundInputElement);
        assertEquals(roundId.toString(), roundInputElement.attr("value"));

        Element playerOneInputElement = html.select("#create_match #playerOne [selected=selected]").first();
        assertNotNull(playerOneInputElement);
        assertEquals(playerOneId.toString(), playerOneInputElement.attr("value"));

        Element playerTwoInputElement = html.select("#create_match #playerTwo [selected=selected]").first();
        assertNotNull(playerTwoInputElement);
        assertEquals(playerTwoId.toString(), playerTwoInputElement.attr("value"));

        Element scoreInputElement = html.select("#create_match #score").first();
        assertNotNull(scoreInputElement);
        assertEquals(score, scoreInputElement.val());
    }
}
