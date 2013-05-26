package org.squashleague.web.controller.league;

import org.joda.time.DateTime;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.squashleague.dao.account.RoleDAO;
import org.squashleague.dao.account.UserDAO;
import org.squashleague.dao.league.*;
import org.squashleague.domain.account.MobilePrivacy;
import org.squashleague.domain.account.Role;
import org.squashleague.domain.account.User;
import org.squashleague.domain.league.*;
import org.squashleague.service.security.SpringSecurityUserContext;

import javax.annotation.Resource;

/**
 * @author jamesdbloom
 */
@Controller
public class LeagueTableController {

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
    @RequestMapping("/setupData")
    public String setupData(Integer count) {
        Integer clubs = count + 1;
        Integer leagues = count + 1;
        Integer divisions = count + 3;
        Integer rounds = count + 3;
        roleDAO.save(Role.ROLE_USER);
        for (int c = 0; c < clubs; c++) {
            Club club = new Club()
                    .withName("club name " + c)
                    .withAddress("address " + c);
            clubDAO.save(club);
            for (int l = 0; l < leagues; l++) {
                League league = new League()
                        .withName("League name " + l + "-" + c)
                        .withClub(club);
                leagueDAO.save(league);
                for (int d = 0; d < divisions; d++) {
                    Division division = new Division()
                            .withName("Division name " + d + "-" + l + "-" + c)
                            .withLeague(league);
                    divisionDAO.save(division);
                    for (int r = 0; r < rounds; r++) {
                        Round round = new Round()
                                .withStartDate(new DateTime().plusDays(1 + r + d + l + c))
                                .withEndDate(new DateTime().plusDays(5 + r + d + l + c))
                                .withDivision(division);
                        roundDAO.save(round);
                        for (int p = 0; p < rounds; p++) {
                            User userOne = new User()
                                    .withEmail("one_" + p + "_" + r + "_" + d + "_" + l + "_" + c + "@email.com")
                                    .withName("one_" + p + "_" + r + "_" + d + "_" + l + "_" + c + " name")
                                    .withMobilePrivacy(MobilePrivacy.SECRET)
                                    .withRole(Role.ROLE_USER);
                            userDAO.save(userOne);
                            Player playerOne = new Player()
                                    .withCurrentDivision(division)
                                    .withPlayerStatus(PlayerStatus.ACTIVE)
                                    .withUser(userOne);
                            playerDAO.save(playerOne);
                            User userTwo = new User()
                                    .withEmail("two_" + p + "_" + r + "_" + d + "_" + l + "_" + c + "@email.com")
                                    .withName("two_" + p + "_" + r + "_" + d + "_" + l + "_" + c + " name")
                                    .withMobilePrivacy(MobilePrivacy.SECRET)
                                    .withRole(Role.ROLE_USER);
                            userDAO.save(userTwo);
                            Player playerTwo = new Player()
                                    .withCurrentDivision(division)
                                    .withPlayerStatus(PlayerStatus.ACTIVE)
                                    .withUser(userTwo);
                            playerDAO.save(playerTwo);
                            Player playerThree = new Player()
                                    .withCurrentDivision(division)
                                    .withPlayerStatus(PlayerStatus.ACTIVE)
                                    .withUser(userDAO.findById(securityUserContext.getCurrentUser().getId()));
                            playerDAO.save(playerThree);
                            Match matchOne = new Match()
                                    .withPlayerOne(playerOne)
                                    .withPlayerTwo(playerTwo)
                                    .withRound(round);
                            matchDAO.save(matchOne);
                            Match matchTwo = new Match()
                                    .withPlayerOne(playerTwo)
                                    .withPlayerTwo(playerOne)
                                    .withRound(round);
                            matchDAO.save(matchTwo);
                            Match matchThree = new Match()
                                    .withPlayerOne(playerThree)
                                    .withPlayerTwo(playerOne)
                                    .withRound(round);
                            matchDAO.save(matchThree);
                            Match matchFour = new Match()
                                    .withPlayerOne(playerThree)
                                    .withPlayerTwo(playerTwo)
                                    .withRound(round);
                            matchDAO.save(matchFour);
                            Match matchFive = new Match()
                                    .withPlayerOne(playerOne)
                                    .withPlayerTwo(playerThree)
                                    .withRound(round);
                            matchDAO.save(matchFive);
                            Match matchSix = new Match()
                                    .withPlayerOne(playerTwo)
                                    .withPlayerTwo(playerThree)
                                    .withRound(round);
                            matchDAO.save(matchSix);
                        }
                    }
                }
            }
        }
        return "redirect:/administration";
    }

    @Transactional
    @RequestMapping(value = "/leagueTable", method = RequestMethod.GET)
    public String getPage(Model uiModel) {

        User user = userDAO.findById(securityUserContext.getCurrentUser().getId());
        return "page/leagueTable";
    }
}
