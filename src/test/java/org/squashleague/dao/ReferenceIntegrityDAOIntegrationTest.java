package org.squashleague.dao;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.squashleague.configuration.RootConfiguration;
import org.squashleague.dao.account.RoleDAO;
import org.squashleague.dao.account.UserDAO;
import org.squashleague.dao.league.*;
import org.squashleague.domain.account.MobilePrivacy;
import org.squashleague.domain.account.Role;
import org.squashleague.domain.account.User;
import org.squashleague.domain.league.*;
import org.squashleague.service.security.AdministratorLoggedInTest;

import javax.annotation.Resource;

/**
 * @author jamesdbloom
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = RootConfiguration.class, initializers = HSQLApplicationContextInitializer.class)
public class ReferenceIntegrityDAOIntegrationTest extends AdministratorLoggedInTest {

    @Resource
    private RoleDAO roleDAO;
    private Role role;
    @Resource
    private UserDAO userDAO;
    private User userOne;
    private User userTwo;
    @Resource
    private ClubDAO clubDAO;
    private Club club;
    @Resource
    private LeagueDAO leagueDAO;
    private League league;
    @Resource
    private DivisionDAO divisionDAO;
    private Division division;
    @Resource
    private RoundDAO roundDAO;
    private Round round;
    @Resource
    private PlayerDAO playerDAO;
    private Player playerOne;
    private Player playerTwo;
    @Resource
    private MatchDAO matchDAO;
    private Match match;

    @Before
    public void setupDatabase() {
        role = new Role()
                .withName("role name")
                .withDescription("role description");
        roleDAO.save(role);
        userOne = new User()
                .withEmail("one@email.com")
                .withName("playerOne name")
                .withMobilePrivacy(MobilePrivacy.SECRET)
                .withRoles(role);
        userTwo = new User()
                .withEmail("two@email.com")
                .withName("playerTwo name")
                .withMobilePrivacy(MobilePrivacy.SECRET)
                .withRoles(role);
        userDAO.save(userOne);
        userDAO.save(userTwo);
        club = new Club()
                .withName("club name")
                .withAddress("address");
        clubDAO.save(club);
        league = new League()
                .withName("league name")
                .withClub(club);
        leagueDAO.save(league);
        division = new Division()
                .withName("division name")
                .withLeague(league);
        divisionDAO.save(division);
        round = new Round()
                .withStartDate(new DateTime().plusDays(1))
                .withEndDate(new DateTime().plusDays(2))
                .withDivision(division);
        roundDAO.save(round);
        playerOne = new Player()
                .withCurrentDivision(division)
                .withStatus(PlayerStatus.ACTIVE)
                .withUser(userOne);
        playerTwo = new Player()
                .withCurrentDivision(division)
                .withStatus(PlayerStatus.ACTIVE)
                .withUser(userTwo);
        playerDAO.save(playerOne);
        playerDAO.save(playerTwo);
        match = new Match()
                .withPlayerOne(playerOne)
                .withPlayerTwo(playerTwo)
                .withRound(round);
        matchDAO.save(match);
    }

    @After
    public void teardownDatabase() {
        matchDAO.delete(match);
        playerDAO.delete(playerOne);
        playerDAO.delete(playerTwo);
        roundDAO.delete(round);
        divisionDAO.delete(division);
        leagueDAO.delete(league);
        clubDAO.delete(club);
        userDAO.delete(userOne);
        userDAO.delete(userTwo);
        roleDAO.delete(role);
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void shouldNotAllowPlayerOneWithMatchesToBeDeleted() throws Exception {
        playerDAO.delete(playerOne);
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void shouldNotAllowPlayerTwoWithMatchesToBeDeleted() throws Exception {
        playerDAO.delete(playerOne);
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void shouldNotAllowRoundWithMatchesToBeDeleted() throws Exception {
        roundDAO.delete(round);
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void shouldNotAllowDivisionWithRoundsToBeDeleted() throws Exception {
        divisionDAO.delete(division);
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void shouldNotAllowLeagueWithDivisionsToBeDeleted() throws Exception {
        leagueDAO.delete(league);
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void shouldNotAllowClubWithLeaguesToBeDeleted() throws Exception {
        clubDAO.delete(club);
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void shouldNotAllowUserOneWithPlayersToBeDeleted() throws Exception {
        userDAO.delete(userOne);
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void shouldNotAllowUserTwoWithPlayersToBeDeleted() throws Exception {
        userDAO.delete(userTwo);
    }
}
