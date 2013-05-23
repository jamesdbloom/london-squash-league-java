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
import org.squashleague.dao.league.MatchDAO;
import org.squashleague.dao.league.PlayerDAO;
import org.squashleague.dao.league.RoundDAO;
import org.squashleague.domain.league.Match;
import org.squashleague.domain.league.Player;
import org.squashleague.domain.league.Round;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

/**
 * @author jamesdbloom
 */
@RunWith(MockitoJUnitRunner.class)
public class MatchControllerTest {

    private final List<Match> matches = new ArrayList<>();
    private final List<Round> rounds = new ArrayList<>();
    private final List<Player> players = new ArrayList<>();
    @Mock
    private MatchDAO matchDAO;
    @Mock
    private RoundDAO roundDAO;
    @Mock
    private PlayerDAO playerDAO;
    @Resource
    private Environment environment;
    @InjectMocks
    private MatchController matchController = new MatchController();

    @Before
    public void setupFixture() {
        when(matchDAO.findAll()).thenReturn(matches);
        when(roundDAO.findAll()).thenReturn(rounds);
        when(playerDAO.findAll()).thenReturn(players);
    }

    @Test
    public void shouldSaveMatchAndRedirectWhenNoBindingErrors() throws Exception {
        // given
        Match match = new Match();
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);

        // when
        String page = matchController.create(match, mock(BindingResult.class), redirectAttributes);

        // then
        verify(matchDAO).save(same(match));
        assertEquals("redirect:/administration", page);
    }

    @Test
    public void shouldAddBindingErrorsToSessionAndRedirect() throws Exception {
        // given
        Match match = new Match();
        String objectName = "match";
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);

        // when
        String page = matchController.create(match, bindingResult, redirectAttributes);

        // then
        verify(redirectAttributes).addFlashAttribute(eq("bindingResult"), same(bindingResult));
        verify(redirectAttributes).addFlashAttribute(eq(objectName), same(match));
        assertEquals("redirect:/administration#" + objectName + "es", page);
    }

    @Test
    public void shouldRetrieveCorrectMatchAndDisplayForm() throws Exception {
        // given
        Model uiModel = mock(Model.class);
        Long id = 1l;
        Match match = new Match();
        when(matchDAO.findById(same(id))).thenReturn(match);

        // when
        String page = matchController.updateForm(id, uiModel);

        // then
        verify(uiModel).addAttribute(eq("match"), same(match));
        verify(uiModel).addAttribute(eq("rounds"), same(rounds));
        verify(uiModel).addAttribute(eq("players"), same(players));
        verify(uiModel).addAttribute(eq("environment"), same(environment));
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
        verify(uiModel).addAttribute(eq("rounds"), same(rounds));
        verify(uiModel).addAttribute(eq("players"), same(players));
        verify(uiModel).addAttribute(eq("environment"), same(environment));
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
