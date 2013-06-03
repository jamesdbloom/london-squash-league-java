package org.squashleague.web.controller.administration;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.squashleague.dao.account.RoleDAO;
import org.squashleague.dao.account.UserDAO;
import org.squashleague.dao.league.*;
import org.squashleague.domain.account.MobilePrivacy;
import org.squashleague.domain.account.Role;
import org.squashleague.domain.account.User;
import org.squashleague.domain.league.*;
import org.squashleague.service.security.AdministratorLoggedInTest;

import javax.annotation.Resource;

/**
 * @author jamesdbloom
 */
public class MockDAOTest extends AdministratorLoggedInTest {

    @Resource
    protected RoleDAO roleDAO;
    protected Role role;
    @Resource
    protected UserDAO userDAO;
    protected User userOne;
    protected User userTwo;
    protected User userThree;
    @Resource
    protected ClubDAO clubDAO;
    protected Club club;
    @Resource
    protected LeagueDAO leagueDAO;
    protected League leagueOne;
    protected League leagueTwo;
    @Resource
    protected DivisionDAO divisionDAO;
    protected Division division;
    @Resource
    protected RoundDAO roundDAO;
    protected Round round;
    @Resource
    protected PlayerDAO playerDAO;
    protected Player playerOne;
    protected Player playerTwo;
    protected Player playerThree;
    @Resource
    protected MatchDAO matchDAO;
    protected Match matchOne;
    protected Match matchTwo;
    protected Match matchThree;
    protected Match matchFour;

    @Before
    public void setupData() {
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
        userThree = new User()
                .withEmail("three@email.com")
                .withName("playerThree name")
                .withMobilePrivacy(MobilePrivacy.SECRET)
                .withRoles(role);
        userDAO.save(userOne);
        userDAO.save(userTwo);
        userDAO.save(userThree);
        club = new Club()
                .withName("club name")
                .withAddress("address");
        clubDAO.save(club);
        leagueOne = new League()
                .withName("league one")
                .withClub(club);
        leagueTwo = new League()
                .withName("league two")
                .withClub(club);
        leagueDAO.save(leagueOne);
        leagueDAO.save(leagueTwo);
        division = new Division()
                .withName("division name")
                .withLeague(leagueOne);
        divisionDAO.save(division);
        round = new Round()
                .withStartDate(new DateTime().plusDays(1))
                .withEndDate(new DateTime().plusDays(2))
                .withDivision(division);
        roundDAO.save(round);
        playerOne = new Player()
                .withCurrentDivision(division)
                .withStatus(PlayerStatus.ACTIVE)
                .withUser(userOne);
        playerTwo = new Player()
                .withCurrentDivision(division)
                .withStatus(PlayerStatus.ACTIVE)
                .withUser(userTwo);
        playerThree = new Player()
                .withCurrentDivision(division)
                .withStatus(PlayerStatus.INACTIVE)
                .withUser(userThree);
        playerDAO.save(playerOne);
        playerDAO.save(playerTwo);
        playerDAO.save(playerThree);
        matchOne = new Match()
                .withPlayerOne(playerOne)
                .withPlayerTwo(playerTwo)
                .withRound(round);
        matchTwo = new Match()
                .withPlayerOne(playerTwo)
                .withPlayerTwo(playerOne)
                .withRound(round);
        matchThree = new Match()
                .withPlayerOne(playerTwo)
                .withPlayerTwo(playerThree)
                .withRound(round);
        matchFour = new Match()
                .withPlayerOne(playerThree)
                .withPlayerTwo(playerOne)
                .withRound(round);
        matchDAO.save(matchOne);
        matchDAO.save(matchTwo);
        matchDAO.save(matchThree);
        matchDAO.save(matchFour);
    }

    @After
    public void teardownDatabase() {
        matchDAO.delete(matchOne);
        matchDAO.delete(matchTwo);
        matchDAO.delete(matchThree);
        matchDAO.delete(matchFour);
        playerDAO.delete(playerOne);
        playerDAO.delete(playerTwo);
        playerDAO.delete(playerThree);
        roundDAO.delete(round);
        divisionDAO.delete(division);
        leagueDAO.delete(leagueOne);
        leagueDAO.delete(leagueTwo);
        clubDAO.delete(club);
        userDAO.delete(userOne);
        userDAO.delete(userTwo);
        userDAO.delete(userThree);
        roleDAO.delete(role);
    }
}
