package org.squashleague.dao.league;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.squashleague.configuration.RootConfiguration;
import org.squashleague.dao.account.RoleDAO;
import org.squashleague.dao.account.UserDAO;
import org.squashleague.domain.account.MobilePrivacy;
import org.squashleague.domain.account.Role;
import org.squashleague.domain.account.User;
import org.squashleague.domain.league.*;
import org.squashleague.service.security.AdministratorLoggedInTest;

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
public class MatchDAOIntegrationTest extends AdministratorLoggedInTest {

    @Resource
    private MatchDAO matchDAO;
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

    @Before
    public void setupDatabase() {
        role = new Role()
                .withName("role name")
                .withDescription("role description");
        roleDAO.save(role);
        userOne = new User()
                .withEmail("user@email.com")
                .withName("playerOne name")
                .withMobilePrivacy(MobilePrivacy.SECRET)
                .withRole(role);
        userTwo = new User()
                .withEmail("user@email.com")
                .withName("playerTwo name")
                .withMobilePrivacy(MobilePrivacy.SECRET)
                .withRole(role);
        userDAO.save(userOne);
        userDAO.save(userTwo);
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
    }

    @After
    public void teardownDatabase() {
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

    @Test
    public void shouldSaveRequiredFieldsAndRetrieveById() throws Exception {
        // given
        Match expectedMatch = new Match()
                .withPlayerOne(playerOne)
                .withPlayerTwo(playerTwo)
                .withRound(round);

        // when
        matchDAO.save(expectedMatch);

        // then
        assertEquals(expectedMatch, matchDAO.findById(expectedMatch.getId()));
        matchDAO.delete(expectedMatch);
    }

    @Test
    public void shouldSaveUpdateAndRetrieveById() throws Exception {
        // given
        Match expectedMatch = new Match()
                .withPlayerOne(playerOne)
                .withPlayerTwo(playerTwo)
                .withRound(round);
        matchDAO.save(expectedMatch);
        expectedMatch
                .withPlayerOne(playerTwo)
                .withPlayerTwo(playerOne);

        // when
        matchDAO.update(expectedMatch);

        // then
        assertEquals(expectedMatch.incrementVersion(), matchDAO.findById(expectedMatch.getId()));
        matchDAO.delete(expectedMatch);
    }

    @Test
    public void shouldSaveAllFieldsWithObjectHierarchyAndRetrieveById() throws Exception {
        // given
        Match expectedMatch = new Match()
                .withPlayerOne(playerOne)
                .withPlayerTwo(playerTwo)
                .withRound(round);

        // when
        matchDAO.save(expectedMatch);

        // then
        assertEquals(expectedMatch, matchDAO.findById(expectedMatch.getId()));
        matchDAO.delete(expectedMatch);
    }

    @Test
    public void shouldSaveAndRetrieveList() throws Exception {
        // given
        Match matchOne = new Match()
                .withPlayerOne(playerOne)
                .withPlayerTwo(playerTwo)
                .withRound(round);
        Match matchTwo = new Match()
                .withPlayerOne(playerTwo)
                .withPlayerTwo(playerOne)
                .withRound(round);

        // when
        matchDAO.save(matchOne);
        matchDAO.save(matchTwo);

        // then
        assertArrayEquals(new Match[]{matchOne, matchTwo}, matchDAO.findAll().toArray());
        matchDAO.delete(matchOne);
        matchDAO.delete(matchTwo);
    }

    @Test
    public void shouldSaveAndRetrieveAndDelete() throws Exception {
        // given
        Match expectedMatch = new Match()
                .withPlayerOne(playerOne)
                .withPlayerTwo(playerTwo)
                .withRound(round);
        matchDAO.save(expectedMatch);
        assertEquals(expectedMatch, matchDAO.findById(expectedMatch.getId()));

        // when
        matchDAO.delete(expectedMatch);

        // then
        assertNull(matchDAO.findById(expectedMatch.getId()));
    }

    @Test
    public void shouldSaveAndRetrieveAndDeleteById() throws Exception {
        // given
        Match expectedMatch = new Match()
                .withPlayerOne(playerOne)
                .withPlayerTwo(playerTwo)
                .withRound(round);
        matchDAO.save(expectedMatch);
        assertEquals(expectedMatch, matchDAO.findById(expectedMatch.getId()));

        // when
        matchDAO.delete(expectedMatch.getId());

        // then
        assertNull(matchDAO.findById(expectedMatch.getId()));
    }

    @Test
    public void shouldNotThrowExceptionWhenFindingNullId() {
        assertNull(matchDAO.findById(null));
    }

    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void shouldNotThrowExceptionWhenSavingNull() {
        matchDAO.save(null);
    }

    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void shouldNotThrowExceptionWhenUpdatingNull() {
        matchDAO.update(null);
    }

    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void shouldNotThrowExceptionWhenDeletingNull() {
        matchDAO.delete((Match) null);
    }

    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void shouldNotThrowExceptionWhenDeletingInvalidId() {
        matchDAO.delete(1l);
    }

}
