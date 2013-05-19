package org.squashleague.dao.account;

import com.eaio.uuid.UUID;
import org.joda.time.DateTime;
import org.squashleague.configuration.RootConfiguration;
import org.squashleague.dao.league.*;
import org.squashleague.domain.account.MobilePrivacy;
import org.squashleague.domain.account.Role;
import org.squashleague.domain.account.User;
import org.squashleague.domain.league.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

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
public class UserDAOIntegrationTest {

    @Resource
    private UserDAO userDAO;
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
    }

    @Test
    public void shouldSaveRequiredFieldsAndRetrieveById() throws Exception {
        // given
        User expectedUser = new User()
                .withEmail("user@email.com")
                .withName("user name")
                .withMobilePrivacy(MobilePrivacy.SECRET);

        // when
        userDAO.save(expectedUser);

        // then
        assertEquals(expectedUser, userDAO.findOne(expectedUser.getId()));
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
                .withRole(Role.ROLE_USER)
                .withOneTimeToken(new UUID().toString())
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
        assertEquals(expectedUser, userDAO.findOne(expectedUser.getId()));
        userDAO.delete(expectedUser);
    }

    @Test
    public void shouldSaveAndRetrieveList() throws Exception {
        // given
        User userOne = new User()
                .withEmail("user@email.com")
                .withName("user name")
                .withMobilePrivacy(MobilePrivacy.SECRET);
        User userTwo = new User()
                .withEmail("user@email.com")
                .withName("user name")
                .withMobilePrivacy(MobilePrivacy.SECRET);

        // when
        userDAO.save(userOne);
        userDAO.save(userTwo);

        // then
        assertArrayEquals(new User[]{userOne, userTwo}, userDAO.findAll().toArray());
        userDAO.delete(userOne);
        userDAO.delete(userTwo);
    }

    @Test
    public void shouldSaveAndRetrieveAndDelete() throws Exception {
        // given
        User expectedUser = new User()
                .withEmail("user@email.com")
                .withName("user name")
                .withMobilePrivacy(MobilePrivacy.SECRET);
        userDAO.save(expectedUser);
        assertEquals(expectedUser, userDAO.findOne(expectedUser.getId()));

        // when
        userDAO.delete(expectedUser);

        // then
        assertNull(userDAO.findOne(expectedUser.getId()));
    }

}
