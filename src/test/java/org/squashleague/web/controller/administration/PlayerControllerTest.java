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
import org.squashleague.dao.account.UserDAO;
import org.squashleague.dao.league.DivisionDAO;
import org.squashleague.dao.league.LeagueDAO;
import org.squashleague.dao.league.PlayerDAO;
import org.squashleague.domain.account.User;
import org.squashleague.domain.league.Division;
import org.squashleague.domain.league.League;
import org.squashleague.domain.league.Player;
import org.squashleague.domain.league.PlayerStatus;

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
public class PlayerControllerTest {

    private final List<Player> players = new ArrayList<>();
    private final List<User> users = new ArrayList<>();
    private final List<Division> divisions = new ArrayList<>();
    private final List<League> leagues = new ArrayList<>();

    @Mock
    private PlayerDAO playerDAO;
    @Mock
    private UserDAO userDAO;
    @Mock
    private DivisionDAO divisionDAO;
    @Mock
    private LeagueDAO leagueDAO;
    @Mock
    private Environment environment;
    @InjectMocks
    private PlayerController playerController = new PlayerController();

    @Before
    public void setupFixture() {
        when(playerDAO.findAll()).thenReturn(players);
        when(userDAO.findAll()).thenReturn(users);
        when(divisionDAO.findAll()).thenReturn(divisions);
        when(leagueDAO.findAll()).thenReturn(leagues);
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
    public void shouldRetrieveCorrectPlayerAndDisplayForm() throws Exception {
        // given
        Model uiModel = mock(Model.class);
        Long id = 1l;
        Player player = new Player();
        when(playerDAO.findById(same(id))).thenReturn(player);

        // when
        String page = playerController.updateForm(id, uiModel);

        // then
        verify(uiModel).addAttribute(eq("player"), same(player));
        verify(uiModel).addAttribute("playerStatuses", PlayerStatus.enumToFormOptionMap());
        verify(uiModel).addAttribute(eq("users"), same(users));
        verify(uiModel).addAttribute(eq("divisions"), same(divisions));
        verify(uiModel).addAttribute(eq("leagues"), same(leagues));
        verify(uiModel).addAttribute(eq("environment"), same(environment));
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
        verify(uiModel).addAttribute(eq("bindingResult"), same(bindingResult));
        verify(uiModel).addAttribute(eq("player"), same(player));
        verify(uiModel).addAttribute("playerStatuses", PlayerStatus.enumToFormOptionMap());
        verify(uiModel).addAttribute(eq("users"), same(users));
        verify(uiModel).addAttribute(eq("divisions"), same(divisions));
        verify(uiModel).addAttribute(eq("leagues"), same(leagues));
        verify(uiModel).addAttribute(eq("environment"), same(environment));
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
