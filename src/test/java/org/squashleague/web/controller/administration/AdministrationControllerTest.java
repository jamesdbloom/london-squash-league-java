package org.squashleague.web.controller.administration;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.VerboseMockitoJUnitRunner;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.squashleague.dao.account.UserDAO;
import org.squashleague.dao.league.*;
import org.squashleague.domain.account.User;
import org.squashleague.domain.league.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

/**
 * @author jamesdbloom
 */
@RunWith(VerboseMockitoJUnitRunner.class)
public class AdministrationControllerTest {

    private final List<Club> clubs = new ArrayList<>();
    private final List<League> leagues = new ArrayList<>();
    private final List<Division> divisions = new ArrayList<>();
    private final List<Round> rounds = new ArrayList<>();
    private final List<Match> matches = new ArrayList<>();
    private final List<Player> players = new ArrayList<>();
    private final List<User> users = new ArrayList<>();
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
    }

    @Test
    public void shouldCreateCorrectModelForView() throws Exception {
        // given
        Model uiModel = mock(Model.class);

        // when
        administrationController.list(uiModel, mock(HttpSession.class));

        // then
        verify(uiModel).addAttribute(eq("clubs"), same(clubs));
        verify(uiModel).addAttribute(eq("leagues"), same(leagues));
        verify(uiModel).addAttribute(eq("divisions"), same(divisions));
        verify(uiModel).addAttribute(eq("rounds"), same(rounds));
        verify(uiModel).addAttribute(eq("matches"), same(matches));
        verify(uiModel).addAttribute(eq("players"), same(players));
        verify(uiModel).addAttribute(eq("users"), same(users));
        verify(uiModel).addAttribute("bindingResult", null);
        verify(uiModel).addAttribute("club", new Club());
        verify(uiModel).addAttribute("league", new League());
        verify(uiModel).addAttribute("division", new Division());
        verify(uiModel).addAttribute("round", new Round());
        verify(uiModel).addAttribute("match", new Match());
        verify(uiModel).addAttribute("player", new Player());
    }

    @Test
    public void shouldCreateRetrieveValuesFromSession() throws Exception {
        // given
        Model uiModel = mock(Model.class);
        HttpSession session = mock(HttpSession.class);
        BindingResult bindingResult = mock(BindingResult.class);
        when(session.getAttribute("bindingResult")).thenReturn(bindingResult);
        Club club = new Club();
        when(session.getAttribute("club")).thenReturn(club);
        League league = new League();
        when(session.getAttribute("league")).thenReturn(league);
        Division division = new Division();
        when(session.getAttribute("division")).thenReturn(division);
        Round round = new Round();
        when(session.getAttribute("round")).thenReturn(round);
        Match match = new Match();
        when(session.getAttribute("match")).thenReturn(match);
        Player player = new Player();
        when(session.getAttribute("player")).thenReturn(player);

        // when
        administrationController.list(uiModel, session);

        // then
        verify(uiModel).addAttribute(eq("bindingResult"), same(bindingResult));
        verify(uiModel).addAttribute(eq("club"), same(club));
        verify(uiModel).addAttribute(eq("league"), same(league));
        verify(uiModel).addAttribute(eq("division"), same(division));
        verify(uiModel).addAttribute(eq("round"), same(round));
        verify(uiModel).addAttribute(eq("match"), same(match));
        verify(uiModel).addAttribute(eq("player"), same(player));
    }

}
