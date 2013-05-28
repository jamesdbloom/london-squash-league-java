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
import org.springframework.validation.ObjectError;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.squashleague.dao.league.MatchDAO;
import org.squashleague.dao.league.PlayerDAO;
import org.squashleague.dao.league.RoundDAO;
import org.squashleague.domain.league.Match;
import org.squashleague.domain.league.Player;
import org.squashleague.domain.league.Round;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
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
    @Mock
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
        verify(matchDAO).save(match);
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
        verify(redirectAttributes).addFlashAttribute("bindingResult", bindingResult);
        verify(redirectAttributes).addFlashAttribute(objectName, match);
        assertEquals("redirect:/administration#" + objectName + "es", page);
    }


    @Test
    public void shouldAddPlayersErrorsToSessionAndRedirectWhenCreating() throws Exception {
        // given
        Match match = new Match();
        String objectName = "match";
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);
        BindingResult bindingResult = mock(BindingResult.class);
        String playersIdentical = "playersIdentical";
        when(environment.getProperty("validation.match.playersIdentical")).thenReturn(playersIdentical);
        match.withPlayerOne(new Player()).withPlayerTwo(match.getPlayerOne());

        // when
        String page = matchController.create(match, bindingResult, redirectAttributes);

        // then
        verify(bindingResult).addError(new ObjectError(objectName, playersIdentical));
        verify(redirectAttributes).addFlashAttribute("bindingResult", bindingResult);
        verify(redirectAttributes).addFlashAttribute(objectName, match);
        assertEquals("redirect:/administration#" + objectName + "es", page);
    }

    @Test
    public void shouldRetrieveCorrectMatchAndDisplayForm() throws Exception {
        // given
        Model uiModel = mock(Model.class);
        Long id = 1l;
        Match match = new Match();
        when(matchDAO.findById(id)).thenReturn(match);

        // when
        String page = matchController.updateForm(id, uiModel);

        // then
        verify(uiModel).addAttribute("match", match);
        verify(uiModel).addAttribute("rounds", rounds);
        verify(uiModel).addAttribute("players", players);
        verify(uiModel).addAttribute("environment", environment);
        assertEquals("page/administration/match/update", page);
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
        verify(matchDAO).update(match);
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
        verify(uiModel).addAttribute("bindingResult", bindingResult);
        verify(uiModel).addAttribute("match", match);
        verify(uiModel).addAttribute("rounds", rounds);
        verify(uiModel).addAttribute("players", players);
        verify(uiModel).addAttribute("environment", environment);
        assertEquals("page/administration/match/update", page);
    }

    @Test
    public void shouldAddPlayersErrorsToSessionAndRedirectWhenUpdating() throws Exception {
        // given
        Match match = new Match();
        String objectName = "match";
        Model uiModel = mock(Model.class);
        BindingResult bindingResult = mock(BindingResult.class);
        String playersIdentical = "playersIdentical";
        when(environment.getProperty("validation.match.playersIdentical")).thenReturn(playersIdentical);
        match.withPlayerOne(new Player()).withPlayerTwo(match.getPlayerOne());

        // when
        String page = matchController.update(match, bindingResult, uiModel);

        // then
        verify(bindingResult).addError(new ObjectError(objectName, playersIdentical));
        verify(uiModel).addAttribute("bindingResult", bindingResult);
        verify(uiModel).addAttribute("match", match);
        verify(uiModel).addAttribute("rounds", rounds);
        verify(uiModel).addAttribute("players", players);
        verify(uiModel).addAttribute("environment", environment);
        assertEquals("page/administration/match/update", page);
    }

    @Test
    public void shouldDeleteAndRedirect() throws Exception {
        // given
        Long id = 1l;

        // when
        String page = matchController.delete(id);

        // then
        verify(matchDAO).delete(id);
        assertEquals("redirect:/administration", page);
    }

}
