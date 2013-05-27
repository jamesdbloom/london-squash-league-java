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
import org.squashleague.domain.league.Club;
import org.squashleague.domain.league.Division;
import org.squashleague.domain.league.League;
import org.squashleague.domain.league.Round;
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
    @Resource
    private RoundDAO roundDAO;

    @Before
    public void setupDatabase() {
        club = new Club()
                .withName("club name")
                .withAddress("address");
        clubDAO.save(club);
        league = new League()
                .withName("league name")
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
                .withName("division name")
                .withLeague(league);

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
                .withLeague(league);
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
                .withLeague(league)
                .withRounds(
                        new Round().withStartDate(new DateTime().plusDays(1)).withEndDate(new DateTime().plusDays(2)),
                        new Round().withStartDate(new DateTime().plusDays(1)).withEndDate(new DateTime().plusDays(2))
                );

        // when
        divisionDAO.save(expectedDivision);

        // then
        Division actualDivision = divisionDAO.findById(expectedDivision.getId());
        try {
            assertEquals(expectedDivision, actualDivision);
        } finally {
            for (Round round : expectedDivision.getRounds()) {
                roundDAO.delete(round);
            }
            divisionDAO.delete(expectedDivision);
        }
    }

    @Test
    public void shouldUpdateWhenContainsChildren() throws Exception {
        // given
        Division expectedDivision = new Division()
                .withName("division name")
                .withLeague(league)
                .withRounds(
                        new Round().withStartDate(new DateTime().plusDays(1)).withEndDate(new DateTime().plusDays(2)),
                        new Round().withStartDate(new DateTime().plusDays(1)).withEndDate(new DateTime().plusDays(2))
                );

        // when
        divisionDAO.save(expectedDivision);
        Division updatedDivision =
                expectedDivision.merge(new Division()
                        .withName("new division name")
                        .withLeague(league));
        divisionDAO.update(updatedDivision);

        // then
        Division actualDivision = divisionDAO.findById(expectedDivision.getId());
        try {
            assertEquals(updatedDivision.incrementVersion(), actualDivision);
            assertEquals("new division name", actualDivision.getName());
        } finally {
            for (Round round : expectedDivision.getRounds()) {
                roundDAO.delete(round);
            }
            divisionDAO.delete(expectedDivision);
        }
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
                .withLeague(league);
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
                .withLeague(league);
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
