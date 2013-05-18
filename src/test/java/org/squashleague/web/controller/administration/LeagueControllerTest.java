package org.squashleague.web.controller.administration;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.VerboseMockitoJUnitRunner;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.squashleague.dao.league.LeagueDAO;
import org.squashleague.domain.league.League;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

/**
 * @author jamesdbloom
 */
@RunWith(VerboseMockitoJUnitRunner.class)
public class LeagueControllerTest {

    private final List<League> leagues = new ArrayList<>();

    @Mock
    private LeagueDAO leagueDAO;
    @InjectMocks
    private LeagueController leagueController = new LeagueController();

    @Before
    public void setupFixture() {
        when(leagueDAO.findAll()).thenReturn(leagues);
    }

    @Test
    public void shouldSaveLeagueAndRedirectWhenNoBindingErrors() throws Exception {
        // given
        League league = new League();
        HttpSession session = mock(HttpSession.class);

        // when
        String page = leagueController.create(league, mock(BindingResult.class), session);

        // then
        verify(session).removeAttribute("bindingResult");
        verify(leagueDAO).save(same(league));
        assertEquals("redirect:/administration", page);
    }

    @Test
    public void shouldAddBindingErrorsToSessionAndRedirect() throws Exception {
        // given
        League league = new League();
        HttpSession session = mock(HttpSession.class);
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);

        // when
        String page = leagueController.create(league, bindingResult, session);

        // then
        verify(session).setAttribute(eq("bindingResult"), same(bindingResult));
        verify(session).setAttribute(eq("league"), same(league));
        assertEquals("redirect:/administration", page);
    }

    @Test
    public void shouldRetrieveCorrectUserAndDisplayForm() throws Exception {
        // given
        Model uiModel = mock(Model.class);
        Long id = 1l;
        League league = new League();
        when(leagueDAO.findOne(same(id))).thenReturn(league);

        // when
        String page = leagueController.updateForm(id, uiModel);

        // then
        verify(uiModel).addAttribute(eq("league"), same(league));
        assertEquals("page/league/update", page);
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
        verify(leagueDAO).update(same(league));
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
        verify(uiModel).addAttribute(eq("league"), same(league));
        assertEquals("page/league/update", page);
    }

    @Test
    public void shouldDeleteAndRedirect() throws Exception {
        // given
        Long id = 1l;

        // when
        String page = leagueController.delete(id);

        // then
        verify(leagueDAO).delete(same(id));
        assertEquals("redirect:/administration", page);
    }

}
