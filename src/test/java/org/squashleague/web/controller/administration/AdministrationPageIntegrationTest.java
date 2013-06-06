package org.squashleague.web.controller.administration;

import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
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
        getAdministrationPage("user", 2, users.get(0)).hasUserFields(users.get(0).getName(), users.get(0).getEmail(), users.get(0).getMobile(), users.get(0).getMobilePrivacy(), users.get(0).getRoleNames());
    }

    @Test
    public void shouldGetPageWithClubErrors() throws Exception {
        getAdministrationPage("club", 2, clubs.get(0)).hasClubFields(clubs.get(0).getName(), clubs.get(0).getAddress());
    }

    @Test
    public void shouldGetPageWithLeagueErrors() throws Exception {
        getAdministrationPage("league", 2, leagues.get(0)).hasLeagueFields(leagues.get(0).getName());
    }

    @Test
    public void shouldGetPageWithDivisionErrors() throws Exception {
        getAdministrationPage("division", 2, divisions.get(0)).hasDivisionFields(divisions.get(0).getName());
    }

    @Test
    public void shouldGetPageWithRoundErrors() throws Exception {
        getAdministrationPage("round", 2, rounds.get(0)).hasRoundFields(rounds.get(0).getDivision().getId(), rounds.get(0).getStartDate(), rounds.get(0).getEndDate());
    }

    @Test
    public void shouldGetPageWithPlayerErrors() throws Exception {
        getAdministrationPage("player", 2, players.get(0)).hasPlayerFields(players.get(0).getUser().getId(), players.get(0).getCurrentDivision().getId(), players.get(0).getStatus());
    }

    @Test
    public void shouldGetPageWithMatchErrors() throws Exception {
        getAdministrationPage("match", 2, matches.get(0)).hasMatchFields(matches.get(0).getRound().getId(), matches.get(0).getPlayerOne().getId(), matches.get(0).getPlayerTwo().getId(), matches.get(0).getScore());
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
