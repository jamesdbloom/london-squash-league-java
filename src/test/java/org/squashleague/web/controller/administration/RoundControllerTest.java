package org.squashleague.web.controller.administration;

import org.joda.time.DateTime;
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
import org.squashleague.dao.league.LeagueDAO;
import org.squashleague.dao.league.RoundDAO;
import org.squashleague.domain.league.League;
import org.squashleague.domain.league.Round;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * @author jamesdbloom
 */
@RunWith(MockitoJUnitRunner.class)
public class RoundControllerTest {

    private final List<Round> rounds = new ArrayList<>();
    private final List<League> leagues = new ArrayList<>();

    @Mock
    private RoundDAO roundDAO;
    @Mock
    private LeagueDAO leagueDAO;
    @Mock
    private Environment environment;
    @InjectMocks
    private RoundController roundController = new RoundController();

    @Before
    public void setupFixture() {
        when(roundDAO.findAll()).thenReturn(rounds);
        when(leagueDAO.findAll()).thenReturn(leagues);
    }

    @Test
    public void shouldSaveRoundAndRedirectWhenNoBindingErrors() throws Exception {
        // given
        Round round = new Round();
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);

        // when
        String page = roundController.create(round, mock(BindingResult.class), redirectAttributes);

        // then
        verify(roundDAO).save(round);
        assertEquals("redirect:/administration", page);
    }

    @Test
    public void shouldAddBindingErrorsToSessionAndRedirect() throws Exception {
        // given
        Round round = new Round();
        String objectName = "round";
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);

        // when
        String page = roundController.create(round, bindingResult, redirectAttributes);

        // then
        verify(redirectAttributes).addFlashAttribute("bindingResult", bindingResult);
        verify(redirectAttributes).addFlashAttribute(objectName, round);
        assertEquals("redirect:/administration#" + objectName + "s", page);
    }


    @Test
    public void shouldAddDateErrorsToModelAndForwardWhenCreating() throws Exception {
        // given
        Round round = new Round();
        String objectName = "round";
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);
        BindingResult bindingResult = mock(BindingResult.class);
        String startDateAfterEndDate = "startDateAfterEndDate";
        when(environment.getProperty("validation.round.startDateAfterEndDate")).thenReturn(startDateAfterEndDate);
        round.withStartDate(new DateTime().plusDays(1)).withEndDate(new DateTime());

        // when
        String page = roundController.create(round, bindingResult, redirectAttributes);

        // then
        verify(bindingResult).addError(new ObjectError(objectName, startDateAfterEndDate));
        verify(redirectAttributes).addFlashAttribute("bindingResult", bindingResult);
        verify(redirectAttributes).addFlashAttribute(objectName, round);
        assertEquals("redirect:/administration#" + objectName + "s", page);
    }

    @Test
    public void shouldRetrieveCorrectRoundAndDisplayForm() throws Exception {
        // given
        Model uiModel = mock(Model.class);
        Long id = 1l;
        Round round = new Round();
        when(roundDAO.findById(id)).thenReturn(round);

        // when
        String page = roundController.updateForm(id, uiModel);

        // then
        verify(uiModel).addAttribute("round", round);
        verify(uiModel).addAttribute("leagues", leagues);
        verify(uiModel).addAttribute("environment", environment);
        assertEquals("page/administration/round/update", page);
    }

    @Test
    public void shouldUpdateRoundAndRedirectWhenNoBindingErrors() throws Exception {
        // given
        Model uiModel = mock(Model.class);
        Round round = new Round();
        BindingResult bindingResult = mock(BindingResult.class);

        // when
        String page = roundController.update(round, bindingResult, uiModel);

        // then
        verify(roundDAO).update(round);
        assertEquals("redirect:/administration", page);
    }

    @Test
    public void shouldAddBindingErrorsToModelAndForward() throws Exception {
        // given
        Model uiModel = mock(Model.class);
        Round round = new Round();
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);

        // when
        String page = roundController.update(round, bindingResult, uiModel);

        // then
        verify(uiModel).addAttribute("bindingResult", bindingResult);
        verify(uiModel).addAttribute("round", round);
        verify(uiModel).addAttribute("leagues", leagues);
        verify(uiModel).addAttribute("environment", environment);
        assertEquals("page/administration/round/update", page);
    }

    @Test
    public void shouldAddDateErrorsToModelAndForwardWhenUpdating() throws Exception {
        // given
        Model uiModel = mock(Model.class);
        Round round = new Round();
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(false);
        String startDateAfterEndDate = "startDateAfterEndDate";
        when(environment.getProperty("validation.round.startDateAfterEndDate")).thenReturn(startDateAfterEndDate);
        round.withStartDate(new DateTime().plusDays(1)).withEndDate(new DateTime());

        // when
        String page = roundController.update(round, bindingResult, uiModel);

        // then
        verify(bindingResult).addError(new ObjectError("round", startDateAfterEndDate));
        verify(uiModel).addAttribute("bindingResult", bindingResult);
        verify(uiModel).addAttribute("round", round);
        verify(uiModel).addAttribute("leagues", leagues);
        verify(uiModel).addAttribute("environment", environment);
        assertEquals("page/administration/round/update", page);
    }

    @Test
    public void shouldDeleteAndRedirect() throws Exception {
        // given
        Long id = 1l;

        // when
        String page = roundController.delete(id);

        // then
        verify(roundDAO).delete(id);
        assertEquals("redirect:/administration", page);
    }

}
