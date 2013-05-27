package org.squashleague.dao.account;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.squashleague.configuration.RootConfiguration;
import org.squashleague.dao.league.*;
import org.squashleague.domain.account.MobilePrivacy;
import org.squashleague.domain.account.Role;
import org.squashleague.domain.account.User;
import org.squashleague.domain.league.*;
import org.squashleague.service.security.AdministratorLoggedInTest;

import javax.annotation.Resource;
import java.util.List;

import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertEquals;

/**
 * @author jamesdbloom
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = RootConfiguration.class, initializers = HSQLApplicationContextInitializer.class)
public class UserDAOIntegrationTest extends AdministratorLoggedInTest {

    @Resource
    private UserDAO userDAO;
    @Resource
    private RoleDAO roleDAO;
    private Role role;
    @Resource
    private ClubDAO clubDAO;
    private Club club;
    @Resource
    private LeagueDAO leagueDAO;
    private League league;
    @Resource
    private DivisionDAO divisionDAO;
    private Division divisionOne;
    private Division divisionTwo;
    @Resource
    private PlayerDAO playerDAO;

    @Before
    public void setupDatabase() {
        role = new Role()
                .withName("role name")
                .withDescription("role description");
        roleDAO.save(role);
        club = new Club()
                .withName("club name")
                .withAddress("address");
        clubDAO.save(club);
        league = new League()
                .withName("league name")
                .withClub(club);
        leagueDAO.save(league);
        divisionOne = new Division()
                .withName("division one name")
                .withLeague(league);
        divisionDAO.save(divisionOne);
        divisionTwo = new Division()
                .withName("division two name")
                .withLeague(league);
        divisionDAO.save(divisionTwo);
    }

    @After
    public void teardownDatabase() {
        divisionDAO.delete(divisionOne);
        divisionDAO.delete(divisionTwo);
        leagueDAO.delete(league);
        clubDAO.delete(club);
        roleDAO.delete(role);
    }

    @Test
    public void shouldRegisterWithNewRole() throws Exception {
        // given
        Role newRole = new Role()
                .withName("new role")
                .withDescription("role description");
        User expectedUser = new User()
                .withEmail("user@email.com")
                .withName("user name")
                .withMobilePrivacy(MobilePrivacy.SECRET)
                .withRole(newRole);

        // when
        userDAO.register(expectedUser);

        // then
        User actualUser = userDAO.findById(expectedUser.getId());
        Role actualyNewRole = roleDAO.findByName(newRole.getName());
        try {
            assertEquals(expectedUser, actualUser);
            assertEquals(newRole, actualyNewRole);
        } finally {
            userDAO.delete(expectedUser);
            roleDAO.delete(newRole);
        }
    }

    @Test
    public void shouldRegisterWithExistingRole() throws Exception {
        // given
        User expectedUser = new User()
                .withEmail("user@email.com")
                .withName("user name")
                .withMobilePrivacy(MobilePrivacy.SECRET)
                .withRole(role);

        // when
        userDAO.register(expectedUser);

        // then
        User actualUser = userDAO.findById(expectedUser.getId());
        try {
            assertEquals(expectedUser, actualUser);
            assertEquals(role, roleDAO.findByName(role.getName()));
        } finally {
            userDAO.delete(expectedUser);
        }
    }

    @Test
    public void shouldRegisterWithOneExistingRoleAndOneNewRole() throws Exception {
        // given
        Role newRole = new Role()
                .withName("new role")
                .withDescription("role description");
        User expectedUser = new User()
                .withEmail("user@email.com")
                .withName("user name")
                .withMobilePrivacy(MobilePrivacy.SECRET)
                .withRole(newRole, role);

        // when
        userDAO.register(expectedUser);

        // then
        Role actualNewRole = roleDAO.findByName(newRole.getName());
        Role actualExisting = roleDAO.findByName(role.getName());
        User actualUser = userDAO.findById(expectedUser.getId());
        try {
            assertEquals(expectedUser, actualUser);
            assertEquals(newRole, actualNewRole);
            assertEquals(role, actualExisting);
        } finally {
            userDAO.delete(expectedUser);
            roleDAO.delete(newRole);
        }
    }

    @Test
    public void shouldSaveRequiredFieldsAndRetrieveById() throws Exception {
        // given
        User expectedUser = new User()
                .withEmail("user@email.com")
                .withName("user name")
                .withMobilePrivacy(MobilePrivacy.SECRET)
                .withRole(role);

        // when
        userDAO.save(expectedUser);

        // then
        User actualUser = userDAO.findById(expectedUser.getId());
        try {
            assertEquals(expectedUser, actualUser);
        } finally {
            userDAO.delete(expectedUser);
        }
    }

    @Test
    public void shouldSaveUpdateAndRetrieveById() throws Exception {
        // given
        User expectedUser = new User()
                .withEmail("user@email.com")
                .withName("user name")
                .withMobilePrivacy(MobilePrivacy.SECRET)
                .withRole(role);
        userDAO.save(expectedUser);
        expectedUser
                .withEmail("new@email.com")
                .withName("new name");

        // when
        userDAO.update(expectedUser);

        // then
        User actualUser = userDAO.findById(expectedUser.getId());
        try {
            assertEquals(expectedUser.incrementVersion(), actualUser);
        } finally {
            userDAO.delete(expectedUser);
        }
    }

    @Test
    public void shouldSaveAllFieldsWithObjectHierarchyAndRetrieveById() throws Exception {
        // given
        User expectedUser = new User()
                .withEmail("user@email.com")
                .withName("user name")
                .withMobile("07515 900 569")
                .withMobilePrivacy(MobilePrivacy.SECRET)
                .withRole(role)
                .withPassword("password")
                .withOneTimeToken("oneTimeToken");

        expectedUser.withPlayers(
                new Player()
                        .withCurrentDivision(divisionOne)
                        .withStatus(PlayerStatus.ACTIVE)
                        .withUser(expectedUser),
                new Player()
                        .withCurrentDivision(divisionTwo)
                        .withStatus(PlayerStatus.ACTIVE)
                        .withUser(expectedUser));

        // when
        userDAO.save(expectedUser);

        // then
        User actualUser = userDAO.findById(expectedUser.getId());
        try {
            assertEquals(expectedUser, actualUser);
        } finally {
            for (Player player : expectedUser.getPlayers()) {
                playerDAO.delete(player);
            }
            userDAO.delete(expectedUser);
        }
    }

    @Test
    public void shouldUpdateWhenContainsChildren() throws Exception {
        // given
        User expectedUser = new User()
                .withEmail("user@email.com")
                .withName("user name")
                .withMobile("07515 900 569")
                .withMobilePrivacy(MobilePrivacy.SECRET)
                .withRole(role)
                .withPassword("password")
                .withOneTimeToken("oneTimeToken");

        expectedUser.withPlayers(
                new Player()
                        .withCurrentDivision(divisionOne)
                        .withStatus(PlayerStatus.ACTIVE)
                        .withUser(expectedUser),
                new Player()
                        .withCurrentDivision(divisionTwo)
                        .withStatus(PlayerStatus.ACTIVE)
                        .withUser(expectedUser));


        // when
        userDAO.save(expectedUser);
        User updatedUser =
                expectedUser.merge(new User()
                        .withEmail("new@email.com")
                        .withName("new user name")
                        .withMobile("666 6666 6666")
                        .withMobilePrivacy(MobilePrivacy.SECRET)
                        .withRole(role)
                        .withPassword("new password")
                        .withOneTimeToken("new oneTimeToken"));
        userDAO.update(updatedUser);

        // then
        User actualUser = userDAO.findById(expectedUser.getId());
        try {
            assertEquals(updatedUser.incrementVersion(), actualUser);
            assertEquals("new@email.com", actualUser.getEmail());
        } finally {
            for (Player player : expectedUser.getPlayers()) {
                playerDAO.delete(player);
            }
            userDAO.delete(expectedUser);
        }
    }

    @Test
    public void shouldSaveAndRetrieveList() throws Exception {
        // given
        User userOne = new User()
                .withEmail("one@email.com")
                .withName("user name")
                .withMobilePrivacy(MobilePrivacy.SECRET)
                .withRole(role);
        User userTwo = new User()
                .withEmail("two@email.com")
                .withName("user name")
                .withMobilePrivacy(MobilePrivacy.SECRET)
                .withRole(role);

        // when
        userDAO.save(userOne);
        userDAO.save(userTwo);

        // then
        List<User> actualUser = userDAO.findAll();
        try {
            assertEquals(userOne, actualUser.get(0));
            assertEquals(userTwo, actualUser.get(1));
        } finally {
            userDAO.delete(userOne);
            userDAO.delete(userTwo);
        }
    }

    @Test
    public void shouldSaveAndRetrieveAndDelete() throws Exception {
        // given
        User expectedUser = new User()
                .withEmail("user@email.com")
                .withName("user name")
                .withMobilePrivacy(MobilePrivacy.SECRET)
                .withRole(role);
        userDAO.save(expectedUser);
        assertEquals(expectedUser, userDAO.findById(expectedUser.getId()));

        // when
        userDAO.delete(expectedUser);

        // then
        assertNull(userDAO.findById(expectedUser.getId()));
    }

    @Test(expected = Exception.class)
    public void shouldNotAllowDuplicateNames() throws Exception {
        // given
        User expectedUserOne = new User()
                .withEmail("user@email.com")
                .withName("user name")
                .withMobilePrivacy(MobilePrivacy.SECRET)
                .withRole(role);
        User expectedUserTwo = new User()
                .withEmail(expectedUserOne.getEmail())
                .withName("user name")
                .withMobilePrivacy(MobilePrivacy.SECRET)
                .withRole(role);
        try {
            // when
            userDAO.save(expectedUserOne);
            userDAO.save(expectedUserTwo);
        } finally {
            userDAO.delete(expectedUserOne);
        }
    }

    @Test
    public void shouldSaveAndRetrieveAndDeleteById() throws Exception {
        // given
        User expectedUser = new User()
                .withEmail("user@email.com")
                .withName("user name")
                .withMobilePrivacy(MobilePrivacy.SECRET)
                .withRole(role);
        userDAO.save(expectedUser);
        assertEquals(expectedUser, userDAO.findById(expectedUser.getId()));

        // when
        userDAO.delete(expectedUser.getId());

        // then
        assertNull(userDAO.findById(expectedUser.getId()));
    }

    @Test
    public void shouldThrowExceptionWhenFindingNullId() {
        assertNull(userDAO.findById(null));
    }

    @Test
    public void shouldThrowExceptionWhenFindingNullEmail() {
        assertNull(userDAO.findByEmail(null));
    }

    @Test(expected = Exception.class)
    public void shouldThrowExceptionWhenSavingNull() {
        userDAO.save(null);
    }

    @Test
    public void shouldNotThrowExceptionWhenUpdatingNull() {
        userDAO.update(null);
    }

    @Test(expected = Exception.class)
    public void shouldThrowExceptionWhenDeletingNull() {
        userDAO.delete((User) null);
    }

    @Test(expected = Exception.class)
    public void shouldThrowExceptionWhenDeletingInvalidId() {
        userDAO.delete(1l);
    }

}
