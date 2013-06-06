package org.squashleague.web.controller.round;

import com.google.common.collect.Maps;
import org.joda.time.DateTime;
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
import org.squashleague.domain.ModelObject;
import org.squashleague.domain.league.Division;
import org.squashleague.domain.league.League;
import org.squashleague.domain.league.Player;
import org.squashleague.domain.league.Round;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author jamesdbloom
 */
@Controller
@RequestMapping("/leagueRounds")
public class LeagueRoundsController {

    @Resource
    private LeagueDAO leagueDAO;
    @Resource
    private DivisionDAO divisionDAO;
    @Resource
    private RoundDAO roundDAO;
    @Resource
    private PlayerDAO playerDAO;
    @Resource
    private Environment environment;

    @RequestMapping(method = RequestMethod.GET)
    public String list(Model uiModel) {
        uiModel.addAttribute("environment", environment);

        Map<Long, League> leagues = Maps.uniqueIndex(leagueDAO.findAll(), ModelObject.TO_MAP);
        Map<Long, Division> divisions = Maps.uniqueIndex(divisionDAO.findAll(), ModelObject.TO_MAP);
        List<Round> rounds = roundDAO.findAll();

        for (Round round : rounds) {
            divisions.get(round.getDivision().getId()).addRound(round);
        }
        for (Division division : divisions.values()) {
            leagues.get(division.getLeague().getId()).addDivision(division);
        }

        List<League> values = new ArrayList<>(leagues.values());
        Collections.sort(values);
        uiModel.addAttribute("leagues", values);

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
    @RequestMapping(value = "/update/{roundId}", method = RequestMethod.GET)
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
        for (Division division : round.getDivision().getLeague().getDivisions()) {
            for (Round divisionRound : division.getRounds()) {
                // todo fix to only change rounds with identical dates
                roundDAO.update(divisionRound.withStartDate(startDate).withEndDate(endDate));
            }
        }
        return "redirect:/leagueRounds";
    }

}
