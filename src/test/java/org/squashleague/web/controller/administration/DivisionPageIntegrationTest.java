package org.squashleague.web.controller.administration;

import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.squashleague.domain.league.Division;
import org.squashleague.web.controller.WebAndDataIntegrationTest;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author jamesdbloom
 */
public class DivisionPageIntegrationTest extends WebAndDataIntegrationTest {

    private final static String OBJECT_NAME = "division";

    @Test
    public void shouldSaveDivisionWithNoErrors() throws Exception {
        mockMvc.perform(post("/" + OBJECT_NAME + "/save")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", "test name")
                .param("league", leagueOne.getId().toString())
        )
                .andExpect(redirectedUrl("/administration"));

        divisionDAO.delete(division.getId() + 1);
    }

    @Test
    public void shouldSaveDivisionWithErrors() throws Exception {
        mockMvc.perform(post("/" + OBJECT_NAME + "/save")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        )
                .andExpect(redirectedUrl("/administration#" + OBJECT_NAME + "s"))
                .andExpect(flash().attributeExists("bindingResult"))
                .andExpect(flash().attributeExists(OBJECT_NAME));
    }

    @Test
    public void shouldReturnPopulatedUpdateForm() throws Exception {
        MvcResult response = mockMvc.perform(get("/" + OBJECT_NAME + "/update/" + division.getId()).accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andReturn();

        DivisionUpdatePage DivisionUpdatePage = new DivisionUpdatePage(response);
        DivisionUpdatePage.hasDivisionFields(division.getId(), division.getVersion(), division.getName(), division.getLeague().getId());
    }

    @Test
    public void shouldUpdateDivisionNoErrors() throws Exception {
        mockMvc.perform(post("/" + OBJECT_NAME + "/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", "test name")
                .param("league", leagueOne.getId().toString())
        )
                .andExpect(redirectedUrl("/administration"));
    }

    @Test
    public void shouldGetPageWithLeagueError() throws Exception {
        // when
        MvcResult response = mockMvc.perform(post("/" + OBJECT_NAME + "/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", division.getId().toString())
                .param("version", division.getVersion().toString())
                .param("name", division.getName())
        )

                // then
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andReturn();

        DivisionUpdatePage DivisionUpdatePage = new DivisionUpdatePage(response);
        DivisionUpdatePage.hasErrors("division", 1);
        DivisionUpdatePage.hasDivisionFields(division.getId(), division.getVersion(), division.getName(), null);
    }

    @Test
    public void shouldGetPageWithNameError() throws Exception {
        // when
        MvcResult response = mockMvc.perform(post("/" + OBJECT_NAME + "/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", division.getId().toString())
                .param("version", division.getVersion().toString())
                .param("name", "four")
                .param("league", division.getLeague().getId().toString())
        )

                // then
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andReturn();

        DivisionUpdatePage DivisionUpdatePage = new DivisionUpdatePage(response);
        DivisionUpdatePage.hasErrors("division", 1);
        DivisionUpdatePage.hasDivisionFields(division.getId(), division.getVersion(), "four", division.getLeague().getId());
    }

    @Test
    public void shouldGetPageWithAllErrors() throws Exception {
        // when
        MvcResult response = mockMvc.perform(post("/" + OBJECT_NAME + "/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", division.getId().toString())
                .param("version", division.getVersion().toString())
                .param("name", "four")
        )

                // then
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andReturn();

        DivisionUpdatePage DivisionUpdatePage = new DivisionUpdatePage(response);
        DivisionUpdatePage.hasErrors("division", 2);
        DivisionUpdatePage.hasDivisionFields(division.getId(), division.getVersion(), "four", null);
    }

    @Test
    public void shouldDeleteDivision() throws Exception {
        // given
        Division division = new Division()
                .withName("to delete")
                .withLeague(leagueOne);
        divisionDAO.save(division);

        // when
        mockMvc.perform(get("/" + OBJECT_NAME + "/delete/" + division.getId())
                .accept(MediaType.TEXT_HTML)
        )
                // then
                .andExpect(redirectedUrl("/administration"));

        assertNull(divisionDAO.findById(division.getId()));
    }

}
