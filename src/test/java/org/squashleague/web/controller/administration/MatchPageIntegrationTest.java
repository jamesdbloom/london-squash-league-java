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
import org.squashleague.dao.league.MatchDAO;
import org.squashleague.dao.league.PlayerDAO;
import org.squashleague.dao.league.RoundDAO;
import org.squashleague.domain.account.User;
import org.squashleague.domain.league.*;
import org.squashleague.service.configuration.ServiceConfiguration;
import org.squashleague.web.configuration.WebMvcConfiguration;
import org.squashleague.web.controller.PropertyMockingApplicationContextInitializer;

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
public class MatchPageIntegrationTest {

    private final static String OBJECT_NAME = "match";
    @Resource
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    @Resource
    private MatchDAO matchDAO;
    @Resource
    private RoundDAO roundDAO;
    @Resource
    private PlayerDAO playerDAO;
    private Round round;
    private Player playerOne;
    private Player playerTwo;

    @Before
    public void setupFixture() {
        mockMvc = webAppContextSetup(webApplicationContext).build();
        round = (Round) new Round()
                .withStartDate(new DateTime().plusDays(1))
                .withEndDate(new DateTime().plusDays(2))
                .withDivision(
                        new Division()
                                .withName("division name")
                                .withLeague(
                                        new League()
                                                .withName("league name")
                                                .withClub(
                                                        new Club()
                                                                .withName("club name")
                                                )
                                )
                )
                .withId(1l);
        when(roundDAO.findById(round.getId())).thenReturn(round);
        when(roundDAO.findAll()).thenReturn(
                Arrays.asList(
                        round,
                        (Round) new Round()
                                .withStartDate(new DateTime().plusDays(3))
                                .withEndDate(new DateTime().plusDays(4))
                                .withDivision(
                                        new Division()
                                                .withName("division name")
                                                .withLeague(
                                                        new League()
                                                                .withName("league name")
                                                                .withClub(
                                                                        new Club()
                                                                                .withName("club name")
                                                                )
                                                )
                                )
                                .withId(2l)
                )
        );
        playerOne = (Player) new Player()
                .withUser(
                        new User()
                                .withName("player one")
                )
                .withId(1l);
        playerTwo = (Player) new Player()
                .withUser(
                        new User()
                                .withName("player two")
                )
                .withId(2l);
        when(playerDAO.findById(playerOne.getId())).thenReturn(playerOne);
        when(playerDAO.findById(playerTwo.getId())).thenReturn(playerTwo);
        when(playerDAO.findAll()).thenReturn(
                Arrays.asList(
                        playerOne,
                        playerTwo,
                        (Player) new Player()
                                .withUser(
                                        new User()
                                                .withName("player three")
                                )
                                .withId(3l)
                )
        );
    }

    @Test
    public void shouldSaveMatchWithNoErrors() throws Exception {
        mockMvc.perform(post("/" + OBJECT_NAME + "/save")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("playerOne", playerOne.getId().toString())
                .param("playerTwo", playerTwo.getId().toString())
                .param("round", round.getId().toString())
        )
                .andExpect(redirectedUrl("/administration"));
    }

    @Test
    public void shouldSaveMatchWithErrors() throws Exception {
        mockMvc.perform(post("/" + OBJECT_NAME + "/save")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        )
                .andExpect(redirectedUrl("/administration#" + OBJECT_NAME + "es"))
                .andExpect(flash().attributeExists("bindingResult"))
                .andExpect(flash().attributeExists(OBJECT_NAME));
    }

