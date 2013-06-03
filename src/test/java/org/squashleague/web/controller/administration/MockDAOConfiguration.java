package org.squashleague.web.controller.administration;

import org.joda.time.DateTime;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.squashleague.dao.account.RoleDAO;
import org.squashleague.dao.account.UserDAO;
import org.squashleague.dao.league.*;
import org.squashleague.domain.account.MobilePrivacy;
import org.squashleague.domain.account.Role;
import org.squashleague.domain.account.User;
import org.squashleague.domain.league.*;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author jamesdbloom
 */
//@Configuration
public class MockDAOConfiguration {

    private static final List<Role> roles = new ArrayList<>();
    private static final List<User> users = new ArrayList<>();
    private static final List<Club> clubs = new ArrayList<>();
    private static final List<League> leagues = new ArrayList<>();
    private static final List<Division> divisions = new ArrayList<>();
    private static final List<Round> rounds = new ArrayList<>();
    private static final List<Player> players = new ArrayList<>();
    private static final List<Match> matches = new ArrayList<>();

    static {
        for (int i = 0; i < 3; i++) {
            roles.add(
                    (Role) new Role()
                            .withName("role " + i)
                            .withDescription("description " + i)
                            .withId((long) i)
            );
            users.add(
                    (User) new User()
                            .withName("user " + i)
                            .withEmail("user_" + i + "@email.com")
                            .withMobile("123456789")
                            .withMobilePrivacy(MobilePrivacy.SHOW_ALL)
                            .withRoles(Role.ROLE_USER)
                            .withId((long) i)
            );
            clubs.add(
                    (Club) new Club()
                            .withName("club " + i)
                            .withAddress("address " + i)
                            .withId((long) i)
            );
            leagues.add(
                    (League) new League()
                            .withName("league " + i)
                            .withClub(clubs.get(i))
                            .withId((long) i)
            );
            divisions.add(
                    (Division) new Division()
                            .withName("division " + i)
                            .withLeague(leagues.get(i))
                            .withId((long) i)
            );
            rounds.add(
                    (Round) new Round()
                            .withStartDate(new DateTime().plus(1))
                            .withEndDate(new DateTime().plus(2))
                            .withDivision(divisions.get(i))
                            .withId((long) i)
            );
            players.add(
                    (Player) new Player()
                            .withUser(users.get(i))
                            .withCurrentDivision(divisions.get(i))
                            .withStatus(PlayerStatus.ACTIVE)
                            .withId((long) i)
            );
            matches.add(
                    (Match) new Match()
                            .withPlayerOne(players.get(i))
                            .withPlayerTwo(players.get(i))
                            .withRound(rounds.get(i))
                            .withId((long) i)
            );
        }
    }

    @Bean
    public RoleDAO roleDAO() {
        RoleDAO mock = mock(RoleDAO.class);
        when(mock.findAll()).thenReturn(roles);
        return mock;
    }

    @Bean
    public UserDAO userDAO() {
        UserDAO mock = mock(UserDAO.class);
        when(mock.findAll()).thenReturn(users);
        return mock;
    }

    @Bean
    public ClubDAO clubDAO() {
        ClubDAO mock = mock(ClubDAO.class);
        when(mock.findAll()).thenReturn(clubs);
        return mock;
    }

    @Bean
    public LeagueDAO leagueDAO() {
        LeagueDAO mock = mock(LeagueDAO.class);
        when(mock.findAll()).thenReturn(leagues);
        return mock;
    }

    @Bean
    public DivisionDAO divisionDAO() {
        DivisionDAO mock = mock(DivisionDAO.class);
        when(mock.findAll()).thenReturn(divisions);
        return mock;
    }

    @Bean
    public RoundDAO roundDAO() {
        RoundDAO mock = mock(RoundDAO.class);
        when(mock.findAll()).thenReturn(rounds);
        return mock;
    }

    @Bean
    public PlayerDAO playerDAO() {
        PlayerDAO mock = mock(PlayerDAO.class);
        when(mock.findAll()).thenReturn(players);
        return mock;
    }

    @Bean
    public MatchDAO matchDAO() {
        MatchDAO mock = mock(MatchDAO.class);
        when(mock.findAll()).thenReturn(matches);
        return mock;
    }
}
