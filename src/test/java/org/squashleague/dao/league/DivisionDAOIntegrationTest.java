package org.squashleague.dao.league;

import org.joda.time.DateTime;
import org.squashleague.configuration.RootConfiguration;
import org.squashleague.domain.league.Club;
import org.squashleague.domain.league.Division;
import org.squashleague.domain.league.League;
import org.squashleague.domain.league.Round;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
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
public class DivisionDAOIntegrationTest extends AdministratorLoggedInTest {

    @Resource
    private DivisionDAO divisionDAO;
    @Resource
    private ClubDAO clubDAO;
    private Club club;
    @Resource
    private LeagueDAO leagueDAO;
    private League league;

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
    }

    @After
    public void teardownDatabase() {
        leagueDAO.delete(league);
        clubDAO.delete(club);
    }

    @Test
    public void shouldSaveRequiredFieldsAndRetrieveById() throws Exception {
        // given
        Division expectedDivision = new Division()
                .withName("expectedDivision name")
                .withLeague(league);

        // when
        divisionDAO.save(expectedDivision);

        // then
        assertEquals(expectedDivision, divisionDAO.findById(expectedDivision.getId()));
        divisionDAO.delete(expectedDivision);
    }

    @Test
    public void shouldSaveAllFieldsWithObjectHierarchyAndRetrieveById() throws Exception {
        // given
        Division expectedDivision = new Division()
                .withName("expectedDivision name")
                .withLeague(league)
                .withRounds(
                        new Round().withStartDate(new DateTime().plusDays(1)).withEndDate(new DateTime().plusDays(2)),
                        new Round().withStartDate(new DateTime().plusDays(1)).withEndDate(new DateTime().plusDays(2))
                );

        // when
        divisionDAO.save(expectedDivision);

        // then
        assertEquals(expectedDivision, divisionDAO.findById(expectedDivision.getId()));
        divisionDAO.delete(expectedDivision);
    }

    @Test
    public void shouldSaveAndRetrieveList() throws Exception {
        // given
        Division divisionOne = new Division()
                .withName("divisionOne name")
                .withLeague(league);
        Division divisionTwo = new Division()
                .withName("divisionTwo name")
                .withLeague(league);

        // when
        divisionDAO.save(divisionOne);
        divisionDAO.save(divisionTwo);

        // then
        assertArrayEquals(new Division[]{divisionOne, divisionTwo}, divisionDAO.findAll().toArray());
        divisionDAO.delete(divisionOne);
        divisionDAO.delete(divisionTwo);
    }

    @Test
    public void shouldSaveAndRetrieveAndDelete() throws Exception {
        // given
        Division expectedDivision = new Division()
                .withName("expectedDivision name")
                .withLeague(league);
        divisionDAO.save(expectedDivision);
        assertEquals(expectedDivision, divisionDAO.findById(expectedDivision.getId()));

        // when
        divisionDAO.delete(expectedDivision);

        // then
        assertNull(divisionDAO.findById(expectedDivision.getId()));
    }

}
