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
import org.squashleague.dao.account.UserDAO;
import org.squashleague.dao.league.*;
import org.squashleague.domain.account.MobilePrivacy;
import org.squashleague.domain.account.User;
import org.squashleague.domain.league.*;

import javax.annotation.Resource;

import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * @author jamesdbloom
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = RootConfiguration.class, initializers = HSQLApplicationContextInitializer.class)
public class ReferenceIntegrityDAOIntegrationTest {


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
    private UserDAO userDAO;
    private User userOne;
    private User userTwo;
    @Resource
    private PlayerDAO playerDAO;
    private Player playerOne;
    private Player playerTwo;
    @Resource
    private MatchDAO matchDAO;
    private Match match;

    @Before
    public void setupDatabase() {
        club = new Club()
                .withName("club name")
                .withAddress("address");
        clubDAO.save(club);
        league = new League()
                .withName("expectedLeague name")
                .withClub(club);
        leagueDAO.save(league);
        division = new Division()
                .withName("expectedDivision name")
                .withLeague(league);
        divisionDAO.save(division);
        round = new Round()
                .withStartDate(new DateTime().plusDays(1))
                .withEndDate(new DateTime().plusDays(2))
                .withDivision(division);
        roundDAO.save(round);
        userOne = new User()
                .withEmail("user@email.com")
                .withName("playerOne name")
                .withMobilePrivate(MobilePrivacy.SECRET);
        userTwo = new User()
                .withEmail("user@email.com")
                .withName("playerTwo name")
                .withMobilePrivate(MobilePrivacy.SECRET);
        userDAO.save(userOne);
        userDAO.save(userTwo);
        playerOne = new Player()
                .withCurrentDivision(division)
                .withPlayerStatus(PlayerStatus.ACTIVE)
                .withUser(userOne);
        playerTwo = new Player()
                .withCurrentDivision(division)
                .withPlayerStatus(PlayerStatus.ACTIVE)
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
        userDAO.delete(userOne);
        userDAO.delete(userTwo);
        roundDAO.delete(round);
        divisionDAO.delete(division);
        leagueDAO.delete(league);
        clubDAO.delete(club);
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
    public void shouldNotAllowUserOneWithPlayersToBeDeleted() throws Exception {
        userDAO.delete(userOne);
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void shouldNotAllowUserTwoWithPlayersToBeDeleted() throws Exception {
        userDAO.delete(userTwo);
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
}
