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
public class RoundDAOIntegrationTest extends AdministratorLoggedInTest {

    @Resource
    private RoundDAO roundDAO;
    @Resource
    private ClubDAO clubDAO;
    private Club club;
    @Resource
    private LeagueDAO leagueDAO;
    private League league;
    @Resource
    private DivisionDAO divisionDAO;
    private Division division;

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
    }

    @After
    public void teardownDatabase() {
        divisionDAO.delete(division);
        leagueDAO.delete(league);
        clubDAO.delete(club);
    }

    @Test
    public void shouldSaveRequiredFieldsAndRetrieveById() throws Exception {
        // given
        Round expectedRound = new Round()
                .withStartDate(new DateTime().plusDays(1))
                .withEndDate(new DateTime().plusDays(2))
                .withDivision(division);

        // when
        roundDAO.save(expectedRound);

        // then
        assertEquals(expectedRound, roundDAO.findById(expectedRound.getId()));
        roundDAO.delete(expectedRound);
    }

    @Test
    public void shouldSaveAllFieldsWithObjectHierarchyAndRetrieveById() throws Exception {
        // given
        Round expectedRound = new Round()
                .withStartDate(new DateTime().plusDays(1))
                .withEndDate(new DateTime().plusDays(2))
                .withRoundStatus(RoundStatus.INPLAY)
                .withDivision(division);

        // when
        roundDAO.save(expectedRound);

        // then
        assertEquals(expectedRound, roundDAO.findById(expectedRound.getId()));
        roundDAO.delete(expectedRound);
    }

    @Test
    public void shouldSaveAndRetrieveList() throws Exception {
        // given
        Round roundOne = new Round()
                .withStartDate(new DateTime().plusDays(1))
                .withEndDate(new DateTime().plusDays(2))
                .withDivision(division);
        Round roundTwo = new Round()
                .withStartDate(new DateTime().plusDays(1))
                .withEndDate(new DateTime().plusDays(2))
                .withDivision(division);

        // when
        roundDAO.save(roundOne);
        roundDAO.save(roundTwo);

        // then
        assertArrayEquals(new Round[]{roundOne, roundTwo}, roundDAO.findAll().toArray());
        roundDAO.delete(roundOne);
        roundDAO.delete(roundTwo);
    }

    @Test
    public void shouldSaveAndRetrieveAndDelete() throws Exception {
        // given
        Round expectedRound = new Round()
                .withStartDate(new DateTime().plusDays(1))
                .withEndDate(new DateTime().plusDays(2))
                .withDivision(division);
        roundDAO.save(expectedRound);
        assertEquals(expectedRound, roundDAO.findById(expectedRound.getId()));

        // when
        roundDAO.delete(expectedRound);

        // then
        assertNull(roundDAO.findById(expectedRound.getId()));
    }

}
