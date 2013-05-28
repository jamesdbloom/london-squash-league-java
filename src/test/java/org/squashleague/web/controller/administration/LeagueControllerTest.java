package org.squashleague.web.controller.administration;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.env.Environment;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.squashleague.dao.league.ClubDAO;
import org.squashleague.dao.league.LeagueDAO;
import org.squashleague.domain.league.Club;
import org.squashleague.domain.league.League;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * @author jamesdbloom
 */
@RunWith(MockitoJUnitRunner.class)
public class LeagueControllerTest {

    private final List<League> leagues = new ArrayList<>();
    private final List<Club> clubs = new ArrayList<>();

    @Mock
    private LeagueDAO leagueDAO;
    @Mock
    private ClubDAO clubDAO;
    @Mock
    private Environment environment;
    @InjectMocks
    private LeagueController leagueController = new LeagueController();

    @Before
    public void setupFixture() {
        when(leagueDAO.findAll()).thenReturn(leagues);
        when(clubDAO.findAll()).thenReturn(clubs);
    }

    @Test
    public void shouldSaveLeagueAndRedirectWhenNoBindingErrors() throws Exception {
        // given
        League league = new League();
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);

        // when
        String page = leagueController.create(league, mock(BindingResult.class), redirectAttributes);

        // then
        verify(leagueDAO).save(league);
        assertEquals("redirect:/administration", page);
    }

    @Test
    public void shouldAddBindingErrorsToSessionAndRedirect() throws Exception {
        // given
        League league = new League();
        String objectName = "league";
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);

        // when
        String page = leagueController.create(league, bindingResult, redirectAttributes);

        // then
        verify(redirectAttributes).addFlashAttribute("bindingResult", bindingResult);
        verify(redirectAttributes).addFlashAttribute(objectName, league);
        assertEquals("redirect:/administration#" + objectName + "s", page);
    }

    @Test
    public void shouldRetrieveCorrectLeagueAndDisplayForm() throws Exception {
        // given
        Model uiModel = mock(Model.class);
        Long id = 1l;
        League league = new League();
        when(leagueDAO.findById(id)).thenReturn(league);

        // when
        String page = leagueController.updateForm(id, uiModel);

        // then
        verify(uiModel).addAttribute("league", league);
        verify(uiModel).addAttribute("clubs", clubs);
        verify(uiModel).addAttribute("environment", environment);
        assertEquals("page/administration/league/update", page);
    }

    @Test
    public void shouldUpdateLeagueAndRedirectWhenNoBindingErrors() throws Exception {
        // given
        Model uiModel = mock(Model.class);
        League league = new League();
        BindingResult bindingResult = mock(BindingResult.class);

        // when
        String page = leagueController.update(league, bindingResult, uiModel);

        // then
        verify(leagueDAO).update(league);
        assertEquals("redirect:/administration", page);
    }

    @Test
    public void shouldAddBindingErrorsToModelAndForward() throws Exception {
        // given
        Model uiModel = mock(Model.class);
        League league = new League();
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);

        // when
        String page = leagueController.update(league, bindingResult, uiModel);

        // then
        verify(uiModel).addAttribute("bindingResult", bindingResult);
        verify(uiModel).addAttribute("league", league);
        verify(uiModel).addAttribute("clubs", clubs);
        verify(uiModel).addAttribute("environment", environment);
        assertEquals("page/administration/league/update", page);
    }

    @Test
    public void shouldDeleteAndRedirect() throws Exception {
        // given
        Long id = 1l;

        // when
        String page = leagueController.delete(id);

        // then
        verify(leagueDAO).delete(id);
        assertEquals("redirect:/administration", page);
    }

}