    @Test
    public void shouldReturnPopulatedUpdateForm() throws Exception {
        Long id = 1l;
        Match match = (Match) new Match()
                .withPlayerOne(playerOne)
                .withPlayerTwo(playerTwo)
                .withRound(round)
                .withId(id);
        match.setVersion(5);
        when(matchDAO.findById(id)).thenReturn(match);

        MvcResult response = mockMvc.perform(get("/" + OBJECT_NAME + "/update/" + id).accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andReturn();

        MatchUpdatePage MatchUpdatePage = new MatchUpdatePage(response);
        MatchUpdatePage.hasMatchFields(match.getId(), match.getVersion(), match.getPlayerOne().getId(), match.getPlayerTwo().getId(), match.getRound().getId());
    }

    @Test
    public void shouldUpdateMatchNoErrors() throws Exception {
        mockMvc.perform(post("/" + OBJECT_NAME + "/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("playerOne", playerOne.getId().toString())
                .param("playerTwo", playerTwo.getId().toString())
                .param("round", round.getId().toString())
        )
                .andExpect(redirectedUrl("/administration"));
    }

    @Test
    public void shouldGetPageWithRoundError() throws Exception {
        // given
        Match match = (Match) new Match()
                .withPlayerOne(playerOne)
                .withPlayerTwo(playerTwo)
                .withId(2l);
        match.setVersion(5);

        // when
        MvcResult response = mockMvc.perform(post("/" + OBJECT_NAME + "/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", match.getId().toString())
                .param("version", match.getVersion().toString())
                .param("playerOne", match.getPlayerOne().getId().toString())
                .param("playerTwo", match.getPlayerTwo().getId().toString())
        )

                // then
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andReturn();

        MatchUpdatePage MatchUpdatePage = new MatchUpdatePage(response);
        MatchUpdatePage.hasErrors("match", 1);
        MatchUpdatePage.hasMatchFields(match.getId(), match.getVersion(), match.getPlayerOne().getId(), match.getPlayerTwo().getId(), null);
    }

    @Test
    public void shouldGetPageWithPlayerOneError() throws Exception {
        // given
        Match match = (Match) new Match()
                .withPlayerOne(null)
                .withPlayerTwo(playerTwo)
                .withRound(round)
                .withId(2l);
        match.setVersion(5);

        // when
        MvcResult response = mockMvc.perform(post("/" + OBJECT_NAME + "/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", match.getId().toString())
                .param("version", match.getVersion().toString())
                .param("playerOne", "")
                .param("playerTwo", match.getPlayerTwo().getId().toString())
                .param("round", match.getRound().getId().toString())
        )

                // then
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andReturn();

        MatchUpdatePage MatchUpdatePage = new MatchUpdatePage(response);
        MatchUpdatePage.hasErrors("match", 1);
        MatchUpdatePage.hasMatchFields(match.getId(), match.getVersion(), null, match.getPlayerTwo().getId(), match.getRound().getId());
    }

    @Test
    public void shouldGetPageWithPlayerTwoError() throws Exception {
        // given
        Match match = (Match) new Match()
                .withPlayerOne(playerOne)
                .withPlayerTwo(null)
                .withRound(round)
                .withId(2l);
        match.setVersion(5);

        // when
        MvcResult response = mockMvc.perform(post("/" + OBJECT_NAME + "/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", match.getId().toString())
                .param("version", match.getVersion().toString())
                .param("playerOne", match.getPlayerOne().getId().toString())
                .param("playerTwo", "")
                .param("round", match.getRound().getId().toString())
        )

                // then
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andReturn();

        MatchUpdatePage MatchUpdatePage = new MatchUpdatePage(response);
        MatchUpdatePage.hasErrors("match", 1);
        MatchUpdatePage.hasMatchFields(match.getId(), match.getVersion(), match.getPlayerOne().getId(), null, match.getRound().getId());
    }

    @Test
    public void shouldGetPageWithPlayersIdenticalError() throws Exception {
        // given
        Match match = (Match) new Match()
                .withPlayerOne(playerOne)
                .withPlayerTwo(playerOne)
                .withRound(round)
                .withId(2l);
        match.setVersion(5);

        // when
        MvcResult response = mockMvc.perform(post("/" + OBJECT_NAME + "/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", match.getId().toString())
                .param("version", match.getVersion().toString())
                .param("playerOne", match.getPlayerOne().getId().toString())
                .param("playerTwo", match.getPlayerOne().getId().toString())
                .param("round", match.getRound().getId().toString())
        )

                // then
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andReturn();

        MatchUpdatePage MatchUpdatePage = new MatchUpdatePage(response);
        MatchUpdatePage.hasErrors("match", 1);
        MatchUpdatePage.hasMatchFields(match.getId(), match.getVersion(), match.getPlayerOne().getId(), match.getPlayerTwo().getId(), match.getRound().getId());
    }

    @Test
    public void shouldGetPageWithAllErrors() throws Exception {
        // given
        Match match = (Match) new Match()
                .withId(2l);
        match.setVersion(5);

        // when
        MvcResult response = mockMvc.perform(post("/" + OBJECT_NAME + "/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", match.getId().toString())
                .param("version", match.getVersion().toString())
        )

                // then
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andReturn();

        MatchUpdatePage MatchUpdatePage = new MatchUpdatePage(response);
        MatchUpdatePage.hasErrors("match", 3);
        MatchUpdatePage.hasMatchFields(match.getId(), match.getVersion(), null, null, null);
    }

    @Test
    public void shouldDeleteMatch() throws Exception {
        // given
        Long id = 5l;

        // when
        mockMvc.perform(get("/" + OBJECT_NAME + "/delete/" + id)
                .accept(MediaType.TEXT_HTML)
        )
                // then
                .andExpect(redirectedUrl("/administration"));

        verify(matchDAO).delete(same(id));
    }

}
