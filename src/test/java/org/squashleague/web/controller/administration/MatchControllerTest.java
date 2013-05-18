package org.squashleague.web.controller.administration;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.VerboseMockitoJUnitRunner;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.squashleague.dao.league.MatchDAO;
import org.squashleague.domain.league.Match;

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
public class MatchControllerTest {

    private final List<Match> matchs = new ArrayList<>();

    @Mock
    private MatchDAO matchDAO;
    @InjectMocks
    private MatchController matchController = new MatchController();

    @Before
    public void setupFixture() {
        when(matchDAO.findAll()).thenReturn(matchs);
    }

    @Test
    public void shouldSaveMatchAndRedirectWhenNoBindingErrors() throws Exception {
        // given
        Match match = new Match();
        HttpSession session = mock(HttpSession.class);

        // when
        String page = matchController.create(match, mock(BindingResult.class), session);

        // then
        verify(session).removeAttribute("bindingResult");
        verify(matchDAO).save(same(match));
        assertEquals("redirect:/administration", page);
    }

    @Test
    public void shouldAddBindingErrorsToSessionAndRedirect() throws Exception {
        // given
        Match match = new Match();
        HttpSession session = mock(HttpSession.class);
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);

        // when
        String page = matchController.create(match, bindingResult, session);

        // then
        verify(session).setAttribute(eq("bindingResult"), same(bindingResult));
        verify(session).setAttribute(eq("match"), same(match));
        assertEquals("redirect:/administration", page);
    }

    @Test
    public void shouldRetrieveCorrectUserAndDisplayForm() throws Exception {
        // given
        Model uiModel = mock(Model.class);
        Long id = 1l;
        Match match = new Match();
        when(matchDAO.findOne(same(id))).thenReturn(match);

        // when
        String page = matchController.updateForm(id, uiModel);

        // then
        verify(uiModel).addAttribute(eq("match"), same(match));
        assertEquals("page/match/update", page);
    }

    @Test
    public void shouldUpdateMatchAndRedirectWhenNoBindingErrors() throws Exception {
        // given
        Model uiModel = mock(Model.class);
        Match match = new Match();
        BindingResult bindingResult = mock(BindingResult.class);

        // when
        String page = matchController.update(match, bindingResult, uiModel);

        // then
        verify(matchDAO).update(same(match));
        assertEquals("redirect:/administration", page);
    }

    @Test
    public void shouldAddBindingErrorsToModelAndForward() throws Exception {
        // given
        Model uiModel = mock(Model.class);
        Match match = new Match();
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);

        // when
        String page = matchController.update(match, bindingResult, uiModel);

        // then
        verify(uiModel).addAttribute(eq("match"), same(match));
        assertEquals("page/match/update", page);
    }

    @Test
    public void shouldDeleteAndRedirect() throws Exception {
        // given
        Long id = 1l;

        // when
        String page = matchController.delete(id);

        // then
        verify(matchDAO).delete(same(id));
        assertEquals("redirect:/administration", page);
    }

}
