package org.squashleague.web.controller.administration;

import com.eaio.uuid.UUID;
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
    protected League leagueThree;
    @Resource
    protected DivisionDAO divisionDAO;
    protected Division divisionOne;
    protected Division divisionTwo;
    @Resource
    protected RoundDAO roundDAO;
    protected Round roundOne;
    protected Round roundTwo;
    @Resource
    protected PlayerDAO playerDAO;
    protected Player playerOne;
    protected Player playerTwo;
    protected Player playerThree;
    protected Player playerFour;
    @Resource
    protected MatchDAO matchDAO;
    protected Match matchOne;
    protected Match matchTwo;
    protected Match matchThree;
    protected Match matchFour;
    protected Match matchFive;

    @Before
    public void setupData() {
        role = new Role()
                .withName(Role.ROLE_ADMIN.getName())
                .withDescription("role description");
        roleDAO.save(role);
        userOne = new User()
                .withEmail("one@email.com")
                .withName("playerOne name")
                .withMobile("666 666 666")
                .withMobilePrivacy(MobilePrivacy.SHOW_OPPONENTS)
                .withOneTimeToken(new UUID().toString())
                .withPassword("abc123$%^")
                .withRoles(role);
        userTwo = new User()
                .withEmail("two@email.com")
                .withName("playerTwo name")
                .withMobile("666 666 666")
                .withMobilePrivacy(MobilePrivacy.SECRET)
                .withOneTimeToken(new UUID().toString())
                .withPassword("abc123$%^")
                .withRoles(role);
        userThree = new User()
                .withEmail("three@email.com")
                .withName("playerThree name")
                .withMobile("666 666 666")
                .withMobilePrivacy(MobilePrivacy.SECRET)
                .withOneTimeToken(new UUID().toString())
                .withPassword("abc123$%^")
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
        leagueThree = new League()
                .withName("league three")
                .withClub(club);
        leagueDAO.save(leagueOne);
        leagueDAO.save(leagueTwo);
        leagueDAO.save(leagueThree);
        divisionOne = new Division()
                .withName("division one")
                .withLeague(leagueOne);
        divisionTwo = new Division()
                .withName("division two")
                .withLeague(leagueTwo);
        divisionDAO.save(divisionOne);
        divisionDAO.save(divisionTwo);
        roundOne = new Round()
                .withStartDate(new DateTime().plusDays(1))
                .withEndDate(new DateTime().plusDays(2))
                .withDivision(divisionOne);
        roundTwo = new Round()
                .withStartDate(new DateTime().plusDays(2))
                .withEndDate(new DateTime().plusDays(3))
                .withDivision(divisionTwo);
        roundDAO.save(roundOne);
        roundDAO.save(roundTwo);
        playerOne = new Player()
                .withCurrentDivision(divisionOne)
                .withStatus(PlayerStatus.ACTIVE)
                .withUser(userOne);
        playerTwo = new Player()
                .withCurrentDivision(divisionOne)
                .withStatus(PlayerStatus.ACTIVE)
                .withUser(userTwo);
        playerThree = new Player()
                .withCurrentDivision(divisionOne)
                .withStatus(PlayerStatus.INACTIVE)
                .withUser(userThree);
        playerFour = new Player()
                .withCurrentDivision(divisionTwo)
                .withStatus(PlayerStatus.ACTIVE)
                .withUser(userOne);
        playerDAO.save(playerOne);
        playerDAO.save(playerTwo);
        playerDAO.save(playerThree);
        playerDAO.save(playerFour);
        matchOne = new Match()
                .withPlayerOne(playerOne)
                .withPlayerTwo(playerTwo)
                .withRound(roundOne)
                .withScore("3-2");
        matchTwo = new Match()
                .withPlayerOne(playerTwo)
                .withPlayerTwo(playerOne)
                .withRound(roundOne);
        matchThree = new Match()
                .withPlayerOne(playerTwo)
                .withPlayerTwo(playerThree)
                .withRound(roundOne);
        matchFour = new Match()
                .withPlayerOne(playerThree)
                .withPlayerTwo(playerOne)
                .withRound(roundTwo);
        matchFive = new Match()
                .withPlayerOne(playerFour)
                .withPlayerTwo(playerOne)
                .withRound(roundTwo);
        matchDAO.save(matchOne);
        matchDAO.save(matchTwo);
        matchDAO.save(matchThree);
        matchDAO.save(matchFour);
        matchDAO.save(matchFive);
    }

    @After
    public void teardownDatabase() {
        matchDAO.delete(matchOne);
        matchDAO.delete(matchTwo);
        matchDAO.delete(matchThree);
        matchDAO.delete(matchFour);
        matchDAO.delete(matchFive);
        playerDAO.delete(playerOne);
        playerDAO.delete(playerTwo);
        playerDAO.delete(playerThree);
        playerDAO.delete(playerFour);
        roundDAO.delete(roundOne);
        roundDAO.delete(roundTwo);
        divisionDAO.delete(divisionOne);
        divisionDAO.delete(divisionTwo);
        leagueDAO.delete(leagueOne);
        leagueDAO.delete(leagueTwo);
        leagueDAO.delete(leagueThree);
        clubDAO.delete(club);
        userDAO.delete(userOne);
        userDAO.delete(userTwo);
        userDAO.delete(userThree);
        roleDAO.delete(role);
    }
}
