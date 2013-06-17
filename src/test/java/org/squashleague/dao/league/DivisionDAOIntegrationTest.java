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
public class DivisionDAOIntegrationTest extends AdministratorLoggedInTest {

    @Resource
    private DivisionDAO divisionDAO;
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
    private RoundDAO roundDAO;
    private Round round;
    @Resource
    private MatchDAO matchDAO;
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
        round = new Round()
                .withLeague(league)
                .withStartDate(new DateTime().minusDays(1))
                .withEndDate(new DateTime().plusDays(1));
        roundDAO.save(round);

        playerOne = new Player()
                .withLeague(league)
                .withStatus(PlayerStatus.ACTIVE)
                .withUser(userOne);
        playerTwo = new Player()
                .withLeague(league)
                .withStatus(PlayerStatus.ACTIVE)
                .withUser(userTwo);
        playerDAO.save(playerOne);
        playerDAO.save(playerTwo);
    }

    @After
    public void teardownDatabase() {
        playerDAO.delete(playerOne);
        playerDAO.delete(playerTwo);
        roundDAO.delete(round);
        leagueDAO.delete(league);
        clubDAO.delete(club);
        userDAO.delete(userOne);
        userDAO.delete(userTwo);
        roleDAO.delete(role);
    }

    @Test
    public void shouldSaveRequiredFieldsAndRetrieveById() throws Exception {
        // given
        Division expectedDivision = new Division()
                .withName("division name")
                .withRound(round);

        // when
        divisionDAO.save(expectedDivision);

        // then
        Division actualDivision = divisionDAO.findById(expectedDivision.getId());
        try {
            assertEquals(expectedDivision, actualDivision);
        } finally {
            divisionDAO.delete(expectedDivision);
        }
    }

    @Test
    public void shouldSaveUpdateAndRetrieveById() throws Exception {
        // given
        Division expectedDivision = new Division()
                .withName("division name")
                .withRound(round);
        divisionDAO.save(expectedDivision);
        expectedDivision
                .withName("new division name");

        // when
        divisionDAO.update(expectedDivision);

        // then
        Division actualDivision = divisionDAO.findById(expectedDivision.getId());
        try {
            assertEquals(expectedDivision.incrementVersion(), actualDivision);
        } finally {
            divisionDAO.delete(expectedDivision);
        }
    }

    @Test
    public void shouldSaveAllFieldsWithObjectHierarchyAndRetrieveById() throws Exception {
        // given
        Division expectedDivision = new Division()
                .withName("division name")
                .withRound(round);

        // when
        divisionDAO.save(expectedDivision);

        // then
        Division actualDivision = divisionDAO.findById(expectedDivision.getId());
        try {
            assertEquals(expectedDivision, actualDivision);
        } finally {
            divisionDAO.delete(expectedDivision);
        }
    }

    @Test
    public void shouldUpdateWhenContainsChildren() throws Exception {
        // given
        Division expectedDivision = new Division()
                .withName("division name")
                .withRound(round);

        // when
        divisionDAO.save(expectedDivision);
        Division updatedDivision =
                expectedDivision.merge(new Division()
                        .withName("new division name")
                        .withRound(round));
        divisionDAO.update(updatedDivision);

        // then
        Division actualDivision = divisionDAO.findById(expectedDivision.getId());
        try {
            assertEquals(updatedDivision.incrementVersion(), actualDivision);
            assertEquals("new division name", actualDivision.getName());
        } finally {
            divisionDAO.delete(expectedDivision);
        }
    }

    @Test
    public void shouldSaveAndRetrieveList() throws Exception {
        // given
        Division divisionOne = new Division()
                .withName("divisionOne name")
                .withRound(round);
        Division divisionTwo = new Division()
                .withName("divisionTwo name")
                .withRound(round);

        // when
        divisionDAO.save(divisionOne);
        divisionDAO.save(divisionTwo);

        // then
        Object[] actualDivisions = divisionDAO.findAll().toArray();
        try {
            assertArrayEquals(new Division[]{divisionOne, divisionTwo}, actualDivisions);
        } finally {
            divisionDAO.delete(divisionOne);
            divisionDAO.delete(divisionTwo);
        }
    }

    @Test
    public void shouldSaveAndRetrieveAndDelete() throws Exception {
        // given
        Division expectedDivision = new Division()
                .withName("division name")
                .withRound(round);
        divisionDAO.save(expectedDivision);
        assertEquals(expectedDivision, divisionDAO.findById(expectedDivision.getId()));

        // when
        divisionDAO.delete(expectedDivision);

        // then
        assertNull(divisionDAO.findById(expectedDivision.getId()));
    }

    @Test
    public void shouldSaveAndRetrieveAndDeleteById() throws Exception {
        // given
        Division expectedDivision = new Division()
                .withName("division name")
                .withRound(round);
        divisionDAO.save(expectedDivision);
        assertEquals(expectedDivision, divisionDAO.findById(expectedDivision.getId()));

        // when
        divisionDAO.delete(expectedDivision.getId());

        // then
        assertNull(divisionDAO.findById(expectedDivision.getId()));
    }

    @Test
    public void shouldThrowExceptionWhenFindingNullId() {
        assertNull(divisionDAO.findById(null));
    }

    @Test(expected = Exception.class)
    public void shouldThrowExceptionWhenSavingNull() {
        divisionDAO.save(null);
    }

    @Test
    public void shouldNotThrowExceptionWhenUpdatingNull() {
        divisionDAO.update(null);
    }

    @Test(expected = Exception.class)
    public void shouldThrowExceptionWhenDeletingNull() {
        divisionDAO.delete((Division) null);
    }

    @Test(expected = Exception.class)
    public void shouldThrowExceptionWhenDeletingInvalidId() {
        divisionDAO.delete(1l);
    }

}
