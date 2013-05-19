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
import org.squashleague.dao.league.DivisionDAO;
import org.squashleague.domain.league.Division;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

/**
 * @author jamesdbloom
 */
@RunWith(VerboseMockitoJUnitRunner.class)
public class DivisionControllerTest {

    private final List<Division> divisions = new ArrayList<>();

    @Mock
    private DivisionDAO divisionDAO;
    @InjectMocks
    private DivisionController divisionController = new DivisionController();

    @Before
    public void setupFixture() {
        when(divisionDAO.findAll()).thenReturn(divisions);
    }

    @Test
    public void shouldSaveDivisionAndRedirectWhenNoBindingErrors() throws Exception {
        // given
        Division division = new Division();
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);

        // when
        String page = divisionController.create(division, mock(BindingResult.class), redirectAttributes);

        // then
        verify(divisionDAO).save(same(division));
        assertEquals("redirect:/administration", page);
    }

    @Test
    public void shouldAddBindingErrorsToSessionAndRedirect() throws Exception {
        // given
        Division division = new Division();
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);

        // when
        String page = divisionController.create(division, bindingResult, redirectAttributes);

        // then
        verify(redirectAttributes).addFlashAttribute(eq("bindingResult"), same(bindingResult));
        verify(redirectAttributes).addFlashAttribute(eq("division"), same(division));
        assertEquals("redirect:/administration", page);
    }

    @Test
    public void shouldRetrieveCorrectUserAndDisplayForm() throws Exception {
        // given
        Model uiModel = mock(Model.class);
        Long id = 1l;
        Division division = new Division();
        when(divisionDAO.findOne(same(id))).thenReturn(division);

        // when
        String page = divisionController.updateForm(id, uiModel);

        // then
        verify(uiModel).addAttribute(eq("division"), same(division));
        assertEquals("page/division/update", page);
    }

    @Test
    public void shouldUpdateDivisionAndRedirectWhenNoBindingErrors() throws Exception {
        // given
        Model uiModel = mock(Model.class);
        Division division = new Division();
        BindingResult bindingResult = mock(BindingResult.class);

        // when
        String page = divisionController.update(division, bindingResult, uiModel);

        // then
        verify(divisionDAO).update(same(division));
        assertEquals("redirect:/administration", page);
    }

    @Test
    public void shouldAddBindingErrorsToModelAndForward() throws Exception {
        // given
        Model uiModel = mock(Model.class);
        Division division = new Division();
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);

        // when
        String page = divisionController.update(division, bindingResult, uiModel);

        // then
        verify(uiModel).addAttribute(eq("division"), same(division));
        assertEquals("page/division/update", page);
    }

    @Test
    public void shouldDeleteAndRedirect() throws Exception {
        // given
        Long id = 1l;

        // when
        String page = divisionController.delete(id);

        // then
        verify(divisionDAO).delete(same(id));
        assertEquals("redirect:/administration", page);
    }

}
