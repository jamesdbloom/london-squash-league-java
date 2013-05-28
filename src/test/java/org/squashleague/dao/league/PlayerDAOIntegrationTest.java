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

import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * @author jamesdbloom
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = RootConfiguration.class, initializers = HSQLApplicationContextInitializer.class)
public class PlayerDAOIntegrationTest extends AdministratorLoggedInTest {

    @Resource
    private PlayerDAO playerDAO;
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
    }

    @After
    public void teardownDatabase() {
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
        Player expectedPlayer = new Player()
                .withCurrentDivision(division)
                .withStatus(PlayerStatus.ACTIVE)
                .withUser(userOne);

        // when
        playerDAO.save(expectedPlayer);

        // then
        Player actualPlayer = playerDAO.findById(expectedPlayer.getId());
        try {
            assertEquals(expectedPlayer, actualPlayer);
        } finally {
            playerDAO.delete(expectedPlayer);
        }
    }

    @Test
    public void shouldSaveUpdateAndRetrieveById() throws Exception {
        // given
        Player expectedPlayer = new Player()
                .withCurrentDivision(division)
                .withStatus(PlayerStatus.ACTIVE)
                .withUser(userOne);
        playerDAO.save(expectedPlayer);
        expectedPlayer
                .withStatus(PlayerStatus.INACTIVE);

        // when
        playerDAO.update(expectedPlayer);

        // then
        Player actualPlayer = playerDAO.findById(expectedPlayer.getId());
        try {
            assertEquals(expectedPlayer.incrementVersion(), actualPlayer);
        } finally {
            playerDAO.delete(expectedPlayer);
        }
    }

    @Test
    public void shouldSaveAllFieldsWithObjectHierarchyAndRetrieveById() throws Exception {
        // given
        Player expectedPlayer = new Player()
                .withCurrentDivision(division)
                .withStatus(PlayerStatus.ACTIVE)
                .withUser(userOne);

        // when
        playerDAO.save(expectedPlayer);

        // then
        Player actualPlayer = playerDAO.findById(expectedPlayer.getId());
        try {
            assertEquals(expectedPlayer, actualPlayer);
        } finally {
            playerDAO.delete(expectedPlayer);
        }
    }

    @Test
    public void shouldUpdateWhenContainsChildren() throws Exception {
        // given
        Player expectedPlayer = new Player()
                .withCurrentDivision(division)
                .withStatus(PlayerStatus.ACTIVE)
                .withUser(userOne);

        // when
        playerDAO.save(expectedPlayer);
        Player updatedPlayer =
                expectedPlayer.merge(new Player()
                        .withStatus(PlayerStatus.INACTIVE));
        playerDAO.update(updatedPlayer);

        // then
        Player actualPlayer = playerDAO.findById(expectedPlayer.getId());
        try {
            assertEquals(updatedPlayer.incrementVersion(), actualPlayer);
            assertEquals(PlayerStatus.INACTIVE, actualPlayer.getStatus());
        } finally {
            playerDAO.delete(expectedPlayer);
        }
    }

    @Test
    public void shouldSaveAndRetrieveList() throws Exception {
        // given
        Player playerOne = new Player()
                .withCurrentDivision(division)
                .withStatus(PlayerStatus.ACTIVE)
                .withUser(userOne);
        Player playerTwo = new Player()
                .withCurrentDivision(division)
                .withStatus(PlayerStatus.ACTIVE)
                .withUser(userTwo);

        // when
        playerDAO.save(playerOne);
        playerDAO.save(playerTwo);

        // then
        Object[] actualPlayers = playerDAO.findAll().toArray();
        try {
            assertArrayEquals(new Player[]{playerOne, playerTwo}, actualPlayers);
        } finally {
            playerDAO.delete(playerOne);
            playerDAO.delete(playerTwo);
        }
    }

    @Test
    public void shouldSaveAndRetrieveAndDelete() throws Exception {
        // given
        Player expectedPlayer = new Player()
                .withCurrentDivision(division)
                .withStatus(PlayerStatus.ACTIVE)
                .withUser(userOne);
        playerDAO.save(expectedPlayer);
        assertEquals(expectedPlayer, playerDAO.findById(expectedPlayer.getId()));

        // when
        playerDAO.delete(expectedPlayer);

        // then
        assertNull(playerDAO.findById(expectedPlayer.getId()));
    }

    @Test
    public void shouldSaveAndRetrieveAndDeleteById() throws Exception {
        // given
        Player expectedPlayer = new Player()
                .withCurrentDivision(division)
                .withStatus(PlayerStatus.ACTIVE)
                .withUser(userOne);
        playerDAO.save(expectedPlayer);
        assertEquals(expectedPlayer, playerDAO.findById(expectedPlayer.getId()));

        // when
        playerDAO.delete(expectedPlayer.getId());

        // then
        assertNull(playerDAO.findById(expectedPlayer.getId()));
    }

    @Test
    public void shouldThrowExceptionWhenFindingNullId() {
        assertNull(playerDAO.findById(null));
    }

    @Test(expected = Exception.class)
    public void shouldThrowExceptionWhenSavingNull() {
        playerDAO.save(null);
    }

    @Test
    public void shouldNotThrowExceptionWhenUpdatingNull() {
        playerDAO.update(null);
    }

    @Test(expected = Exception.class)
    public void shouldThrowExceptionWhenDeletingNull() {
        playerDAO.delete((Player) null);
    }

    @Test(expected = Exception.class)
    public void shouldThrowExceptionWhenDeletingInvalidId() {
        playerDAO.delete(1l);
    }

}
