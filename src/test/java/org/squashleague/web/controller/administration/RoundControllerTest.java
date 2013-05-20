package org.squashleague.web.controller.administration;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.VerboseMockitoJUnitRunner;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.squashleague.dao.league.RoundDAO;
import org.squashleague.domain.league.Round;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * @author jamesdbloom
 */
@RunWith(VerboseMockitoJUnitRunner.class)
public class RoundControllerTest {

    private final List<Round> rounds = new ArrayList<>();

    @Mock
    private RoundDAO roundDAO;
    @InjectMocks
    private RoundController roundController = new RoundController();

    @Before
    public void setupFixture() {
        when(roundDAO.findAll()).thenReturn(rounds);
    }

    @Test
    public void shouldSaveRoundAndRedirectWhenNoBindingErrors() throws Exception {
        // given
        Round round = new Round();
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);

        // when
        String page = roundController.create(round, mock(BindingResult.class), redirectAttributes);

        // then
        verify(roundDAO).save(same(round));
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
        verify(redirectAttributes).addFlashAttribute(eq("bindingResult"), same(bindingResult));
        verify(redirectAttributes).addFlashAttribute(eq(objectName), same(round));
        assertEquals("redirect:/administration#" + objectName + "s", page);
    }

    @Test
    public void shouldRetrieveCorrectUserAndDisplayForm() throws Exception {
        // given
        Model uiModel = mock(Model.class);
        Long id = 1l;
        Round round = new Round();
        when(roundDAO.findOne(same(id))).thenReturn(round);

        // when
        String page = roundController.updateForm(id, uiModel);

        // then
        verify(uiModel).addAttribute(eq("round"), same(round));
        assertEquals("page/round/update", page);
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
        verify(roundDAO).update(same(round));
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
        verify(uiModel).addAttribute(eq("round"), same(round));
        assertEquals("page/round/update", page);
    }

    @Test
    public void shouldDeleteAndRedirect() throws Exception {
        // given
        Long id = 1l;

        // when
        String page = roundController.delete(id);

        // then
        verify(roundDAO).delete(same(id));
        assertEquals("redirect:/administration", page);
    }

}