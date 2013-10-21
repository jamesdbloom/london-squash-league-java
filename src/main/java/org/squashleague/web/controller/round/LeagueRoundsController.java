package org.squashleague.web.controller.round;

import com.google.common.base.Function;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
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
import org.squashleague.domain.league.Division;
import org.squashleague.domain.league.League;
import org.squashleague.domain.league.Player;
import org.squashleague.domain.league.Round;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author jamesdbloom
 */
@Controller
@RequestMapping("/leagueRounds")
public class LeagueRoundsController {

    public static final DateTimeComparator DATE_ONLY_COMPARATOR = DateTimeComparator.getDateOnlyInstance();
    private final NewDivisionService divisionSizeService = new NewDivisionService();
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

        Multimap<Long, Division> roundIdToDivision = Multimaps.index(divisionDAO.findAll(), new Function<Division, Long>() {
            @Override
            public Long apply(Division division) {
                return division.getRound().getId();
            }
        });
        Map<String, Round> rounds = new LinkedHashMap<>();
        for (Round round : roundDAO.findAll()) {
            rounds.put(round.getLeague().getId().toString() + round.getStartDate() + round.getEndDate(), round.withDivisions(roundIdToDivision.get(round.getId())));
        }
        List<Round> sortedRounds = new ArrayList<>(rounds.values());
        Collections.sort(sortedRounds, new Comparator<Round>() {
            @Override
            public int compare(Round roundOne, Round roundTwo) {
                int leagueComparison = roundOne.getLeague().compareTo(roundTwo.getLeague());
                int startDate = roundOne.getStartDate().compareTo(roundTwo.getStartDate());
                return (leagueComparison == 0 ? (startDate == 0 ? roundOne.getEndDate().compareTo(roundTwo.getEndDate()) : startDate) : leagueComparison);
            }
        });

        uiModel.addAttribute("rounds", sortedRounds);
        uiModel.addAttribute("leagues", leagueDAO.findAll());

        return "page/round/leagueRounds";
    }

    @Transactional
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String create(Long leagueId,
                         @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) DateTime startDate,
                         @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) DateTime endDate,
                         Long previousRoundId,
                         RedirectAttributes redirectAttributes) {
        League league = leagueDAO.findById(leagueId);
        boolean startDateAfterEndDate = startDate != null && endDate != null && startDate.isAfter(endDate.minusDays(1));
        Round previousRound = roundDAO.findById(previousRoundId);

        List<String> validationErrors = new ArrayList<>();
        if (previousRound != null) {
            if (!previousRound.getLeague().getId().equals(league.getId())) {
                validationErrors.add(environment.getProperty("validation.round.previousRound.sameLeague"));
            }
            if (startDate != null && previousRound.getEndDate().isAfter(startDate.plusDays(1))) {
                validationErrors.add(environment.getProperty("validation.round.previousRound.endDateBeforeStartDate"));
            }
        }
        if (league == null) {
            validationErrors.add(environment.getProperty("validation.round.league"));
        }
        if (startDateAfterEndDate) {
            validationErrors.add(environment.getProperty("validation.round.startDateAfterEndDate"));
        }
        if (!validationErrors.isEmpty()) {
            redirectAttributes.addFlashAttribute("environment", environment);
            redirectAttributes.addFlashAttribute("leagueId", leagueId);
            redirectAttributes.addFlashAttribute("startDate", (startDate != null ? startDate.toString("yyyy-MM-dd") : ""));
            redirectAttributes.addFlashAttribute("endDate", (endDate != null ? endDate.toString("yyyy-MM-dd") : ""));
            redirectAttributes.addFlashAttribute("previousRoundId", previousRoundId);
            redirectAttributes.addFlashAttribute("validationErrors", validationErrors);
            return "redirect:/leagueRounds";
        }

        roundDAO.save(
                new Round()
                        .withLeague(league)
                        .withPreviousRound(previousRound)
                        .withStartDate(startDate)
                        .withEndDate(endDate)
        );
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

    @Transactional
    @RequestMapping(value = "/createMatches", params = "roundId", method = RequestMethod.POST)
    public String createMatches(Long roundId, RedirectAttributes redirectAttributes) {
        Round round = roundDAO.findById(roundId);
        if (round == null) {
            return "redirect:/errors/403";
        }
        List<Division> oldDivisions = divisionDAO.findAllByRound(round);
        if (oldDivisions.size() > 0) {
            redirectAttributes.addFlashAttribute("message", "Round " + round.getLeague().getClub().getName() + " - " + round.getLeague().getName() + " - (" + round.getStartDate().toString("yyyy-MM-dd") + " - " + round.getEndDate().toString("yyyy-MM-dd") + ") already has divisions");
            redirectAttributes.addFlashAttribute("title", "Can't create matches");
            return "redirect:/message";
        }

        List<Player> players = playerDAO.findAllActiveByLeague(round.getLeague());
        divisionDAO.save(divisionSizeService.allocateDivisions(players, matchDAO.findAllInRound(round, 2), round));
        matchDAO.save(divisionSizeService.createMatches(players));
        playerDAO.update(players);

        return "redirect:/leagueRounds";
    }


}
