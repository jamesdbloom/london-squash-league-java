package org.squashleague.web.controller.account;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Maps;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.squashleague.dao.account.UserDAO;
import org.squashleague.dao.league.LeagueDAO;
import org.squashleague.dao.league.MatchDAO;
import org.squashleague.dao.league.PlayerDAO;
import org.squashleague.domain.ModelObject;
import org.squashleague.domain.account.User;
import org.squashleague.domain.league.League;
import org.squashleague.domain.league.Match;
import org.squashleague.domain.league.Player;
import org.squashleague.domain.league.PlayerStatus;
import org.squashleague.service.security.SpringSecurityUserContext;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.*;
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
    private SpringSecurityUserContext securityUserContext;

    @Transactional
    @RequestMapping(value = "/account", method = RequestMethod.GET)
    public String getAccountPage(Model uiModel) {

        final User user = securityUserContext.getCurrentUser();
        List<Player> players = playerDAO.findAllByUser(user);
        Map<Long, Player> playerById = Maps.uniqueIndex(players, ModelObject.TO_MAP);
        List<Match> matches = matchDAO.findAllByUser(user);
        for (Match match : matches) {
            if (playerById.containsKey(match.getPlayerOne().getId())) {
                playerById.get(match.getPlayerOne().getId()).getMatches().add(match);
            }
            if (playerById.containsKey(match.getPlayerOne().getId())) {
                playerById.get(match.getPlayerOne().getId()).getMatches().add(match);
            }
        }
        user.setPlayers(players);
        Collection<League> unregisteredLeagues = Collections2.filter(leagueDAO.findAll(), new Predicate<League>() {
            public boolean apply(League league) {
                for (Player player : user.getPlayers()) {
                    if (player.getLeague().getId().equals(league.getId())) {
                        return false;
                    }
                }
                return true;
            }
        });
        uiModel.addAttribute("environment", environment);
        uiModel.addAttribute("unregisteredLeagues", unregisteredLeagues);
        uiModel.addAttribute("user", user);
        return "page/account/account";
    }

    @Transactional
    @RequestMapping(value = "/account/unregister", params = "player", method = {RequestMethod.GET, RequestMethod.POST})
    public String unregisterPlayer(@RequestParam("player") Long id) {
        Player player = playerDAO.findById(id);
        if (player != null) {
            playerDAO.updateStatus(player, PlayerStatus.INACTIVE);
        }
        return "redirect:/account";
    }

    @Transactional
    @RequestMapping(value = "/account/register", params = "player", method = {RequestMethod.GET, RequestMethod.POST})
    public String registerPlayer(@RequestParam("player") Long id) {
        Player player = playerDAO.findById(id);
        if (player != null) {
            playerDAO.updateStatus(player, PlayerStatus.ACTIVE);
        }
        return "redirect:/account";
    }

    @Transactional
    @RequestMapping(value = "/account/register", params = "league", method = {RequestMethod.GET, RequestMethod.POST})
    public String registerLeague(Long league, RedirectAttributes redirectAttributes) {
        if(league == null) {
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

    @RequestMapping(value = "/score/{id}", method = RequestMethod.GET)
    public String retrievePasswordForm(@PathVariable("id") Long id, Model uiModel) throws MalformedURLException, UnsupportedEncodingException {
        Match match = matchDAO.findById(id);
        if (match == null) {
            return "redirect:/errors/403";
        }
        uiModel.addAttribute("match", match);
        uiModel.addAttribute("environment", environment);
        uiModel.addAttribute("scorePattern", Match.SCORE_PATTERN);
        return "page/account/score";
    }

    @RequestMapping(value = "/score", method = RequestMethod.POST)
    public String sendUpdatePasswordEmail(Long id, String score, RedirectAttributes redirectAttributes) throws MalformedURLException, UnsupportedEncodingException {
        Match match = matchDAO.findById(id);
        if (match == null) {
            return "redirect:/errors/403";
        }
        if (!SCORE_PATTERN.matcher(String.valueOf(score)).matches()) {
            redirectAttributes.addFlashAttribute("validationErrors", Arrays.asList(environment.getProperty("validation.match.score")));
            return "redirect:/score/" + match.getId();
        }
        matchDAO.update(match.withScore(score));
        return "redirect:/account";
    }
}
