package org.squashleague.web.controller.administration;

import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.squashleague.domain.account.User;
import org.squashleague.domain.league.*;
import org.squashleague.web.controller.WebAndDataIntegrationTest;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author jamesdbloom
 */
public class AdministrationPageIntegrationTest extends WebAndDataIntegrationTest {

    @Test
    public void shouldGetPage() throws Exception {
        mockMvc.perform(get("/administration").accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"));
    }

    @Test
    public void shouldGetPageWithRoleErrors() throws Exception {
        getAdministrationPage("role", 2, role).hasRoleFields(role.getName(), role.getDescription());
    }

    @Test
    public void shouldGetPageWithUserErrors() throws Exception {
        User user = users.get(0);
        getAdministrationPage("user", 2, user).hasUserFields(user.getName(), user.getEmail(), user.getMobile(), user.getMobilePrivacy(), user.getRoleNames());
    }

    @Test
    public void shouldGetPageWithClubErrors() throws Exception {
        Club club = clubs.get(0);
        getAdministrationPage("club", 2, club).hasClubFields(club.getName(), club.getAddress());
    }

    @Test
    public void shouldGetPageWithLeagueErrors() throws Exception {
        League league = leagues.get(0);
        getAdministrationPage("league", 2, league).hasLeagueFields(league.getClub().getId(), league.getName());
    }

    @Test
    public void shouldGetPageWithRoundErrors() throws Exception {
        Round round = rounds.get(0);
        getAdministrationPage("round", 2, round).hasRoundFields(round.getLeague().getId(), round.getStartDate(), round.getEndDate());
    }

    @Test
    public void shouldGetPageWithDivisionErrors() throws Exception {
        Division division = divisions.get(0);
        getAdministrationPage("division", 2, division).hasDivisionFields(division.getRound().getId(), division.getName());
    }

    @Test
    public void shouldGetPageWithPlayerErrors() throws Exception {
        Player player = players.get(0);
        getAdministrationPage("player", 2, player).hasPlayerFields(player.getUser().getId(), player.getCurrentDivision().getId(), player.getStatus());
    }

    @Test
    public void shouldGetPageWithMatchErrors() throws Exception {
        Match match = matches.get(0);
        getAdministrationPage("match", 2, match).hasMatchFields(match.getDivision().getId(), match.getPlayerOne().getId(), match.getPlayerTwo().getId(), match.getScore());
    }

    private AdministrationPage getAdministrationPage(String objectName, int errorCount, Object object) throws Exception {
        MvcResult response = mockMvc.perform(get("/administration")
                .accept(MediaType.TEXT_HTML)
                .flashAttr("bindingResult", getBindingResult(objectName, errorCount))
                .flashAttr(objectName, object)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andReturn();

        AdministrationPage administrationPage = new AdministrationPage(response);
        administrationPage.hasErrors(objectName, errorCount);
        return administrationPage;
    }

    private BindingResult getBindingResult(String objectName, int errorCount) {
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.getObjectName()).thenReturn(objectName);
        List<ObjectError> objectErrors = new ArrayList<>();
        for (int i = 0; i < errorCount; i++) {
            objectErrors.add(i, mock(ObjectError.class));
            when(objectErrors.get(i).getDefaultMessage()).thenReturn("test message " + i);
        }
        when(bindingResult.getAllErrors()).thenReturn(objectErrors);
        return bindingResult;
    }

}
