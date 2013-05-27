package org.squashleague.web.controller.administration;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;
import org.squashleague.configuration.RootConfiguration;
import org.squashleague.dao.league.DivisionDAO;
import org.squashleague.dao.league.RoundDAO;
import org.squashleague.domain.league.Division;
import org.squashleague.domain.league.Round;
import org.squashleague.service.configuration.ServiceConfiguration;
import org.squashleague.web.configuration.WebMvcConfiguration;
import org.squashleague.web.controller.PropertyMockingApplicationContextInitializer;
import org.squashleague.web.controller.administration.RoundUpdatePage;

import javax.annotation.Resource;
import java.util.Arrays;

import static org.mockito.Matchers.same;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * @author jamesdbloom
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextHierarchy({
        @ContextConfiguration(
                name = "root",
                classes = RootConfiguration.class
        ),
        @ContextConfiguration(
                name = "dispatcher",
                classes = {WebMvcConfiguration.class},
                initializers = PropertyMockingApplicationContextInitializer.class
        )
})
public class RoundPageIntegrationTest {

    private final static String OBJECT_NAME = "round";
    @Resource
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    @Resource
    private RoundDAO roundDAO;
    @Resource
    private DivisionDAO divisionDAO;
    private Division division;

    @Before
    public void setupFixture() {
        mockMvc = webAppContextSetup(webApplicationContext).build();
        division = (Division) new Division()
                .withName("division one")
                .withId(1l);
        when(divisionDAO.findById(division.getId())).thenReturn(division);
        when(divisionDAO.findAll()).thenReturn(Arrays.asList(division, (Division) new Division().withName("division two").withId(2l)));
    }

    @Test
    public void shouldSaveRoundWithNoErrors() throws Exception {
        mockMvc.perform(post("/" + OBJECT_NAME + "/save")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("startDate", new DateTime().plusDays(1).toString("yyyy-MM-dd"))
                .param("endDate", new DateTime().plusDays(2).toString("yyyy-MM-dd"))
                .param("division", division.getId().toString())
        )
                .andExpect(redirectedUrl("/administration"));
    }

    @Test
    public void shouldSaveRoundWithErrors() throws Exception {
        mockMvc.perform(post("/" + OBJECT_NAME + "/save")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        )
                .andExpect(redirectedUrl("/administration#" + OBJECT_NAME + "s"))
                .andExpect(flash().attributeExists("bindingResult"))
                .andExpect(flash().attributeExists(OBJECT_NAME));
    }

    @Test
    public void shouldReturnPopulatedUpdateForm() throws Exception {
        Long id = 1l;
        Round round = (Round) new Round()
                .withStartDate(new DateTime().plusDays(1))
                .withEndDate(new DateTime().plusDays(2))
                .withDivision(division)
                .withId(id);
        round.setVersion(5);
        when(roundDAO.findById(id)).thenReturn(round);


        MvcResult response = mockMvc.perform(get("/" + OBJECT_NAME + "/update/" + id).accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andReturn();

        RoundUpdatePage RoundUpdatePage = new RoundUpdatePage(response);
        RoundUpdatePage.hasRoundFields(round.getId(), round.getVersion(), round.getStartDate(), round.getEndDate(), round.getDivision().getId());
    }

    @Test
    public void shouldUpdateRoundNoErrors() throws Exception {
        mockMvc.perform(post("/" + OBJECT_NAME + "/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("startDate", new DateTime().plusDays(1).toString("yyyy-MM-dd"))
                .param("endDate", new DateTime().plusDays(2).toString("yyyy-MM-dd"))
                .param("division", division.getId().toString())
        )
                .andExpect(redirectedUrl("/administration"));
    }

    @Test
    public void shouldGetPageWithDivisionError() throws Exception {
        // given
        Round round = (Round) new Round()
                .withStartDate(new DateTime().plusDays(1))
                .withEndDate(new DateTime().plusDays(2))
                .withId(2l);
        round.setVersion(5);

        // when
        MvcResult response = mockMvc.perform(post("/" + OBJECT_NAME + "/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", round.getId().toString())
                .param("version", round.getVersion().toString())
                .param("startDate", round.getStartDate().toString("yyyy-MM-dd"))
                .param("endDate", round.getEndDate().toString("yyyy-MM-dd"))
        )

                // then
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andReturn();

        RoundUpdatePage RoundUpdatePage = new RoundUpdatePage(response);
        RoundUpdatePage.hasErrors("round", 1);
        RoundUpdatePage.hasRoundFields(round.getId(), round.getVersion(), round.getStartDate(), round.getEndDate(), null);
    }

    @Test
    public void shouldGetPageWithStartDateError() throws Exception {
        // given
        Round round = (Round) new Round()
                .withStartDate(null)
                .withEndDate(new DateTime().plusDays(2))
                .withDivision(division)
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
                .withDivision(division)
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
                .withDivision(division)
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
                .withDivision(division)
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
        Long id = 5l;

        // when
        mockMvc.perform(get("/" + OBJECT_NAME + "/delete/" + id)
                .accept(MediaType.TEXT_HTML)
        )
                // then
                .andExpect(redirectedUrl("/administration"));

        verify(roundDAO).delete(same(id));
    }

}
