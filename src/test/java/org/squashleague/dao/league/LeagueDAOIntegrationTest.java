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

import static org.junit.Assert.*;

/**
 * @author jamesdbloom
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = RootConfiguration.class, initializers = HSQLApplicationContextInitializer.class)
public class LeagueDAOIntegrationTest extends AdministratorLoggedInTest {

    @Resource
    private LeagueDAO leagueDAO;
    @Resource
    private RoleDAO roleDAO;
    @Resource
    private UserDAO userDAO;
    @Resource
    private ClubDAO clubDAO;
    private Club club;
    @Resource
    private RoundDAO roundDAO;
    @Resource
    private PlayerDAO playerDAO;

    @Before
    public void setupDatabase() {
        club = new Club()
                .withName("club name")
                .withAddress("address");
        clubDAO.save(club);
    }

    @After
    public void teardownDatabase() {
        clubDAO.delete(club);
    }

    @Test
    public void shouldFindAllUnregisteredLeagues() {
        // given
        Role role = new Role()
                .withName("new role")
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
        Round roundOne = new Round()
                .withStartDate(new DateTime().minusDays(1))
                .withEndDate(new DateTime().plusDays(1))
                .withLeague(leagueOne);
        Round roundTwo = new Round()
                .withStartDate(new DateTime().minusDays(1))
                .withEndDate(new DateTime().plusDays(1))
                .withLeague(leagueTwo);
        leagueDAO.save(leagueOne);
        leagueDAO.save(leagueTwo);
        leagueDAO.save(leagueThree);
        roundDAO.save(roundOne);
        roundDAO.save(roundTwo);
        Player expectedPlayerOne = new Player()
                .withLeague(leagueOne)
                .withStatus(PlayerStatus.ACTIVE)
                .withUser(user);
        Player expectedPlayerTwo = new Player()
                .withLeague(leagueTwo)
                .withStatus(PlayerStatus.INACTIVE)
                .withUser(user);
        playerDAO.save(expectedPlayerOne);
        playerDAO.save(expectedPlayerTwo);

        // when
        List<League> leagues = leagueDAO.findAllUnregisteredLeagues(user);

        // then
        League[] actualLeagues = {leagueThree};
        try {
            assertArrayEquals(actualLeagues, leagues.toArray());
        } finally {
            playerDAO.delete(expectedPlayerOne);
            playerDAO.delete(expectedPlayerTwo);
            roundDAO.delete(roundOne);
            roundDAO.delete(roundTwo);
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
        League expectedLeague = new League()
                .withName("league name")
                .withClub(club);

        // when
        leagueDAO.save(expectedLeague);

        // then
        League actualLeague = leagueDAO.findById(expectedLeague.getId());
        try {
            assertEquals(expectedLeague, actualLeague);
        } finally {
            leagueDAO.delete(expectedLeague);
        }
    }

    @Test
    public void shouldSaveUpdateAndRetrieveById() throws Exception {
        // given
        League expectedLeague = new League()
                .withName("league name")
                .withClub(club);
        leagueDAO.save(expectedLeague);
        expectedLeague
                .withName("new league name");

        // when
        leagueDAO.update(expectedLeague);

        // then
        League actualLeague = leagueDAO.findById(expectedLeague.getId());
        try {
            assertEquals(expectedLeague.incrementVersion(), actualLeague);
        } finally {
            leagueDAO.delete(expectedLeague);
        }
    }

    @Test
    public void shouldSaveAllFieldsWithObjectHierarchyAndRetrieveById() throws Exception {
        // given
        League expectedLeague = new League()
                .withName("league name")
                .withClub(club)
                .withRounds(
                        new Round()
                                .withStartDate(new DateTime().minusDays(1))
                                .withEndDate(new DateTime().plusDays(1)),
                        new Round()
                                .withStartDate(new DateTime().minusDays(1))
                                .withEndDate(new DateTime().plusDays(1))
                );

        // when
        leagueDAO.save(expectedLeague);

        // then
        League actualLeague = leagueDAO.findById(expectedLeague.getId());
        try {
            assertEquals(expectedLeague, actualLeague);
        } finally {
            for (Round round : expectedLeague.getRounds()) {
                roundDAO.delete(round);
            }
            leagueDAO.delete(expectedLeague);
        }
    }

    @Test
    public void shouldUpdateWhenContainsChildren() throws Exception {
        // given
        League expectedLeague = new League()
                .withName("league name")
                .withClub(club)
                .withRounds(
                        new Round()
                                .withStartDate(new DateTime().minusDays(1))
                                .withEndDate(new DateTime().plusDays(1)),
                        new Round()
                                .withStartDate(new DateTime().minusDays(1))
                                .withEndDate(new DateTime().plusDays(1))
                );

        // when
        leagueDAO.save(expectedLeague);
        League updatedLeague =
                expectedLeague.merge(new League()
                        .withName("new league name"));
        leagueDAO.update(updatedLeague);

        // then
        League actualLeague = leagueDAO.findById(expectedLeague.getId());
        try {
            assertEquals(updatedLeague.incrementVersion(), actualLeague);
            assertEquals("new league name", actualLeague.getName());
        } finally {
            for (Round round : expectedLeague.getRounds()) {
                roundDAO.delete(round);
            }
            leagueDAO.delete(expectedLeague);
        }
    }

    @Test
    public void shouldSaveAndRetrieveList() throws Exception {
        // given
        League leagueOne = new League()
                .withName("leagueOne name")
                .withClub(club);
        League leagueTwo = new League()
                .withName("leagueTwo name")
                .withClub(club);

        // when
        leagueDAO.save(leagueOne);
        leagueDAO.save(leagueTwo);

        // then
        Object[] actualLeagues = leagueDAO.findAll().toArray();
        try {
            assertArrayEquals(new League[]{leagueOne, leagueTwo}, actualLeagues);
        } finally {
            leagueDAO.delete(leagueOne);
            leagueDAO.delete(leagueTwo);
        }
    }

    @Test
    public void shouldSaveAndRetrieveAndDelete() throws Exception {
        // given
        League expectedLeague = new League()
                .withName("league name")
                .withClub(club);
        leagueDAO.save(expectedLeague);
        assertEquals(expectedLeague, leagueDAO.findById(expectedLeague.getId()));

        // when
        leagueDAO.delete(expectedLeague);

        // then
        assertNull(leagueDAO.findById(expectedLeague.getId()));
    }

    @Test
    public void shouldSaveAndRetrieveAndDeleteById() throws Exception {
        // given
        League expectedLeague = new League()
                .withName("league name")
                .withClub(club);
        leagueDAO.save(expectedLeague);
        assertEquals(expectedLeague, leagueDAO.findById(expectedLeague.getId()));

        // when
        leagueDAO.delete(expectedLeague.getId());

        // then
        assertNull(leagueDAO.findById(expectedLeague.getId()));
    }

    @Test
    public void shouldThrowExceptionWhenFindingNullId() {
        assertNull(leagueDAO.findById(null));
    }

    @Test(expected = Exception.class)
    public void shouldThrowExceptionWhenSavingNull() {
        leagueDAO.save((League) null);
    }

    @Test
    public void shouldNotThrowExceptionWhenUpdatingNull() {
        leagueDAO.update((League) null);
    }

    @Test(expected = Exception.class)
    public void shouldThrowExceptionWhenDeletingNull() {
        leagueDAO.delete((League) null);
    }

    @Test(expected = Exception.class)
    public void shouldThrowExceptionWhenDeletingInvalidId() {
        leagueDAO.delete(1l);
    }

}
