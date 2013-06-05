package org.squashleague.web.controller.administration;

import org.joda.time.DateTime;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.squashleague.domain.league.Round;
import org.squashleague.web.controller.WebAndDataIntegrationTest;

import static org.junit.Assert.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author jamesdbloom
 */
public class RoundPageIntegrationTest extends WebAndDataIntegrationTest {

    private final static String OBJECT_NAME = "round";

    @Test
    public void shouldSaveRoundWithNoErrors() throws Exception {
        // when
        mockMvc.perform(post("/" + OBJECT_NAME + "/save")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("startDate", new DateTime().plusDays(1).toString("yyyy-MM-dd"))
                .param("endDate", new DateTime().plusDays(2).toString("yyyy-MM-dd"))
                .param("division", divisionOne.getId().toString())
        )
                // then
                .andExpect(redirectedUrl("/administration"));

        roundDAO.delete(roundTwo.getId() + 1);
    }

    @Test
    public void shouldSaveRoundWithErrors() throws Exception {
        // when
        mockMvc.perform(post("/" + OBJECT_NAME + "/save")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        )
                // then
                .andExpect(redirectedUrl("/administration#" + OBJECT_NAME + "s"))
                .andExpect(flash().attributeExists("bindingResult"))
                .andExpect(flash().attributeExists(OBJECT_NAME));
    }

