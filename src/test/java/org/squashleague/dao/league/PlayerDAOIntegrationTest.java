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
import javax.persistence.PersistenceException;
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
        round = new Round()
                .withStartDate(new DateTime().plusDays(1))
                .withEndDate(new DateTime().plusDays(2))
                .withLeague(league);
        roundDAO.save(round);
        division = new Division()
                .withName(1)
                .withRound(round);
        divisionDAO.save(division);
    }

    @After
    public void teardownDatabase() {
        divisionDAO.delete(division);
        roundDAO.delete(round);
        leagueDAO.delete(league);
        clubDAO.delete(club);
        userDAO.delete(userOne);
        userDAO.delete(userTwo);
        roleDAO.delete(role);
    }

    @Test
    public void shouldFindAllByUser() {
        // given
        League leagueOne = new League()
                .withName("league name")
                .withClub(club);
        League leagueTwo = new League()
                .withName("league name")
                .withClub(club);
        Round roundOne = new Round()
                .withStartDate(new DateTime().plusDays(1))
                .withEndDate(new DateTime().plusDays(2))
                .withLeague(leagueOne);
        Round roundTwo = new Round()
                .withStartDate(new DateTime().plusDays(1))
                .withEndDate(new DateTime().plusDays(2))
                .withLeague(leagueTwo);
        Division divisionOne = new Division()
                .withName(1)
                .withRound(roundOne);
        Division divisionTwo = new Division()
                .withName(2)
                .withRound(roundTwo);
        leagueDAO.save(leagueOne);
        leagueDAO.save(leagueTwo);
        roundDAO.save(roundOne);
        roundDAO.save(roundTwo);
        divisionDAO.save(divisionOne);
        divisionDAO.save(divisionTwo);
        Player expectedPlayerOne = new Player()
                .withCurrentDivision(this.division)
                .withStatus(PlayerStatus.ACTIVE)
                .withUser(userOne);
        Player expectedPlayerTwo = new Player()
                .withCurrentDivision(divisionOne)
                .withStatus(PlayerStatus.ACTIVE)
                .withUser(userOne);
        Player expectedPlayerThree = new Player()
                .withCurrentDivision(divisionTwo)
                .withStatus(PlayerStatus.ACTIVE)
                .withUser(userOne);
        playerDAO.save(expectedPlayerOne);
        playerDAO.save(expectedPlayerTwo);
        playerDAO.save(expectedPlayerThree);

        // when
        List<Player> players = playerDAO.findAllByUser(userOne);

        // then
        Player[] actualPlayers = {expectedPlayerOne, expectedPlayerTwo, expectedPlayerThree};
        try {
            assertArrayEquals(actualPlayers, players.toArray());
        } finally {
            playerDAO.delete(expectedPlayerOne);
            playerDAO.delete(expectedPlayerTwo);
            playerDAO.delete(expectedPlayerThree);
            divisionDAO.delete(divisionOne);
            divisionDAO.delete(divisionTwo);
            roundDAO.delete(roundOne);
            roundDAO.delete(roundTwo);
            leagueDAO.delete(leagueOne);
            leagueDAO.delete(leagueTwo);
        }
    }

    @Test
    public void shouldFindAllActiveByUser() {
        // given
        League leagueOne = new League()
                .withName("league name")
                .withClub(club);
        League leagueTwo = new League()
                .withName("league name")
                .withClub(club);
        Round roundOne = new Round()
                .withStartDate(new DateTime().plusDays(1))
                .withEndDate(new DateTime().plusDays(2))
                .withLeague(leagueOne);
        Round roundTwo = new Round()
                .withStartDate(new DateTime().plusDays(1))
                .withEndDate(new DateTime().plusDays(2))
                .withLeague(leagueTwo);
        Division divisionOne = new Division()
                .withName(1)
                .withRound(roundOne);
        Division divisionTwo = new Division()
                .withName(2)
                .withRound(roundTwo);
        leagueDAO.save(leagueOne);
        leagueDAO.save(leagueTwo);
        roundDAO.save(roundOne);
        roundDAO.save(roundTwo);
        divisionDAO.save(divisionOne);
        divisionDAO.save(divisionTwo);
        Player expectedPlayerOne = new Player()
                .withCurrentDivision(this.division)
                .withStatus(PlayerStatus.ACTIVE)
                .withUser(userOne);
        Player expectedPlayerTwo = new Player()
                .withCurrentDivision(divisionOne)
                .withStatus(PlayerStatus.INACTIVE)
                .withUser(userOne);
        Player expectedPlayerThree = new Player()
                .withCurrentDivision(divisionTwo)
                .withStatus(PlayerStatus.ACTIVE)
                .withUser(userOne);
        playerDAO.save(expectedPlayerOne);
        playerDAO.save(expectedPlayerTwo);
        playerDAO.save(expectedPlayerThree);

        // when
        List<Player> players = playerDAO.findAllActiveByUser(userOne);

        // then
        Player[] actualPlayers = {expectedPlayerOne, expectedPlayerThree};
        try {
            assertArrayEquals(actualPlayers, players.toArray());
        } finally {
            playerDAO.delete(expectedPlayerOne);
            playerDAO.delete(expectedPlayerTwo);
            playerDAO.delete(expectedPlayerThree);
            divisionDAO.delete(divisionOne);
            divisionDAO.delete(divisionTwo);
            roundDAO.delete(roundOne);
            roundDAO.delete(roundTwo);
            leagueDAO.delete(leagueOne);
            leagueDAO.delete(leagueTwo);
        }
    }

    @Test(expected = PersistenceException.class)
    public void shouldNotLetDuplicateDivisionPlayerBeSaved() throws Exception {
        Player player = new Player()
                .withCurrentDivision(division)
                .withStatus(PlayerStatus.ACTIVE)
                .withUser(userOne);

        try {
            playerDAO.save(player);
            playerDAO.save(new Player()
                    .withCurrentDivision(division)
                    .withStatus(PlayerStatus.INACTIVE)
                    .withUser(userOne));
        } finally {
            playerDAO.delete(player);
        }
    }

    @Test(expected = PersistenceException.class)
    public void shouldNotLetDuplicateLeaguePlayerBeSaved() throws Exception {
        Player player = new Player()
                .withLeague(league)
                .withStatus(PlayerStatus.ACTIVE)
                .withUser(userOne);

        try {
            playerDAO.save(player);
            playerDAO.save(new Player()
                    .withLeague(league)
                    .withStatus(PlayerStatus.INACTIVE)
                    .withUser(userOne));
        } finally {
            playerDAO.delete(player);
        }
    }

    @Test
    public void shouldUpdatePassword() throws Exception {
        // given
        Player expectedPlayer = new Player()
                .withLeague(league)
                .withStatus(PlayerStatus.ACTIVE)
                .withUser(userOne);
        playerDAO.save(expectedPlayer);

        // when
        playerDAO.updateStatus(expectedPlayer, PlayerStatus.INACTIVE);

        // then
        Player actualPlayer = playerDAO.findById(expectedPlayer.getId());
        try {
            assertEquals(PlayerStatus.INACTIVE, actualPlayer.getStatus());
        } finally {
            playerDAO.delete(expectedPlayer);
        }
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

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenSavingNull() {
        playerDAO.save(null);
    }

    @Test
    public void shouldNotThrowExceptionWhenUpdatingNull() {
        playerDAO.update(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenDeletingNull() {
        playerDAO.delete((Player) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenDeletingInvalidId() {
        playerDAO.delete(1l);
    }

}
