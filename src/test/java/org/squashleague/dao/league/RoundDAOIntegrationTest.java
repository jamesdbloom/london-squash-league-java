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
import java.util.List;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * @author jamesdbloom
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = RootConfiguration.class, initializers = HSQLApplicationContextInitializer.class)
public class RoundDAOIntegrationTest extends AdministratorLoggedInTest {

    @Resource
    private RoundDAO roundDAO;
    @Resource
    private ClubDAO clubDAO;
    private Club club;
    @Resource
    private LeagueDAO leagueDAO;
    private League league;
    @Resource
    private DivisionDAO divisionDAO;
    @Resource
    private RoleDAO roleDAO;
    @Resource
    private UserDAO userDAO;
    @Resource
    private PlayerDAO playerDAO;

    @Before
    public void setupDatabase() {
        club = new Club()
                .withName("club name")
                .withAddress("address");
        clubDAO.save(club);
        league = new League()
                .withName("league name")
                .withClub(club);
        leagueDAO.save(league);
    }

    @After
    public void teardownDatabase() {
        leagueDAO.delete(league);
        clubDAO.delete(club);
    }

    @Test
    public void shouldFindAllForUser() {
        // given
        Role role = new Role()
                .withName("role name")
                .withDescription("role description");
        roleDAO.save(role);
        User user = new User()
                .withEmail("one@email.com")
                .withName("playerOne name")
                .withMobilePrivacy(MobilePrivacy.SECRET)
                .withRoles(role);
        userDAO.save(user);
        League leagueOne = new League()
                .withName("league name")
                .withClub(club);
        League leagueTwo = new League()
                .withName("league name")
                .withClub(club);
        League leagueThree = new League()
                .withName("league name")
                .withClub(club);
        leagueDAO.save(leagueOne);
        leagueDAO.save(leagueTwo);
        leagueDAO.save(leagueThree);
        Round roundOne = new Round()
                .withStartDate(new DateTime().plusDays(1))
                .withEndDate(new DateTime().plusDays(2))
                .withLeague(leagueOne);
        Round roundTwo = new Round()
                .withStartDate(new DateTime().plusDays(3))
                .withEndDate(new DateTime().plusDays(4))
                .withLeague(leagueOne);
        Round roundThree = new Round()
                .withStartDate(new DateTime().plusDays(5))
                .withEndDate(new DateTime().plusDays(6))
                .withLeague(leagueTwo);
        Round roundFour = new Round()
                .withStartDate(new DateTime().plusDays(7))
                .withEndDate(new DateTime().plusDays(8))
                .withLeague(leagueTwo);
        roundDAO.save(roundOne);
        roundDAO.save(roundTwo);
        roundDAO.save(roundThree);
        roundDAO.save(roundFour);
        Division divisionOne = new Division()
                .withName("division name")
                .withRound(roundOne);
        Division divisionTwo = new Division()
                .withName("division name")
                .withRound(roundThree);
        divisionDAO.save(divisionOne);
        divisionDAO.save(divisionTwo);
        Player playerOne = new Player()
                .withCurrentDivision(divisionOne)
                .withStatus(PlayerStatus.ACTIVE)
                .withUser(user);
        Player playerTwo = new Player()
                .withCurrentDivision(divisionTwo)
                .withStatus(PlayerStatus.INACTIVE)
                .withUser(user);
        playerDAO.save(playerOne);
        playerDAO.save(playerTwo);

        // when
        List<Round> leagues = roundDAO.findAllForUser(user);

        // then
        Round[] actualLeagues = {roundOne, roundTwo};
        try {
            assertArrayEquals(actualLeagues, leagues.toArray());
        } finally {
            playerDAO.delete(playerOne);
            playerDAO.delete(playerTwo);
            divisionDAO.delete(divisionOne);
            divisionDAO.delete(divisionTwo);
            roundDAO.delete(roundOne);
            roundDAO.delete(roundTwo);
            roundDAO.delete(roundThree);
            roundDAO.delete(roundFour);
            leagueDAO.delete(leagueOne);
            leagueDAO.delete(leagueTwo);
            leagueDAO.delete(leagueThree);
            userDAO.delete(user);
            roleDAO.delete(role);
        }
    }

    @Test
    public void shouldSaveRequiredFieldsAndRetrieveById() throws Exception {
        // given
        Round expectedRound = new Round()
                .withStartDate(new DateTime().plusDays(1))
                .withEndDate(new DateTime().plusDays(2))
                .withLeague(league);

        // when
        roundDAO.save(expectedRound);

        // then
        Round actualRound = roundDAO.findById(expectedRound.getId());
        try {
            assertEquals(expectedRound, actualRound);
        } finally {
            roundDAO.delete(expectedRound);
        }
    }