    @Test
    public void shouldReturnPopulatedUpdateForm() throws Exception {
        // when
        MvcResult response = mockMvc.perform(get("/" + OBJECT_NAME + "/update/" + roundOne.getId()).accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andReturn();

        // then
        new RoundUpdatePage(response).hasRoundFields(roundOne.getId(), roundOne.getVersion(), roundOne.getStartDate(), roundOne.getEndDate(), roundOne.getDivision().getId());
    }

    @Test
    public void shouldUpdateRoundNoErrors() throws Exception {
        // when
        mockMvc.perform(post("/" + OBJECT_NAME + "/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("startDate", new DateTime().plusDays(1).toString("yyyy-MM-dd"))
                .param("endDate", new DateTime().plusDays(2).toString("yyyy-MM-dd"))
                .param("division", divisionOne.getId().toString())
        )
                // then
                .andExpect(redirectedUrl("/administration"));
    }

    @Test
    public void shouldGetPageWithDivisionError() throws Exception {
        // when
        MvcResult response = mockMvc.perform(post("/" + OBJECT_NAME + "/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", roundOne.getId().toString())
                .param("version", roundOne.getVersion().toString())
                .param("startDate", roundOne.getStartDate().toString("yyyy-MM-dd"))
                .param("endDate", roundOne.getEndDate().toString("yyyy-MM-dd"))
        )
                // then
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andReturn();

        RoundUpdatePage RoundUpdatePage = new RoundUpdatePage(response);
        RoundUpdatePage.hasErrors("round", 1);
        RoundUpdatePage.hasRoundFields(roundOne.getId(), roundOne.getVersion(), roundOne.getStartDate(), roundOne.getEndDate(), null);
    }

    @Test
    public void shouldGetPageWithStartDateError() throws Exception {
        // given
        Round round = (Round) new Round()
                .withStartDate(null)
                .withEndDate(new DateTime().plusDays(2))
                .withDivision(divisionOne)
                .withId(2l);
        round.setVersion(5);

        // when
        MvcResult response = mockMvc.perform(post("/" + OBJECT_NAME + "/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", round.getId().toString())
                .param("version", round.getVersion().toString())
                .param("startDate", "")
                .param("endDate", round.getEndDate().toString("yyyy-MM-dd"))
                .param("division", round.getDivision().getId().toString())
        )

                // then
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andReturn();

        RoundUpdatePage RoundUpdatePage = new RoundUpdatePage(response);
        RoundUpdatePage.hasErrors("round", 1);
        RoundUpdatePage.hasRoundFields(round.getId(), round.getVersion(), round.getStartDate(), round.getEndDate(), round.getDivision().getId());
    }

    @Test
    public void shouldGetPageWithEndDateError() throws Exception {
        // given
        Round round = (Round) new Round()
                .withStartDate(new DateTime().plusDays(2))
                .withEndDate(null)
                .withDivision(divisionOne)
                .withId(2l);
        round.setVersion(5);

        // when
        MvcResult response = mockMvc.perform(post("/" + OBJECT_NAME + "/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", round.getId().toString())
                .param("version", round.getVersion().toString())
                .param("startDate", round.getStartDate().toString("yyyy-MM-dd"))
                .param("endDate", "")
                .param("division", round.getDivision().getId().toString())
        )

                // then
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andReturn();

        RoundUpdatePage RoundUpdatePage = new RoundUpdatePage(response);
        RoundUpdatePage.hasErrors("round", 1);
        RoundUpdatePage.hasRoundFields(round.getId(), round.getVersion(), round.getStartDate(), round.getEndDate(), round.getDivision().getId());
    }

    @Test
    public void shouldGetPageWithStartDateAfterEndDateError() throws Exception {
        // given
        Round round = (Round) new Round()
                .withStartDate(new DateTime().plusDays(3))
                .withEndDate(new DateTime().plusDays(2))
                .withDivision(divisionOne)
                .withId(2l);
        round.setVersion(5);

        // when
        MvcResult response = mockMvc.perform(post("/" + OBJECT_NAME + "/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", round.getId().toString())
                .param("version", round.getVersion().toString())
                .param("startDate", round.getStartDate().toString("yyyy-MM-dd"))
                .param("endDate", round.getEndDate().toString("yyyy-MM-dd"))
                .param("division", round.getDivision().getId().toString())
        )

                // then
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andReturn();

        RoundUpdatePage RoundUpdatePage = new RoundUpdatePage(response);
        RoundUpdatePage.hasErrors("round", 1);
        RoundUpdatePage.hasRoundFields(round.getId(), round.getVersion(), round.getStartDate(), round.getEndDate(), round.getDivision().getId());
    }

    @Test
    public void shouldGetPageWithPastDatesError() throws Exception {
        // given
        Round round = (Round) new Round()
                .withStartDate(new DateTime().minusDays(3))
                .withEndDate(new DateTime().minusDays(2))
                .withDivision(divisionOne)
                .withId(2l);
        round.setVersion(5);

        // when
        MvcResult response = mockMvc.perform(post("/" + OBJECT_NAME + "/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", round.getId().toString())
                .param("version", round.getVersion().toString())
                .param("startDate", round.getStartDate().toString("yyyy-MM-dd"))
                .param("endDate", round.getEndDate().toString("yyyy-MM-dd"))
                .param("division", round.getDivision().getId().toString())
        )

                // then
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andReturn();

        RoundUpdatePage RoundUpdatePage = new RoundUpdatePage(response);
        RoundUpdatePage.hasErrors("round", 2);
        RoundUpdatePage.hasRoundFields(round.getId(), round.getVersion(), round.getStartDate(), round.getEndDate(), round.getDivision().getId());
    }

    @Test
    public void shouldGetPageWithAllErrors() throws Exception {
        // given
        Round round = (Round) new Round()
                .withStartDate(null)
                .withEndDate(null)
                .withId(2l);
        round.setVersion(5);

        // when
        MvcResult response = mockMvc.perform(post("/" + OBJECT_NAME + "/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", round.getId().toString())
                .param("version", round.getVersion().toString())
                .param("startDate", "")
                .param("endDate", "")
        )

                // then
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andReturn();

        RoundUpdatePage RoundUpdatePage = new RoundUpdatePage(response);
        RoundUpdatePage.hasErrors("round", 3);
        RoundUpdatePage.hasRoundFields(round.getId(), round.getVersion(), round.getStartDate(), round.getEndDate(), null);
    }

    @Test
    public void shouldDeleteRound() throws Exception {
        // given
        Round round = new Round()
                .withDivision(divisionOne)
                .withStartDate(new DateTime().plusDays(1))
                .withEndDate(new DateTime().plusDays(10));
        roundDAO.save(round);

        // when
        mockMvc.perform(get("/" + OBJECT_NAME + "/delete/" + round.getId())
                .accept(MediaType.TEXT_HTML)
        )
                // then
                .andExpect(redirectedUrl("/administration"));

        assertNull(roundDAO.findById(round.getId()));
    }

}
