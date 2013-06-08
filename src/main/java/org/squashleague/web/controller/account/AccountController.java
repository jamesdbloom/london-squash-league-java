package org.squashleague.web.controller.account;

import org.springframework.core.env.Environment;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.squashleague.dao.league.LeagueDAO;
import org.squashleague.dao.league.MatchDAO;
import org.squashleague.dao.league.PlayerDAO;
import org.squashleague.dao.league.RoundDAO;
import org.squashleague.domain.account.User;
import org.squashleague.domain.league.*;
import org.squashleague.service.http.RequestParser;
import org.squashleague.service.security.SpringSecurityUserContext;
import org.squashleague.web.tasks.CommandHolder;
import org.squashleague.web.tasks.account.LoadPlayerRounds;
import org.squashleague.web.tasks.account.LoadUnregisteredLeagues;
import org.squashleague.web.tasks.account.LoadUserAccountData;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.regex.Pattern;


/**
 * @author jamesdbloom
 */
@Controller
public class AccountController {

    private final static Pattern SCORE_PATTERN = Pattern.compile(Match.SCORE_PATTERN);
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
    private RequestParser requestParser;
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

    @RequestMapping(value = "/score/{id:[0-9]+}", method = RequestMethod.GET)
    public String matchScoreForm(@PathVariable("id") Long id, @RequestHeader(value = "Referer", required = false) String referer, Model uiModel) throws MalformedURLException, UnsupportedEncodingException {
        Match match = matchDAO.findById(id);
        if (match == null) {
            return "redirect:/errors/403";
        }
        uiModel.addAttribute("match", match);
        uiModel.addAttribute("environment", environment);
        uiModel.addAttribute("scorePattern", Match.SCORE_PATTERN);
        uiModel.addAttribute("referer", requestParser.parseRelativeURI(referer, "/account"));
        return "page/account/score";
    }

    @RequestMapping(value = "/score", method = RequestMethod.POST)
    public String updateMatchScore(Long id, String score, String referer, RedirectAttributes redirectAttributes) throws MalformedURLException, UnsupportedEncodingException {
        Match match = matchDAO.findById(id);
        if (match == null) {
            return "redirect:/errors/403";
        }
        if (!SCORE_PATTERN.matcher(String.valueOf(score)).matches()) {
            redirectAttributes.addFlashAttribute("validationErrors", Arrays.asList(environment.getProperty("validation.match.score")));
            return "redirect:/score/" + match.getId();
        }
        matchDAO.update(match.withScore(score));
        return "redirect:" + requestParser.parseRelativeURI(referer, "/account");
    }
}
