package org.squashleague.web.controller.league;

import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.squashleague.domain.league.Match;
import org.squashleague.domain.league.Player;
import org.squashleague.web.controller.WebAndDataIntegrationTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author jamesdbloom
 */
public class LeagueTablePageIntegrationTest extends WebAndDataIntegrationTest {

    @Test
    public void shouldDisplayUserDetails() throws Exception {
        securityUserContext.setCurrentUser(userOne);

        try {
            MvcResult result = mockMvc.perform(get("/leagueTable").accept(MediaType.TEXT_HTML))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("text/html;charset=UTF-8"))
                    .andReturn();

            LeagueTablePage leagueTablePage = new LeagueTablePage(result);
            leagueTablePage.hasRounds(roundOne, roundTwo);
            leagueTablePage.hasMatches(new Match[][]{{matchOne, matchTwo}, {matchFive}}, new Player[][]{{playerOne, playerTwo}, {playerOne, playerFour}}, userOne);
        } finally {
            securityUserContext.setCurrentUser(LOGGED_IN_USER);
        }
    }
}
