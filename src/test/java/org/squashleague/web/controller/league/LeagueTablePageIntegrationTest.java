package org.squashleague.web.controller.league;

import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.squashleague.domain.account.User;
import org.squashleague.web.controller.WebAndDataIntegrationTest;

import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author jamesdbloom
 */
public class LeagueTablePageIntegrationTest extends WebAndDataIntegrationTest {

    @Test
    public void shouldDisplayDivisions() throws Exception {
        User user = users.get(0);
        securityUserContext.setCurrentUser(user);

        try {
            MvcResult result = mockMvc.perform(get("/leagueTable").accept(MediaType.TEXT_HTML))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("text/html;charset=UTF-8"))
                    .andReturn();

            LeagueTablePage leagueTablePage = new LeagueTablePage(result);
            leagueTablePage.hasRounds(rounds.get(0), rounds.get(1));
            leagueTablePage.hasMatches(
                    Arrays.asList(
                            getUserMatches(matchesDivisionZeroRoundZero, user),
                            getUserMatches(matchesDivisionZeroRoundOne, user),
                            getUserMatches(matchesDivisionOneRoundZero, user),
                            getUserMatches(matchesDivisionOneRoundOne, user)
                    ),
                    Arrays.asList(
                            playersDivisionZero,
                            playersDivisionZero,
                            playersDivisionOne,
                            playersDivisionOne
                    ),
                    user);
        } finally {
            securityUserContext.setCurrentUser(LOGGED_IN_USER);
        }
    }

    @Test
    public void shouldDisplayLeagues() throws Exception {
        User user = users.get(0);
        securityUserContext.setCurrentUser(user);

        try {
            MvcResult result = mockMvc.perform(get("/leagueTable").accept(MediaType.TEXT_HTML))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("text/html;charset=UTF-8"))
                    .andReturn();

            LeagueTablePage leagueTablePage = new LeagueTablePage(result);
            leagueTablePage.hasRounds(rounds.get(0), rounds.get(1)); // todo make different between tests
            leagueTablePage.hasMatches(
                    Arrays.asList(
                            getUserMatches(matchesDivisionZeroRoundZero, user),
                            getUserMatches(matchesDivisionZeroRoundOne, user),
                            getUserMatches(matchesDivisionOneRoundZero, user),
                            getUserMatches(matchesDivisionOneRoundOne, user)
                    ),
                    Arrays.asList(
                            playersDivisionZero,
                            playersDivisionZero,
                            playersDivisionOne,
                            playersDivisionOne
                    ),
                    user);
        } finally {
            securityUserContext.setCurrentUser(LOGGED_IN_USER);
        }
    }

    @Test
    public void shouldDisplayDivisionsForUserWithNoCurrentDivisions() throws Exception {
        securityUserContext.setCurrentUser(users.get(5));

        try {
            MvcResult result = mockMvc.perform(get("/leagueTable").accept(MediaType.TEXT_HTML))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("text/html;charset=UTF-8"))
                    .andReturn();

            new LeagueTablePage(result).hasNoRounds();
        } finally {
            securityUserContext.setCurrentUser(LOGGED_IN_USER);
        }
    }

    @Test
    public void shouldDisplayLeaguesForUserWithNoCurrentDivisions() throws Exception {
        User user = users.get(5);
        securityUserContext.setCurrentUser(user);

        try {
            MvcResult result = mockMvc.perform(get("/leagueTable")
                    .accept(MediaType.TEXT_HTML)
                    .param("showAllDivisions", "true")
            )
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("text/html;charset=UTF-8"))
                    .andReturn();

            LeagueTablePage leagueTablePage = new LeagueTablePage(result);
            leagueTablePage.hasRounds(roundsByDivisionId.get(divisions.get(1).getId()).get(0), roundsByDivisionId.get(divisions.get(1).getId()).get(1));
            leagueTablePage.hasMatches(Arrays.asList(matchesDivisionOneRoundZero, matchesDivisionOneRoundOne), Arrays.asList(playersDivisionOne, playersDivisionOne), user);
        } finally {
            securityUserContext.setCurrentUser(LOGGED_IN_USER);
        }
    }

    @Test
    public void shouldDisplayDivisionsForUserWithNoLeague() throws Exception {
        securityUserContext.setCurrentUser(users.get(6));

        try {
            MvcResult result = mockMvc.perform(get("/leagueTable").accept(MediaType.TEXT_HTML))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("text/html;charset=UTF-8"))
                    .andReturn();

            new LeagueTablePage(result).hasNoRounds();
        } finally {
            securityUserContext.setCurrentUser(LOGGED_IN_USER);
        }
    }

    @Test
    public void shouldDisplayLeaguesForUserWithNoLeague() throws Exception {
        securityUserContext.setCurrentUser(users.get(6));

        try {
            MvcResult result = mockMvc.perform(get("/leagueTable")
                    .accept(MediaType.TEXT_HTML)
                    .param("showAllDivisions", "true")
            )
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("text/html;charset=UTF-8"))
                    .andReturn();

            new LeagueTablePage(result).hasNoRounds();
        } finally {
            securityUserContext.setCurrentUser(LOGGED_IN_USER);
        }
    }
}
