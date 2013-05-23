package org.squashleague.web.controller.administration;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.env.Environment;
import org.springframework.ui.Model;
import org.squashleague.dao.account.RoleDAO;
import org.squashleague.dao.account.UserDAO;
import org.squashleague.dao.league.*;
import org.squashleague.domain.account.MobilePrivacy;
import org.squashleague.domain.account.Role;
import org.squashleague.domain.account.User;
import org.squashleague.domain.league.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

/**
 * @author jamesdbloom
 */
@RunWith(MockitoJUnitRunner.class)
public class AdministrationControllerTest {

    private final List<Club> clubs = new ArrayList<>();
    private final List<League> leagues = new ArrayList<>();
    private final List<Division> divisions = new ArrayList<>();
    private final List<Round> rounds = new ArrayList<>();
    private final List<Match> matches = new ArrayList<>();
    private final List<Player> players = new ArrayList<>();
    private final List<User> users = new ArrayList<>();
    private final List<Role> roles = new ArrayList<>();
    @Mock
    private ClubDAO clubDAO;
    @Mock
    private LeagueDAO leagueDAO;
    @Mock
    private DivisionDAO divisionDAO;
    @Mock
    private RoundDAO roundDAO;
    @Mock
    private MatchDAO matchDAO;
    @Mock
    private PlayerDAO playerDAO;
    @Mock
    private UserDAO userDAO;
    @Mock
    private RoleDAO roleDAO;
    @Mock
    private Environment environment;
    @InjectMocks
    private AdministrationController administrationController = new AdministrationController();

    @Before
    public void setupFixture() {
        when(clubDAO.findAll()).thenReturn(clubs);
        when(leagueDAO.findAll()).thenReturn(leagues);
        when(divisionDAO.findAll()).thenReturn(divisions);
        when(roundDAO.findAll()).thenReturn(rounds);
        when(matchDAO.findAll()).thenReturn(matches);
        when(playerDAO.findAll()).thenReturn(players);
        when(userDAO.findAll()).thenReturn(users);
        when(roleDAO.findAll()).thenReturn(roles);
    }

    @Test
    public void shouldCreateCorrectModelForView() throws Exception {
        // given
        Model uiModel = mock(Model.class);

        // when
        administrationController.list(uiModel);

        // then
        verify(uiModel).addAttribute(eq("environment"), same(environment));
        verify(uiModel).addAttribute(eq("roles"), same(roles));
        verify(uiModel).addAttribute(eq("users"), same(users));
        verify(uiModel).addAttribute("mobilePrivacyOptions", MobilePrivacy.enumToFormOptionMap());
        verify(uiModel).addAttribute(eq("clubs"), same(clubs));
        verify(uiModel).addAttribute(eq("leagues"), same(leagues));
        verify(uiModel).addAttribute(eq("divisions"), same(divisions));
        verify(uiModel).addAttribute(eq("rounds"), same(rounds));
        verify(uiModel).addAttribute(eq("matches"), same(matches));
        verify(uiModel).addAttribute(eq("players"), same(players));
        verify(uiModel).addAttribute("playerStatuses", PlayerStatus.enumToFormOptionMap());

        verify(uiModel).addAttribute("role", new Role());
        verify(uiModel).addAttribute("user", new User());
        verify(uiModel).addAttribute("club", new Club());
        verify(uiModel).addAttribute("league", new League());
        verify(uiModel).addAttribute("division", new Division());
        verify(uiModel).addAttribute("round", new Round());
        verify(uiModel).addAttribute("match", new Match());
        verify(uiModel).addAttribute("player", new Player());
    }

    @Test
    public void shouldNotAddDefaultObjectsIfTheyAlreadyExist() throws Exception {
        // given
        Model uiModel = mock(Model.class);
        when(uiModel.containsAttribute("role")).thenReturn(true);
        when(uiModel.containsAttribute("user")).thenReturn(true);
        when(uiModel.containsAttribute("club")).thenReturn(true);
        when(uiModel.containsAttribute("league")).thenReturn(true);
        when(uiModel.containsAttribute("division")).thenReturn(true);
        when(uiModel.containsAttribute("round")).thenReturn(true);
        when(uiModel.containsAttribute("match")).thenReturn(true);
        when(uiModel.containsAttribute("player")).thenReturn(true);

        // when
        administrationController.list(uiModel);

        // then
        verify(uiModel, atMost(0)).addAttribute("role", new Role());
        verify(uiModel, atMost(0)).addAttribute("user", new User());
        verify(uiModel, atMost(0)).addAttribute("club", new Club());
        verify(uiModel, atMost(0)).addAttribute("league", new League());
        verify(uiModel, atMost(0)).addAttribute("division", new Division());
        verify(uiModel, atMost(0)).addAttribute("round", new Round());
        verify(uiModel, atMost(0)).addAttribute("match", new Match());
        verify(uiModel, atMost(0)).addAttribute("player", new Player());
    }

}
