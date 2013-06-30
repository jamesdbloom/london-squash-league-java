package org.squashleague.web.controller.administration;

import com.eaio.uuid.UUID;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
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
import java.util.*;

/**
 * @author jamesdbloom
 */
public class MockDAOTest extends AdministratorLoggedInTest {

    public static final int NO_OF_USERS = 7;
    @Resource
    protected RoleDAO roleDAO;
    protected Role role;
    @Resource
    protected UserDAO userDAO;
    protected List<User> users = new ArrayList<>();
    protected List<User> usersBothDivisions = new ArrayList<>();
    @Resource
    protected ClubDAO clubDAO;
    protected List<Club> clubs = new ArrayList<>();
    @Resource
    protected LeagueDAO leagueDAO;
    protected List<League> leagues = new ArrayList<>();
    protected List<League> leaguesNoPlayers = new ArrayList<>();
    @Resource
    protected DivisionDAO divisionDAO;
    protected List<Division> divisions = new ArrayList<>();
    @Resource
    protected RoundDAO roundDAO;
    protected List<Round> rounds = new ArrayList<>();
    @Resource
    protected PlayerDAO playerDAO;
    protected List<Player> playersDivisionZero = new ArrayList<>();
    protected List<Player> playersDivisionOne = new ArrayList<>();
    protected List<Player> playersUserBothDivisions = new ArrayList<>();
    protected List<Player> playersNoDivision = new ArrayList<>();
    protected List<Player> players = new ArrayList<>();
    @Resource
    protected MatchDAO matchDAO;
    protected List<Match> matchesDivisionZero = new ArrayList<>();
    protected List<Match> matchesDivisionOne = new ArrayList<>();
    protected List<Match> matches = new ArrayList<>();

    @Before
    public void setupData() {
        role = new Role()
                .withName(Role.ROLE_ADMIN.getName())
                .withDescription("role description");
        roleDAO.save(role);

        for (int u = 0; u < NO_OF_USERS; u++) {
            User user = new User()
                    .withEmail(u + "@email.com")
                    .withName("user" + u + " name")
                    .withMobile("07515 " + u + u + u + " " + u + u + u)
                    .withMobilePrivacy(MobilePrivacy.values()[u % MobilePrivacy.values().length])
                    .withOneTimeToken(new UUID().toString())
                    .withPassword("abc123$%^")
                    .withRoles(role);
            users.add(user);
            userDAO.save(user);
        }

        clubs = Arrays.asList(
                new Club()
                        .withName("club name")
                        .withAddress("address")
        );
        for (Club club : clubs) {
            clubDAO.save(club);
        }

        for (int l = 0; l < 3; l++) {
            League league = new League()
                    .withName("league " + l)
                    .withClub(clubs.get(0));
            if (l < 2) {
                leagues.add(league);
            } else {
                leagues.add(league);
                leaguesNoPlayers.add(league);
            }
            leagueDAO.save(league);
        }

        for (int r = 0; r < 2; r++) {
            Round round = new Round()
                    .withStartDate(new DateTime().plusDays(r + 1))
                    .withEndDate(new DateTime().plusDays(r + 11))
                    .withLeague(leagues.get(r));
            rounds.add(round);
            roundDAO.save(round);
        }

        for (int d = 0; d < 2; d++) {
            Division division = new Division()
                    .withName(d + 1)
                    .withRound(rounds.get(d));
            divisions.add(division);
            divisionDAO.save(division);
        }

        for (int u = 0; u < NO_OF_USERS - 3; u++) {
            if (u == 0) {

                Player divisionZeroPlayer = new Player()
                        .withCurrentDivision(divisions.get(0))
                        .withStatus(PlayerStatus.ACTIVE)
                        .withUser(users.get(u));
                playersDivisionZero.add(divisionZeroPlayer);
                playersUserBothDivisions.add(divisionZeroPlayer);

                Player divisionOnePlayer = new Player()
                        .withCurrentDivision(divisions.get(1))
                        .withStatus(PlayerStatus.ACTIVE)
                        .withUser(users.get(u));
                playersDivisionOne.add(divisionOnePlayer);
                playersUserBothDivisions.add(divisionOnePlayer);

                usersBothDivisions.add(users.get(u));
            } else if (u < 3) {
                playersDivisionZero.add(new Player()
                        .withCurrentDivision(divisions.get(0))
                        .withStatus(PlayerStatus.ACTIVE)
                        .withUser(users.get(u)));
            } else {
                playersDivisionOne.add(new Player()
                        .withCurrentDivision(divisions.get(1))
                        .withStatus(PlayerStatus.ACTIVE)
                        .withUser(users.get(u)));
            }
        }

        playersNoDivision.add(new Player()
                .withLeague(leagues.get(1))
                .withStatus(PlayerStatus.ACTIVE)
                .withUser(users.get(NO_OF_USERS - 2)));

        players.addAll(playersDivisionZero);
        players.addAll(playersDivisionOne);
        players.addAll(playersNoDivision);

        for (Player player : players) {
            playerDAO.save(player);
        }

        matchesDivisionZero.addAll(createMatches(playersDivisionZero, divisions.get(0)));
        matchesDivisionOne.addAll(createMatches(playersDivisionOne, divisions.get(1)));

        matches.addAll(matchesDivisionZero);
        matches.addAll(matchesDivisionOne);

        for (Match match : matches) {
            matchDAO.save(match);
        }
    }

    protected List<Match> getUserMatches(List<Match> matches, final User user) {
        ArrayList<Match> userMatches = new ArrayList<>(Collections2.filter(matches, new Predicate<Match>() {
            public boolean apply(Match match) {
                return match.getPlayerOne().getUser().getId().equals(user.getId()) || match.getPlayerTwo().getUser().getId().equals(user.getId());
            }
        }));
        Collections.sort(userMatches);
        return userMatches;
    }

    protected <T> List<T> addLists(List<T>... lists) {
        List<T> total = new ArrayList<>();
        for (List<T> list : lists) {
            total.addAll(list);
        }
        return total;
    }

    @After
    public void teardownDatabase() {
        for (Match match : matches) {
            matchDAO.delete(match);
        }
        for (Player player : players) {
            playerDAO.delete(player);
        }
        for (Division division : divisions) {
            divisionDAO.delete(division);
        }
        for (Round round : rounds) {
            roundDAO.delete(round);
        }
        for (League league : leagues) {
            leagueDAO.delete(league);
        }
        for (Club club : clubs) {
            clubDAO.delete(club);
        }
        for (User user : users) {
            userDAO.delete(user);
        }
        roleDAO.delete(role);
    }

    private List<Match> createMatches(List<Player> players, Division division) {
        List<Match> matches = new ArrayList<>();
        Set<String> playerCombinations = new HashSet<>();
        for (Player playerOne : players) {
            for (Player playerTwo : players) {
                if (!playerOne.getId().equals(playerTwo.getId())) {
                    String playerOneFirst = String.valueOf(playerOne.getId()) + String.valueOf(playerTwo.getId());
                    String playerTwoFirst = String.valueOf(playerTwo.getId()) + String.valueOf(playerOne.getId());
                    if (!playerCombinations.contains(playerOneFirst) && !playerCombinations.contains(playerTwoFirst)) {
                        playerCombinations.add(playerOneFirst);
                        matches.add(new Match().withPlayerOne(playerOne).withPlayerTwo(playerTwo).withDivision(division));
                    }
                }
            }
        }
        return matches;
    }
}
