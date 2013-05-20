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
import org.squashleague.dao.league.PlayerDAO;
import org.squashleague.domain.league.Player;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * @author jamesdbloom
 */
@RunWith(VerboseMockitoJUnitRunner.class)
public class PlayerControllerTest {

    private final List<Player> players = new ArrayList<>();

    @Mock
    private PlayerDAO playerDAO;
    @InjectMocks
    private PlayerController playerController = new PlayerController();

    @Before
    public void setupFixture() {
        when(playerDAO.findAll()).thenReturn(players);
    }

    @Test
    public void shouldSavePlayerAndRedirectWhenNoBindingErrors() throws Exception {
        // given
        Player player = new Player();
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);

        // when
        String page = playerController.create(player, mock(BindingResult.class), redirectAttributes);

        // then
        verify(playerDAO).save(same(player));
        assertEquals("redirect:/administration", page);
    }

    @Test
    public void shouldAddBindingErrorsToSessionAndRedirect() throws Exception {
        // given
        Player player = new Player();
        String objectName = "player";
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);

        // when
        String page = playerController.create(player, bindingResult, redirectAttributes);

        // then
        verify(redirectAttributes).addFlashAttribute(eq("bindingResult"), same(bindingResult));
        verify(redirectAttributes).addFlashAttribute(eq(objectName), same(player));
        assertEquals("redirect:/administration#" + objectName + "s", page);
    }

    @Test
    public void shouldRetrieveCorrectUserAndDisplayForm() throws Exception {
        // given
        Model uiModel = mock(Model.class);
        Long id = 1l;
        Player player = new Player();
        when(playerDAO.findOne(same(id))).thenReturn(player);

        // when
        String page = playerController.updateForm(id, uiModel);

        // then
        verify(uiModel).addAttribute(eq("player"), same(player));
        assertEquals("page/player/update", page);
    }

    @Test
    public void shouldUpdatePlayerAndRedirectWhenNoBindingErrors() throws Exception {
        // given
        Model uiModel = mock(Model.class);
        Player player = new Player();
        BindingResult bindingResult = mock(BindingResult.class);

        // when
        String page = playerController.update(player, bindingResult, uiModel);

        // then
        verify(playerDAO).update(same(player));
        assertEquals("redirect:/administration", page);
    }

    @Test
    public void shouldAddBindingErrorsToModelAndForward() throws Exception {
        // given
        Model uiModel = mock(Model.class);
        Player player = new Player();
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);

        // when
        String page = playerController.update(player, bindingResult, uiModel);

        // then
        verify(uiModel).addAttribute(eq("player"), same(player));
        assertEquals("page/player/update", page);
    }

    @Test
    public void shouldDeleteAndRedirect() throws Exception {
        // given
        Long id = 1l;

        // when
        String page = playerController.delete(id);

        // then
        verify(playerDAO).delete(same(id));
        assertEquals("redirect:/administration", page);
    }

}