    @Test
    public void shouldSaveUpdateAndRetrieveById() throws Exception {
        // given
        Round expectedRound = new Round()
                .withStartDate(new DateTime().plusDays(1))
                .withEndDate(new DateTime().plusDays(2))
                .withLeague(league);
        roundDAO.save(expectedRound);
        expectedRound
                .withStartDate(new DateTime().plusDays(3))
                .withEndDate(new DateTime().plusDays(4));

        // when
        roundDAO.update(expectedRound);

        // then
        Round actualRound = roundDAO.findById(expectedRound.getId());
        try {
            assertEquals(expectedRound.incrementVersion(), actualRound);
        } finally {
            roundDAO.delete(expectedRound);
        }
    }

    @Test
    public void shouldSaveAllFieldsWithObjectHierarchyAndRetrieveById() throws Exception {
        // given
        Round expectedRound = new Round()
                .withStartDate(new DateTime().plusDays(1))
                .withEndDate(new DateTime().plusDays(2))
                .withLeague(league);

        // when
        roundDAO.save(expectedRound);

        // then
        Round actualRound = roundDAO.findById(expectedRound.getId());
        try {
            assertEquals(expectedRound, actualRound);
        } finally {
            roundDAO.delete(expectedRound);
        }
    }

    @Test
    public void shouldUpdateWhenContainsChildren() throws Exception {
        // given
        Round expectedRound = new Round()
                .withStartDate(new DateTime().plusDays(1))
                .withEndDate(new DateTime().plusDays(2))
                .withLeague(league);

        // when
        roundDAO.save(expectedRound);
        DateTime newEndDate = new DateTime().plusDays(10);
        Round updatedRound =
                expectedRound.merge(new Round()
                        .withEndDate(newEndDate));
        roundDAO.update(updatedRound);

        // then
        Round actualRound = roundDAO.findById(expectedRound.getId());
        try {
            assertEquals(updatedRound.incrementVersion(), actualRound);
            assertEquals(newEndDate, actualRound.getEndDate());
        } finally {
            roundDAO.delete(expectedRound);
        }
    }

    @Test
    public void shouldSaveAndRetrieveList() throws Exception {
        // given
        Round roundOne = new Round()
                .withStartDate(new DateTime().plusDays(1))
                .withEndDate(new DateTime().plusDays(2))
                .withLeague(league);
        Round roundTwo = new Round()
                .withStartDate(new DateTime().plusDays(1))
                .withEndDate(new DateTime().plusDays(2))
                .withLeague(league);

        // when
        roundDAO.save(roundOne);
        roundDAO.save(roundTwo);

        // then
        Object[] actualRounds = roundDAO.findAll().toArray();
        try {
            assertArrayEquals(new Round[]{roundOne, roundTwo}, actualRounds);
        } finally {
            roundDAO.delete(roundOne);
            roundDAO.delete(roundTwo);
        }
    }

    @Test
    public void shouldSaveAndRetrieveAndDelete() throws Exception {
        // given
        Round expectedRound = new Round()
                .withStartDate(new DateTime().plusDays(1))
                .withEndDate(new DateTime().plusDays(2))
                .withLeague(league);
        roundDAO.save(expectedRound);
        assertEquals(expectedRound, roundDAO.findById(expectedRound.getId()));

        // when
        roundDAO.delete(expectedRound);

        // then
        assertNull(roundDAO.findById(expectedRound.getId()));
    }

    @Test
    public void shouldSaveAndRetrieveAndDeleteById() throws Exception {
        // given
        Round expectedRound = new Round()
                .withStartDate(new DateTime().plusDays(1))
                .withEndDate(new DateTime().plusDays(2))
                .withLeague(league);
        roundDAO.save(expectedRound);
        assertEquals(expectedRound, roundDAO.findById(expectedRound.getId()));

        // when
        roundDAO.delete(expectedRound.getId());

        // then
        assertNull(roundDAO.findById(expectedRound.getId()));
    }

    @Test
    public void shouldThrowExceptionWhenFindingNullId() {
        assertNull(roundDAO.findById(null));
    }

    @Test(expected = Exception.class)
    public void shouldThrowExceptionWhenSavingNull() {
        roundDAO.save(null);
    }

    @Test
    public void shouldNotThrowExceptionWhenUpdatingNull() {
        roundDAO.update(null);
    }

    @Test(expected = Exception.class)
    public void shouldThrowExceptionWhenDeletingNull() {
        roundDAO.delete((Round) null);
    }

    @Test(expected = Exception.class)
    public void shouldThrowExceptionWhenDeletingInvalidId() {
        roundDAO.delete(1l);
    }

}
