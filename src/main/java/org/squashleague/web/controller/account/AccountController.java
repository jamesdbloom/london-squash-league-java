package org.squashleague.web.controller.account;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.squashleague.dao.account.UserDAO;
import org.squashleague.dao.league.MatchDAO;
import org.squashleague.dao.league.PlayerDAO;
import org.squashleague.domain.account.User;
import org.squashleague.domain.league.Match;
import org.squashleague.domain.league.Player;
import org.squashleague.domain.league.PlayerStatus;
import org.squashleague.service.security.SpringSecurityUserContext;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
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
    private UserDAO userDAO;
    @Resource
    private MatchDAO matchDAO;
    @Resource
    private PlayerDAO playerDAO;
    @Resource
    private SpringSecurityUserContext securityUserContext;

    @Transactional
    @RequestMapping(value = "/account", method = RequestMethod.GET)
    public String getAccountPage(Model uiModel) {
        User user = userDAO.findById(securityUserContext.getCurrentUser().getId());
        for (Player player : user.getPlayers()) {
            player.withMatches(matchDAO.findAllByPlayer(player));
        }
        uiModel.addAttribute("user", user);
        return "page/account/account";
    }

    @Transactional
    @RequestMapping(value = "/account/unregister/{id}", method = RequestMethod.GET)
    public String unregisterPlayer(@PathVariable("id") Long id) {
        Player player = playerDAO.findById(id);
        if (player != null) {
            playerDAO.updateStatus(player, PlayerStatus.INACTIVE);
        }
        return "redirect:/account";
    }

    @Transactional
    @RequestMapping(value = "/account/register/{id}", method = RequestMethod.GET)
    public String registerPlayer(@PathVariable("id") Long id) {
        Player player = playerDAO.findById(id);
        if (player != null) {
            playerDAO.updateStatus(player, PlayerStatus.ACTIVE);
        }
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
            redirectAttributes.addFlashAttribute(("error"), environment.getProperty("validation.match.score"));
            return "redirect:/score/" + match.getId();
        }
        matchDAO.update(match.withScore(score));
        return "redirect:/account";
    }
}
