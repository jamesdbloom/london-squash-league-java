package org.squashleague.dao.league;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.squashleague.configuration.RootConfiguration;
import org.squashleague.domain.league.Club;
import org.squashleague.domain.league.Division;
import org.squashleague.domain.league.League;
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
    @Resource
    private DivisionDAO divisionDAO;

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
                .withDivisions(
                        new Division().withName("league one"),
                        new Division().withName("league two")
                );

        // when
        leagueDAO.save(expectedLeague);

        // then
        League actualLeague = leagueDAO.findById(expectedLeague.getId());
        try {
            assertEquals(expectedLeague, actualLeague);
        } finally {
            for (Division division : expectedLeague.getDivisions()) {
                divisionDAO.delete(division);
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
                .withDivisions(
                        new Division().withName("league one"),
                        new Division().withName("league two")
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
            for (Division division : expectedLeague.getDivisions()) {
                divisionDAO.delete(division);
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
        leagueDAO.save(null);
    }

    @Test
    public void shouldNotThrowExceptionWhenUpdatingNull() {
        leagueDAO.update(null);
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
