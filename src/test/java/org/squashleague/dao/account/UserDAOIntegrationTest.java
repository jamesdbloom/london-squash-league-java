package org.squashleague.dao.account;

import org.joda.time.DateTime;
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
import javax.persistence.PersistenceException;
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
    }

    @After
    public void teardownDatabase() {
        roundDAO.delete(round);
        divisionDAO.delete(division);
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
        assertEquals(expectedUser, userDAO.findById(expectedUser.getId()));
        assertEquals(newRole, roleDAO.findByName(newRole.getName()));
        userDAO.delete(expectedUser);
        roleDAO.delete(newRole);
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
        assertEquals(expectedUser, userDAO.findById(expectedUser.getId()));
        assertEquals(role, roleDAO.findByName(role.getName()));
        userDAO.delete(expectedUser);
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
        assertEquals(expectedUser, userDAO.findById(expectedUser.getId()));
        assertEquals(newRole, roleDAO.findByName(newRole.getName()));
        assertEquals(role, roleDAO.findByName(role.getName()));
        userDAO.delete(expectedUser);
        roleDAO.delete(newRole);
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
        assertEquals(expectedUser, userDAO.findById(expectedUser.getId()));
        userDAO.delete(expectedUser);
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
        assertEquals(expectedUser, userDAO.findById(expectedUser.getId()));
        userDAO.delete(expectedUser);
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
                .withOneTimeToken("oneTimeToken")
                .withPlayers(
                        new Player()
                                .withCurrentDivision(division)
                                .withPlayerStatus(PlayerStatus.ACTIVE),
                        new Player()
                                .withCurrentDivision(division)
                                .withPlayerStatus(PlayerStatus.ACTIVE)
                );

        // when
        userDAO.save(expectedUser);

        // then
        assertEquals(expectedUser, userDAO.findById(expectedUser.getId()));
        userDAO.delete(expectedUser);
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
        List<User> actualUser = userDAO.findAll();

        userDAO.delete(userOne);
        userDAO.delete(userTwo);

        // then
        assertEquals(userOne, actualUser.get(0));
        assertEquals(userTwo, actualUser.get(1));
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

    @Test(expected = PersistenceException.class)
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
    public void shouldNotThrowExceptionWhenFindingNullId() {
        assertNull(userDAO.findById(null));
    }

    @Test
    public void shouldNotThrowExceptionWhenFindingNullEmail() {
        assertNull(userDAO.findByEmail(null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotThrowExceptionWhenSavingNull() {
        userDAO.save(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotThrowExceptionWhenUpdatingNull() {
        userDAO.update(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotThrowExceptionWhenDeletingNull() {
        userDAO.delete((User) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotThrowExceptionWhenDeletingInvalidId() {
        userDAO.delete(1l);
    }

}
