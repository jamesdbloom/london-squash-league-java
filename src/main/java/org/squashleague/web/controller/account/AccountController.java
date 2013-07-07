package org.squashleague.web.controller.account;

import org.springframework.core.env.Environment;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.squashleague.dao.league.LeagueDAO;
import org.squashleague.dao.league.MatchDAO;
import org.squashleague.dao.league.PlayerDAO;
import org.squashleague.dao.league.RoundDAO;
import org.squashleague.domain.account.User;
import org.squashleague.domain.league.League;
import org.squashleague.domain.league.Player;
import org.squashleague.domain.league.PlayerStatus;
import org.squashleague.domain.league.Round;
import org.squashleague.service.security.SpringSecurityUserContext;
import org.squashleague.web.tasks.CommandHolder;
import org.squashleague.web.tasks.account.LoadPlayerRounds;
import org.squashleague.web.tasks.account.LoadUnregisteredLeagues;
import org.squashleague.web.tasks.account.LoadUserAccountData;

import javax.annotation.Resource;
import java.util.Arrays;


/**
 * @author jamesdbloom
 */
@Controller
public class AccountController {

    @Resource
    private Environment environment;
    @Resource
    private MatchDAO matchDAO;
    @Resource
    private PlayerDAO playerDAO;
    @Resource
    private LeagueDAO leagueDAO;
    @Resource
    private RoundDAO roundDAO;
    @Resource
    private SpringSecurityUserContext securityUserContext;
    @Resource
    private ThreadPoolTaskExecutor taskExecutor;

    @RequestMapping(value = "/account", method = RequestMethod.GET)
    public String getAccountPage(Model uiModel) {
        uiModel.addAttribute("user", CommandHolder.newInstance(taskExecutor.submit(new LoadUserAccountData(matchDAO, playerDAO, securityUserContext.getCurrentUser())), User.class));
        uiModel.addAttribute("rounds", CommandHolder.newListInstance(taskExecutor.submit(new LoadPlayerRounds(roundDAO, securityUserContext.getCurrentUser())), Round.class));
        uiModel.addAttribute("unregisteredLeagues", CommandHolder.newListInstance(taskExecutor.submit(new LoadUnregisteredLeagues(leagueDAO, securityUserContext.getCurrentUser())), League.class));
        uiModel.addAttribute("environment", environment);
        return "page/account/account";
    }

    @Transactional
    @RequestMapping(value = "/account/unregister", params = "player", method = RequestMethod.GET)
    public String unregisterPlayer(@RequestParam("player") Long id) {
        Player player = playerDAO.findById(id);
        if (player != null) {
            playerDAO.updateStatus(player, PlayerStatus.INACTIVE);
        }
        return "redirect:/account";
    }

    @Transactional
    @RequestMapping(value = "/account/register", params = "player", method = RequestMethod.GET)
    public String registerPlayer(@RequestParam("player") Long id) {
        Player player = playerDAO.findById(id);
        if (player != null) {
            playerDAO.updateStatus(player, PlayerStatus.ACTIVE);
        }
        return "redirect:/account";
    }

    @Transactional
    @RequestMapping(value = "/account/register", params = "league", method = RequestMethod.POST)
    public String registerLeague(Long league, RedirectAttributes redirectAttributes) {
        if (league == null) {
            redirectAttributes.addFlashAttribute("validationErrors", Arrays.asList(environment.getProperty("validation.player.league")));
            return "redirect:/account";
        }
        Player player =
                new Player()
                        .withStatus(PlayerStatus.ACTIVE)
                        .withUser(securityUserContext.getCurrentUser())
                        .withLeague(leagueDAO.findById(league));
        playerDAO.save(player);
        return "redirect:/account";
    }

}
