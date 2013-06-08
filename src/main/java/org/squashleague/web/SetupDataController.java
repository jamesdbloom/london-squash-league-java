package org.squashleague.web;

import org.joda.time.DateTime;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.squashleague.dao.account.RoleDAO;
import org.squashleague.dao.account.UserDAO;
import org.squashleague.dao.league.*;
import org.squashleague.domain.account.MobilePrivacy;
import org.squashleague.domain.account.Role;
import org.squashleague.domain.account.User;
import org.squashleague.domain.league.*;
import org.squashleague.service.security.SpringSecurityUserContext;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author jamesdbloom
 */
@Controller
public class SetupDataController {

    @Resource
    private RoleDAO roleDAO;
    @Resource
    private UserDAO userDAO;
    @Resource
    private ClubDAO clubDAO;
    @Resource
    private LeagueDAO leagueDAO;
    @Resource
    private DivisionDAO divisionDAO;
    @Resource
    private RoundDAO roundDAO;
    @Resource
    private PlayerDAO playerDAO;
    @Resource
    private MatchDAO matchDAO;
    @Resource
    private SpringSecurityUserContext securityUserContext;

    @Transactional
    @RequestMapping("/setupTestData")
    public String setupData(Integer count) {
        // delete all
        for (Match match : matchDAO.findAll()) {
            matchDAO.delete(match);
        }
        for (Player player : playerDAO.findAll()) {
            playerDAO.delete(player);
        }
        for (Round round : roundDAO.findAll()) {
            roundDAO.delete(round);
        }
        for (Division division : divisionDAO.findAll()) {
            divisionDAO.delete(division);
        }
        for (Division division : divisionDAO.findAll()) {
            divisionDAO.delete(division);
        }
        for (League league : leagueDAO.findAll()) {
            leagueDAO.delete(league);
        }
        for (Club club : clubDAO.findAll()) {
            clubDAO.delete(club);
        }
        for (User user : userDAO.findAll()) {
            if (!user.getEmail().equals("jamesdbloom@gmail.com")) {
                userDAO.delete(user);
            }
        }
        Role role = roleDAO.findByName(Role.ROLE_USER.getName());
        if (role != null) {
            roleDAO.delete(role);
            Role.ROLE_USER.setVersion(null);
            Role.ROLE_USER.setId(null);
        }
        // create all
        roleDAO.save(Role.ROLE_USER);
        for (int c = 0; c < count + 1; c++) {
            Club club = new Club()
                    .withName("club name " + c)
                    .withAddress("address " + c);
            clubDAO.save(club);
            for (int l = 0; l < count + 1; l++) {
                League league = new League()
                        .withName("League name " + l + "-" + c)
                        .withClub(club);
                leagueDAO.save(league);
                int dmax = count + 1;
                for (int d = 0; d < dmax; d++) {
                    Division division = new Division()
                            .withName("Division name " + d + "-" + l + "-" + c)
                            .withLeague(league);
                    divisionDAO.save(division);
                    List<Player> players = new ArrayList<>();
                    List<User> users = new ArrayList<>();
                    int pmax = (count + 6) * dmax;
                    for (int p = 0; p < pmax; p++) {
                        if (p > (pmax / dmax) * d && p < (pmax / dmax) * (d + 1)) {
                            User user = new User()
                                    .withEmail(p + "_" + d + "_" + l + "_" + c + "@email.com")
                                    .withName(p + "_" + d + "_" + l + "_" + c + " name")
                                    .withMobilePrivacy(MobilePrivacy.SECRET)
                                    .withRoles(Role.ROLE_USER);
                            users.add(user);
                            players.add(new Player()
                                    .withCurrentDivision(division)
                                    .withStatus(PlayerStatus.ACTIVE)
                                    .withUser(user));
                        }
                    }
                    if (d == 0) {
                        players.add(new Player()
                                .withCurrentDivision(division)
                                .withStatus(PlayerStatus.ACTIVE)
                                .withUser(userDAO.findById(securityUserContext.getCurrentUser().getId())));
                    }
                    for (User user : users) {
                        userDAO.save(user);
                    }
                    for (Player player : players) {
                        playerDAO.save(player);
                    }
                    for (int r = 0; r < count + 1; r++) {
                        Round round = new Round()
                                .withStartDate(new DateTime().plusDays(1 + r + l + c))
                                .withEndDate(new DateTime().plusDays(5 + r + l + c))
                                .withDivision(division);
                        roundDAO.save(round);
                        for (Match match : createMatches(players, round)) {
                            matchDAO.save(match);
                        }
                    }
                }
            }
        }
        return "redirect:/administration";
    }

    private List<Match> createMatches(List<Player> players, Round round) {
        List<Match> matches = new ArrayList<>();
        Set<String> playerCombinations = new HashSet<>();
        for (Player playerOne : players) {
            for (Player playerTwo : players) {
                if (!playerOne.getId().equals(playerTwo.getId())) {
                    String playerOneFirst = String.valueOf(playerOne.getId()) + String.valueOf(playerTwo.getId());
                    String playerTwoFirst = String.valueOf(playerTwo.getId()) + String.valueOf(playerOne.getId());
                    if (!playerCombinations.contains(playerOneFirst) && !playerCombinations.contains(playerTwoFirst)) {
                        playerCombinations.add(playerOneFirst);
                        matches.add(new Match().withPlayerOne(playerOne).withPlayerTwo(playerTwo).withRound(round));
                    }
                }
            }
        }
        return matches;
    }
}
