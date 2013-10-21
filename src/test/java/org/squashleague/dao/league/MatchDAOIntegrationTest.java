package org.squashleague.dao.league;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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

import static org.junit.Assert.*;

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
    private User userThree;
    @Resource
    private ClubDAO clubDAO;
    private Club club;
    @Resource
    private LeagueDAO leagueDAO;
    private League league;
    @Resource
    private DivisionDAO divisionDAO;
    private Division previousPreviousRoundDivision;
    private Division previousRoundDivision;
    private Division currentRoundDivision;
    @Resource
    private RoundDAO roundDAO;
    private Round previousPreviousRound;
    private Round previousRound;
    private Round round;
    @Resource
    private PlayerDAO playerDAO;
    private Player playerOne;
    private Player playerTwo;
    private Player playerThree;

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
        userThree = new User()
                .withEmail("three@email.com")
                .withName("playerThree name")
                .withMobilePrivacy(MobilePrivacy.SECRET)
                .withRoles(role);
        userDAO.save(userOne);
        userDAO.save(userTwo);
        userDAO.save(userThree);
        club = new Club()
                .withName("club name")
                .withAddress("address");
        clubDAO.save(club);
        league = new League()
                .withName("league name")
                .withClub(club);
        leagueDAO.save(league);
        previousPreviousRound = new Round()
                .withStartDate(new DateTime().plusDays(1))
                .withEndDate(new DateTime().plusDays(2))
                .withLeague(league);
        previousRound = new Round()
                .withStartDate(new DateTime().plusDays(1))
                .withEndDate(new DateTime().plusDays(2))
                .withLeague(league)
                .withPreviousRound(previousPreviousRound);
        round = new Round()
                .withStartDate(new DateTime().plusDays(1))
                .withEndDate(new DateTime().plusDays(2))
                .withLeague(league)
                .withPreviousRound(previousRound);
        roundDAO.save(previousPreviousRound);
        roundDAO.save(previousRound);
        roundDAO.save(round);
        previousPreviousRoundDivision = new Division()
                .withName(1)
                .withRound(previousPreviousRound);
        previousRoundDivision = new Division()
                .withName(1)
                .withRound(previousRound);
        currentRoundDivision = new Division()
                .withName(1)
                .withRound(round);
        divisionDAO.save(previousPreviousRoundDivision);
        divisionDAO.save(previousRoundDivision);
        divisionDAO.save(currentRoundDivision);
        playerOne = new Player()
                .withCurrentDivision(currentRoundDivision)
                .withStatus(PlayerStatus.ACTIVE)
                .withUser(userOne);
        playerTwo = new Player()
                .withCurrentDivision(currentRoundDivision)
                .withStatus(PlayerStatus.ACTIVE)
                .withUser(userTwo);
        playerThree = new Player()
                .withCurrentDivision(currentRoundDivision)
                .withStatus(PlayerStatus.INACTIVE)
                .withUser(userThree);
        playerDAO.save(playerOne);
        playerDAO.save(playerTwo);
        playerDAO.save(playerThree);
    }

    @After
    public void teardownDatabase() {
        playerDAO.delete(playerOne);
        playerDAO.delete(playerTwo);
        playerDAO.delete(playerThree);
        divisionDAO.delete(currentRoundDivision);
        divisionDAO.delete(previousRoundDivision);
        divisionDAO.delete(previousPreviousRoundDivision);
        roundDAO.delete(round);
        roundDAO.delete(previousRound);
        roundDAO.delete(previousPreviousRound);
        leagueDAO.delete(league);
        clubDAO.delete(club);
        userDAO.delete(userOne);
        userDAO.delete(userTwo);
        userDAO.delete(userThree);
        roleDAO.delete(role);
    }

    @Test
    public void shouldFindAllByUser() {
        // given
        Match matchOne = new Match()
                .withPlayerOne(playerThree)
                .withPlayerTwo(playerTwo)
                .withDivision(currentRoundDivision);
        Match matchTwo = new Match()
                .withPlayerOne(playerTwo)
                .withPlayerTwo(playerOne)
                .withDivision(currentRoundDivision);
        Match matchThree = new Match()
                .withPlayerOne(playerTwo)
                .withPlayerTwo(playerThree)
                .withDivision(previousPreviousRoundDivision);
        Match matchFour = new Match()
                .withPlayerOne(playerOne)
                .withPlayerTwo(playerTwo)
                .withDivision(previousRoundDivision);
        Match matchFive = new Match()
                .withPlayerOne(playerOne)
                .withPlayerTwo(playerOne)
                .withDivision(currentRoundDivision);
        matchDAO.save(matchOne);
        matchDAO.save(matchTwo);
        matchDAO.save(matchThree);
        matchDAO.save(matchFour);
        matchDAO.save(matchFive);

        // when
        Object[] actualMatches = matchDAO.findAllByPlayer(playerTwo, playerTwo.getCurrentDivision().getRound(), 1).toArray();
        try {
            // then
            assertArrayEquals(new Match[]{matchTwo, matchOne, matchFour}, actualMatches);
        } finally {
            matchDAO.delete(matchOne);
            matchDAO.delete(matchTwo);
            matchDAO.delete(matchThree);
            matchDAO.delete(matchFour);
            matchDAO.delete(matchFive);
        }
    }

    @Test
    public void shouldSaveRequiredFieldsAndRetrieveById() throws Exception {
        // given
        Match expectedMatch = new Match()
                .withPlayerOne(playerOne)
                .withPlayerTwo(playerTwo)
                .withDivision(currentRoundDivision);

        // when
        matchDAO.save(expectedMatch);

        // then
        Match actualMatch = matchDAO.findById(expectedMatch.getId());
        try {
            assertEquals(expectedMatch, actualMatch);
        } finally {
            matchDAO.delete(expectedMatch);
        }
    }

    @Test
    public void shouldSaveUpdateAndRetrieveById() throws Exception {
        // given
        Match expectedMatch = new Match()
                .withPlayerOne(playerOne)
                .withPlayerTwo(playerTwo)
                .withDivision(currentRoundDivision);
        matchDAO.save(expectedMatch);
        expectedMatch
                .withPlayerOne(playerTwo)
                .withPlayerTwo(playerOne);

        // when
        matchDAO.update(expectedMatch);

        // then
        Match actualMatch = matchDAO.findById(expectedMatch.getId());
        try {
            assertEquals(expectedMatch.incrementVersion(), actualMatch);
        } finally {
            matchDAO.delete(expectedMatch);
        }
    }

    @Test
    public void shouldSaveAllFieldsWithObjectHierarchyAndRetrieveById() throws Exception {
        // given
        Match expectedMatch = new Match()
                .withPlayerOne(playerOne)
                .withPlayerTwo(playerTwo)
                .withDivision(currentRoundDivision);

        // when
        matchDAO.save(expectedMatch);

        // then
        Match actualMatch = matchDAO.findById(expectedMatch.getId());
        try {
            assertEquals(expectedMatch, actualMatch);
        } finally {
            matchDAO.delete(expectedMatch);
        }
    }

    @Test
    public void shouldUpdateWhenContainsChildren() throws Exception {
        // given
        Match expectedMatch = new Match()
                .withPlayerOne(playerOne)
                .withPlayerTwo(playerTwo)
                .withDivision(currentRoundDivision);

        // when
        matchDAO.save(expectedMatch);
        Match updatedMatch =
                expectedMatch.merge(new Match()
                        .withScore("3-2"));
        matchDAO.update(updatedMatch);

        // then
        Match actualMatch = matchDAO.findById(expectedMatch.getId());
        try {
            assertEquals(updatedMatch.incrementVersion(), actualMatch);
            assertEquals("3-2", actualMatch.getScore());
        } finally {
            matchDAO.delete(expectedMatch);
        }
    }

    @Test
    public void shouldSaveAndRetrieveList() throws Exception {
        // given
        Match matchOne = new Match()
                .withPlayerOne(playerThree)
                .withPlayerTwo(playerTwo)
                .withDivision(currentRoundDivision);
        Match matchTwo = new Match()
                .withPlayerOne(playerTwo)
                .withPlayerTwo(playerOne)
                .withDivision(currentRoundDivision);
        Match matchThree = new Match()
                .withPlayerOne(playerTwo)
                .withPlayerTwo(playerThree)
                .withDivision(currentRoundDivision);
        Match matchFour = new Match()
                .withPlayerOne(playerOne)
                .withPlayerTwo(playerTwo)
                .withDivision(currentRoundDivision);
        matchDAO.save(matchOne);
        matchDAO.save(matchTwo);
        matchDAO.save(matchThree);
        matchDAO.save(matchFour);

        // when
        Object[] actualMatches = matchDAO.findAll().toArray();
        try {
            // then
            assertArrayEquals(new Match[]{matchFour, matchOne, matchTwo, matchThree}, actualMatches);
        } finally {
            matchDAO.delete(matchOne);
            matchDAO.delete(matchTwo);
            matchDAO.delete(matchThree);
            matchDAO.delete(matchFour);
        }
    }

    @Test
    public void shouldSaveAndRetrieveAndDelete() throws Exception {
        // given
        Match expectedMatch = new Match()
                .withPlayerOne(playerOne)
                .withPlayerTwo(playerTwo)
                .withDivision(currentRoundDivision);
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
                .withDivision(currentRoundDivision);
        matchDAO.save(expectedMatch);
        assertEquals(expectedMatch, matchDAO.findById(expectedMatch.getId()));

        // when
        matchDAO.delete(expectedMatch.getId());

        // then
        assertNull(matchDAO.findById(expectedMatch.getId()));
    }

    @Test
    public void shouldThrowExceptionWhenFindingNullId() {
        assertNull(matchDAO.findById(null));
    }

    @Test(expected = Exception.class)
    public void shouldThrowExceptionWhenSavingNull() {
        matchDAO.save((Match) null);
    }

    @Test
    public void shouldNotThrowExceptionWhenUpdatingNull() {
        matchDAO.update((Match) null);
    }

    @Test(expected = Exception.class)
    public void shouldThrowExceptionWhenDeletingNull() {
        matchDAO.delete((Match) null);
    }

    @Test(expected = Exception.class)
    public void shouldThrowExceptionWhenDeletingInvalidId() {
        matchDAO.delete(1l);
    }

}
