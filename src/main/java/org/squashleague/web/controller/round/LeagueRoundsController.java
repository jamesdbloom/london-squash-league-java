package org.squashleague.web.controller.round;

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
import org.squashleague.dao.league.LeagueDAO;
import org.squashleague.dao.league.PlayerDAO;
import org.squashleague.dao.league.RoundDAO;
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
    protected Logger logger = LoggerFactory.getLogger(this.getClass());
    @Resource
    private LeagueDAO leagueDAO;
    @Resource
    private RoundDAO roundDAO;
    @Resource
    private PlayerDAO playerDAO;
    @Resource
    private Environment environment;

    @RequestMapping(method = RequestMethod.GET)
    public String list(Model uiModel) {
        uiModel.addAttribute("environment", environment);

        Map<String, Round> rounds = new LinkedHashMap<>();
        for (Round round : roundDAO.findAll()) {
            rounds.put(round.getDivision().getLeague().getId().toString() + round.getStartDate() + round.getEndDate(), round);
        }

        List<Round> values = new ArrayList<>(rounds.values());
        Collections.sort(values, new Comparator<Round>() {
            @Override
            public int compare(Round roundOne, Round roundTwo) {
                int leagueComparison = roundOne.getDivision().getLeague().compareTo(roundTwo.getDivision().getLeague());
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
        for (Division division : league.getDivisions()) {
            roundDAO.save(new Round().withDivision(division).withStartDate(startDate).withEndDate(endDate));
        }
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
        DateTime startDateToMatch = new DateTime(round.getStartDate());
        DateTime endDateToMatch = new DateTime(round.getEndDate());
        for (Division division : round.getDivision().getLeague().getDivisions()) {
            for (Round divisionRound : division.getRounds()) {
                if (datesMatch(divisionRound, startDateToMatch, endDateToMatch)) {
                    roundDAO.update(divisionRound.withStartDate(startDate).withEndDate(endDate));
                }
            }
        }
        return "redirect:/leagueRounds";
    }

    private boolean datesMatch(Round roundOne, DateTime startDateToMatch, DateTime endDateToMatch) {
        logger.warn("roundOne = " + roundOne.getStartDate() + " - " + roundOne.getEndDate());
        logger.warn("dateToMatch = " + startDateToMatch + " - " + endDateToMatch);
        logger.warn("DATE_ONLY_COMPARATOR = " + (DATE_ONLY_COMPARATOR.compare(roundOne.getStartDate(), startDateToMatch) == 0 && DATE_ONLY_COMPARATOR.compare(roundOne.getEndDate(), endDateToMatch) == 0));
        return DATE_ONLY_COMPARATOR.compare(roundOne.getStartDate(), startDateToMatch) == 0 && DATE_ONLY_COMPARATOR.compare(roundOne.getEndDate(), endDateToMatch) == 0;
    }

}
