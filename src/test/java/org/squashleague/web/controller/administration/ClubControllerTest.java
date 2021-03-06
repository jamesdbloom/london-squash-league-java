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
import org.squashleague.domain.league.Club;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * @author jamesdbloom
 */
@RunWith(MockitoJUnitRunner.class)
public class ClubControllerTest {

    private final List<Club> clubs = new ArrayList<>();
    @Mock
    private ClubDAO clubDAO;
    @Mock
    private Environment environment;
    @InjectMocks
    private ClubController clubController = new ClubController();

    @Before
    public void setupFixture() {
        when(clubDAO.findAll()).thenReturn(clubs);
    }

    @Test
    public void shouldSaveClubAndRedirectWhenNoBindingErrors() throws Exception {
        // given
        Club club = new Club();
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);

        // when
        String page = clubController.create(club, mock(BindingResult.class), redirectAttributes);

        // then
        verify(clubDAO).save(club);
        assertEquals("redirect:/administration", page);
    }

    @Test
    public void shouldAddBindingErrorsToSessionAndRedirect() throws Exception {
        // given
        Club club = new Club();
        String objectName = "club";
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);

        // when
        String page = clubController.create(club, bindingResult, redirectAttributes);

        // then
        verify(redirectAttributes).addFlashAttribute("bindingResult", bindingResult);
        verify(redirectAttributes).addFlashAttribute(objectName, club);
        assertEquals("redirect:/administration#" + objectName + "s", page);
    }

    @Test
    public void shouldRetrieveCorrectClubAndDisplayForm() throws Exception {
        // given
        Model uiModel = mock(Model.class);
        Long id = 1l;
        Club club = new Club();
        when(clubDAO.findById(id)).thenReturn(club);

        // when
        String page = clubController.updateForm(id, uiModel);

        // then
        verify(uiModel).addAttribute("club", club);
        verify(uiModel).addAttribute("environment", environment);
        assertEquals("page/administration/club/update", page);
    }

    @Test
    public void shouldUpdateClubAndRedirectWhenNoBindingErrors() throws Exception {
        // given
        Model uiModel = mock(Model.class);
        Club club = new Club();
        BindingResult bindingResult = mock(BindingResult.class);

        // when
        String page = clubController.update(club, bindingResult, uiModel);

        // then
        verify(clubDAO).update(club);
        assertEquals("redirect:/administration", page);
    }

    @Test
    public void shouldAddBindingErrorsToModelAndForward() throws Exception {
        // given
        Model uiModel = mock(Model.class);
        Club club = new Club();
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);

        // when
        String page = clubController.update(club, bindingResult, uiModel);

        // then
        verify(uiModel).addAttribute("bindingResult", bindingResult);
        verify(uiModel).addAttribute("club", club);
        verify(uiModel).addAttribute("environment", environment);
        assertEquals("page/administration/club/update", page);
    }

    @Test
    public void shouldDeleteAndRedirect() throws Exception {
        // given
        Long id = 1l;

        // when
        String page = clubController.delete(id);

        // then
        verify(clubDAO).delete(id);
        assertEquals("redirect:/administration", page);
    }

}
