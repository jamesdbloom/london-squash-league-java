package org.squashleague.web.controller.round;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import org.joda.time.DateTime;
import org.joda.time.DateTimeComparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.squashleague.dao.league.*;
import org.squashleague.domain.league.*;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author jamesdbloom
 */
@Controller
@RequestMapping("/leagueRounds")
public class LeagueRoundsController {

    public static final DateTimeComparator DATE_ONLY_COMPARATOR = DateTimeComparator.getDateOnlyInstance();
    protected Logger logger = LoggerFactory.getLogger(this.getClass());
    @Resource
    private LeagueDAO leagueDAO;
    @Resource
    private RoundDAO roundDAO;
    @Resource
    private DivisionDAO divisionDAO;
    @Resource
    private PlayerDAO playerDAO;
    @Resource
    private MatchDAO matchDAO;
    @Resource
    private Environment environment;

    @RequestMapping(method = RequestMethod.GET)
    public String list(Model uiModel) {
        uiModel.addAttribute("environment", environment);

        Map<String, Round> rounds = new LinkedHashMap<>();
        for (Round round : roundDAO.findAll()) {
            rounds.put(round.getLeague().getId().toString() + round.getStartDate() + round.getEndDate(), round);
        }

        List<Round> values = new ArrayList<>(rounds.values());
        Collections.sort(values, new Comparator<Round>() {
            @Override
            public int compare(Round roundOne, Round roundTwo) {
                int leagueComparison = roundOne.getLeague().compareTo(roundTwo.getLeague());
                int startDate = roundOne.getStartDate().compareTo(roundTwo.getStartDate());
                return (leagueComparison == 0 ? (startDate == 0 ? roundOne.getEndDate().compareTo(roundTwo.getEndDate()) : startDate) : leagueComparison);
            }
        });
        uiModel.addAttribute("rounds", values);
        uiModel.addAttribute("leagues", leagueDAO.findAll());

        return "page/round/leagueRounds";
    }

    @Transactional
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String create(Long leagueId,
                         @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) DateTime startDate,
                         @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) DateTime endDate,
                         RedirectAttributes redirectAttributes) {
        League league = leagueDAO.findById(leagueId);
        boolean startDateAfterEndDate = startDate != null && endDate != null && startDate.isAfter(endDate.minusDays(1));
        if (league == null || startDateAfterEndDate) {
            List<String> validationErrors = new ArrayList<>();
            if (league == null) {
                validationErrors.add(environment.getProperty("validation.round.league"));
            }
            if (startDateAfterEndDate) {
                validationErrors.add(environment.getProperty("validation.round.startDateAfterEndDate"));
            }
            redirectAttributes.addFlashAttribute("environment", environment);
            redirectAttributes.addFlashAttribute("leagueId", leagueId);
            redirectAttributes.addFlashAttribute("startDate", (startDate != null ? startDate.toString("yyyy-MM-dd") : ""));
            redirectAttributes.addFlashAttribute("endDate", (endDate != null ? endDate.toString("yyyy-MM-dd") : ""));
            redirectAttributes.addFlashAttribute("validationErrors", validationErrors);
            return "redirect:/leagueRounds";
        }

        List<Player> players = playerDAO.findAllActiveByLeague(league);
        roundDAO.save(new Round().withLeague(league).withStartDate(startDate).withEndDate(endDate));
        return "redirect:/leagueRounds";
    }

    @Transactional
    @RequestMapping(value = "/update/{roundId:[0-9]+}", method = RequestMethod.GET)
    public String updateForm(@PathVariable("roundId") Long roundId, Model uiModel) {
        Round round = roundDAO.findById(roundId);
        if (round == null) {
            return "redirect:/errors/403";
        }
        uiModel.addAttribute("environment", environment);
        uiModel.addAttribute("round", round);
        uiModel.addAttribute("startDate", (round.getStartDate() != null ? round.getStartDate().toString("yyyy-MM-dd") : ""));
        uiModel.addAttribute("endDate", (round.getEndDate() != null ? round.getEndDate().toString("yyyy-MM-dd") : ""));
        return "page/round/update";
    }

    @Transactional
    @RequestMapping(value = "/createMatches", params = "roundId", method = RequestMethod.POST)
    public String createMatches(Long roundId, Model uiModel) {
        Round round = roundDAO.findById(roundId);
        if (round == null) {
            return "redirect:/errors/403";
        }
        List<Player> players = playerDAO.findAllActiveByLeague(round.getLeague());

        Multimap<Long, ScoreEntry> matchScores = HashMultimap.create();
        for (Match match : matchDAO.findAll()) {
            if (match.getDivision().getRound().getLeague().equals(round.getLeague())) {
                matchScores.put(match.getPlayerOne().getId(), new ScoreEntry(match.getPlayerOne(), match, match.getPlayerOnePoints()));
                matchScores.put(match.getPlayerTwo().getId(), new ScoreEntry(match.getPlayerTwo(), match, match.getPlayerTwoPoints()));
            }
        }

        uiModel.addAttribute("environment", environment);
        uiModel.addAttribute("round", round);
        uiModel.addAttribute("startDate", (round.getStartDate() != null ? round.getStartDate().toString("yyyy-MM-dd") : ""));
        uiModel.addAttribute("endDate", (round.getEndDate() != null ? round.getEndDate().toString("yyyy-MM-dd") : ""));
        return "page/round/update";
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

    @Transactional
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(Long roundId,
                         @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) DateTime startDate,
                         @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) DateTime endDate,
                         Model uiModel) {
        Round round = roundDAO.findById(roundId);
        boolean startDateAfterEndDate = startDate != null && endDate != null && startDate.isAfter(endDate.minusDays(1));
        if (round == null || startDateAfterEndDate) {
            List<String> validationErrors = new ArrayList<>();
            if (round == null) {
                validationErrors.add(environment.getProperty("validation.round.league"));
            }
            if (startDateAfterEndDate) {
                validationErrors.add(environment.getProperty("validation.round.startDateAfterEndDate"));
            }
            uiModel.addAttribute("environment", environment);
            uiModel.addAttribute("round", round);
            uiModel.addAttribute("startDate", (startDate != null ? startDate.toString("yyyy-MM-dd") : ""));
            uiModel.addAttribute("endDate", (endDate != null ? endDate.toString("yyyy-MM-dd") : ""));
            uiModel.addAttribute("endDate", (endDate != null ? endDate.toString("yyyy-MM-dd") : ""));
            uiModel.addAttribute("validationErrors", validationErrors);
            return "page/round/update";
        }
        roundDAO.update(round.withStartDate(startDate).withEndDate(endDate));
        return "redirect:/leagueRounds";
    }

}
