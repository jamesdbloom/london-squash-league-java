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
        getAdministrationPage("user", 2, userOne).hasUserFields(userOne.getName(), userOne.getEmail(), userOne.getMobile(), userOne.getMobilePrivacy(), userOne.getRoleNames());
    }

    @Test
    public void shouldGetPageWithClubErrors() throws Exception {
        getAdministrationPage("club", 2, club).hasClubFields(club.getName(), club.getAddress());
    }

    @Test
    public void shouldGetPageWithLeagueErrors() throws Exception {
        getAdministrationPage("league", 2, leagueOne).hasLeagueFields(leagueOne.getName());
    }

    @Test
    public void shouldGetPageWithDivisionErrors() throws Exception {
        getAdministrationPage("division", 2, division).hasDivisionFields(division.getName());
    }

    @Test
    public void shouldGetPageWithRoundErrors() throws Exception {
        getAdministrationPage("round", 2, round).hasRoundFields(round.getDivision().getId(), round.getStartDate(), round.getEndDate());
    }

    @Test
    public void shouldGetPageWithPlayerErrors() throws Exception {
        getAdministrationPage("player", 2, playerOne).hasPlayerFields(playerOne.getUser().getId(), playerOne.getCurrentDivision().getId(), playerOne.getStatus());
    }

    @Test
    public void shouldGetPageWithMatchErrors() throws Exception {
        getAdministrationPage("match", 2, matchOne).hasMatchFields(matchOne.getRound().getId(), matchOne.getPlayerOne().getId(), matchOne.getPlayerTwo().getId(), matchOne.getScore());
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
