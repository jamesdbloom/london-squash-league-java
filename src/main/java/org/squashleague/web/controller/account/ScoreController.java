package org.squashleague.web.controller.account;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.squashleague.dao.league.MatchDAO;
import org.squashleague.domain.league.Match;
import org.squashleague.service.http.RequestParser;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.regex.Pattern;

/**
 * @author jamesdbloom
 */
@Controller
public class ScoreController {
    private final static Pattern SCORE_PATTERN = Pattern.compile(Match.SCORE_PATTERN);
    @Resource
    private Environment environment;
    @Resource
    private MatchDAO matchDAO;
    @Resource
    private RequestParser requestParser;

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