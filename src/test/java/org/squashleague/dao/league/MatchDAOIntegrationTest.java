package org.squashleague.dao.league;

import org.joda.time.DateTime;
import org.squashleague.configuration.RootConfiguration;
import org.squashleague.dao.account.UserDAO;
import org.squashleague.domain.account.MobilePrivacy;
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
import java.util.Date;

import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * @author squashleague
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = RootConfiguration.class, initializers = HSQLApplicationContextInitializer.class)
public class MatchDAOIntegrationTest {

    @Resource
    private MatchDAO matchDAO;
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
    @Resource
    private UserDAO userDAO;
    private User userOne;
    private User userTwo;
    @Resource
    private PlayerDAO playerDAO;
    private Player playerOne;
    private Player playerTwo;

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
        userOne = new User()
                .withEmail("user@email.com")
                .withName("playerOne name")
                .withMobilePrivate(MobilePrivacy.SECRET);
        userTwo = new User()
                .withEmail("user@email.com")
                .withName("playerTwo name")
                .withMobilePrivate(MobilePrivacy.SECRET);
        userDAO.save(userOne);
        userDAO.save(userTwo);
        playerOne = new Player()
                .withCurrentDivision(division)
                .withPlayerStatus(PlayerStatus.ACTIVE)
                .withUser(userOne);
        playerTwo = new Player()
                .withCurrentDivision(division)
                .withPlayerStatus(PlayerStatus.ACTIVE)
                .withUser(userTwo);
        playerDAO.save(playerOne);
        playerDAO.save(playerTwo);
    }

    @After
    public void teardownDatabase() {
        playerDAO.delete(playerOne);
        playerDAO.delete(playerTwo);
        userDAO.delete(userOne);
        userDAO.delete(userTwo);
        roundDAO.delete(round);
        divisionDAO.delete(division);
        leagueDAO.delete(league);
        clubDAO.delete(club);
    }

    @Test
    public void shouldSaveRequiredFieldsAndRetrieveById() throws Exception {
        // given
        Match expectedMatch = new Match()
                .withPlayerOne(playerOne)
                .withPlayerTwo(playerTwo)
                .withRound(round);

        // when
        matchDAO.save(expectedMatch);

        // then
        assertEquals(expectedMatch, matchDAO.findOne(expectedMatch.getId()));
        matchDAO.delete(expectedMatch);
    }

    @Test
    public void shouldSaveAllFieldsWithObjectHierarchyAndRetrieveById() throws Exception {
        // given
        Match expectedMatch = new Match()
                .withPlayerOne(playerOne)
                .withPlayerTwo(playerTwo)
                .withRound(round);

        // when
        matchDAO.save(expectedMatch);

        // then
        assertEquals(expectedMatch, matchDAO.findOne(expectedMatch.getId()));
        matchDAO.delete(expectedMatch);
    }

    @Test
    public void shouldSaveAndRetrieveList() throws Exception {
        // given
        Match matchOne = new Match()
                .withPlayerOne(playerOne)
                .withPlayerTwo(playerTwo)
                .withRound(round);
        Match matchTwo = new Match()
                .withPlayerOne(playerTwo)
                .withPlayerTwo(playerOne)
                .withRound(round);

        // when
        matchDAO.save(matchOne);
        matchDAO.save(matchTwo);

        // then
        assertArrayEquals(new Match[]{matchOne, matchTwo}, matchDAO.findAll().toArray());
        matchDAO.delete(matchOne);
        matchDAO.delete(matchTwo);
    }

    @Test
    public void shouldSaveAndRetrieveAndDelete() throws Exception {
        // given
        Match expectedMatch = new Match()
                .withPlayerOne(playerOne)
                .withPlayerTwo(playerTwo)
                .withRound(round);
        matchDAO.save(expectedMatch);
        assertEquals(expectedMatch, matchDAO.findOne(expectedMatch.getId()));

        // when
        matchDAO.delete(expectedMatch);

        // then
        assertNull(matchDAO.findOne(expectedMatch.getId()));
    }

}
