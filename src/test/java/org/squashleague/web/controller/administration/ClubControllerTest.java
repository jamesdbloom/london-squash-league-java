package org.squashleague.web.controller.administration;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.VerboseMockitoJUnitRunner;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.squashleague.dao.league.ClubDAO;
import org.squashleague.domain.league.Club;

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
public class ClubControllerTest {

    private final List<Club> clubs = new ArrayList<>();

    @Mock
    private ClubDAO clubDAO;
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
        HttpSession session = mock(HttpSession.class);

        // when
        String page = clubController.create(club, mock(BindingResult.class), session);

        // then
        verify(session).removeAttribute("bindingResult");
        verify(clubDAO).save(same(club));
        assertEquals("redirect:/administration", page);
    }

    @Test
    public void shouldAddBindingErrorsToSessionAndRedirect() throws Exception {
        // given
        Club club = new Club();
        HttpSession session = mock(HttpSession.class);
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);

        // when
        String page = clubController.create(club, bindingResult, session);

        // then
        verify(session).setAttribute(eq("bindingResult"), same(bindingResult));
        verify(session).setAttribute(eq("club"), same(club));
        assertEquals("redirect:/administration", page);
    }

    @Test
    public void shouldRetrieveCorrectUserAndDisplayForm() throws Exception {
        // given
        Model uiModel = mock(Model.class);
        Long id = 1l;
        Club club = new Club();
        when(clubDAO.findOne(same(id))).thenReturn(club);

        // when
        String page = clubController.updateForm(id, uiModel);

        // then
        verify(uiModel).addAttribute(eq("club"), same(club));
        assertEquals("page/club/update", page);
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
        verify(clubDAO).update(same(club));
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
        verify(uiModel).addAttribute(eq("club"), same(club));
        assertEquals("page/club/update", page);
    }

    @Test
    public void shouldDeleteAndRedirect() throws Exception {
        // given
        Long id = 1l;

        // when
        String page = clubController.delete(id);

        // then
        verify(clubDAO).delete(same(id));
        assertEquals("redirect:/administration", page);
    }

}
