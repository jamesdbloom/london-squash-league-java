package org.squashleague.dao.league;

import org.squashleague.configuration.RootConfiguration;
import org.squashleague.domain.league.Club;
import org.squashleague.domain.league.Division;
import org.squashleague.domain.league.League;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
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
public class LeagueDAOIntegrationTest extends AdministratorLoggedInTest {

    @Resource
    private LeagueDAO leagueDAO;

    @Resource
    private ClubDAO clubDAO;
    private Club club;

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
    public void shouldSaveRequiredFieldsAndRetrieveById() throws Exception {
        // given
        League expectedLeague = new League()
                .withName("expectedLeague name")
                .withClub(club);

        // when
        leagueDAO.save(expectedLeague);

        // then
        assertEquals(expectedLeague, leagueDAO.findById(expectedLeague.getId()));
        leagueDAO.delete(expectedLeague);
    }

    @Test
    public void shouldSaveAllFieldsWithObjectHierarchyAndRetrieveById() throws Exception {
        // given
        League expectedLeague = new League()
                .withName("expectedLeague name")
                .withClub(club)
                .withDivisions(
                        new Division().withName("league one"),
                        new Division().withName("league two")
                );

        // when
        leagueDAO.save(expectedLeague);

        // then
        assertEquals(expectedLeague, leagueDAO.findById(expectedLeague.getId()));
        leagueDAO.delete(expectedLeague);
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
        assertArrayEquals(new League[]{leagueOne, leagueTwo}, leagueDAO.findAll().toArray());
        leagueDAO.delete(leagueOne);
        leagueDAO.delete(leagueTwo);
    }

    @Test
    public void shouldSaveAndRetrieveAndDelete() throws Exception {
        // given
        League expectedLeague = new League()
                .withName("expectedLeague name")
                .withClub(club);
        leagueDAO.save(expectedLeague);
        assertEquals(expectedLeague, leagueDAO.findById(expectedLeague.getId()));

        // when
        leagueDAO.delete(expectedLeague);

        // then
        assertNull(leagueDAO.findById(expectedLeague.getId()));
    }

}
