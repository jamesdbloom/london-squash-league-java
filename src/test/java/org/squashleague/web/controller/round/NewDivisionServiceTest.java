package org.squashleague.web.controller.round;

import org.junit.Test;
import org.squashleague.domain.league.Division;
import org.squashleague.domain.league.Match;
import org.squashleague.domain.league.Player;
import org.squashleague.domain.league.Round;

import java.util.*;

import static org.junit.Assert.assertEquals;

/**
 * @author jamesdbloom
 */
public class NewDivisionServiceTest {

    @Test
    public void shouldSortPlayersByScore() {
        Map<Long, Player> players = new HashMap<>();
        players.put(1l, (Player) new Player().withId(1l)); // always lost  0:1/2 + 0:1/2 + 0:1/2 + 0:1/2 = 4/2 = 2
        players.put(2l, (Player) new Player().withId(2l)); // always won 2:(3 + 0.2)/2 + 3:(3 + 0.3)/2 + 3:(3 + 0.3)/2 = 9.8/2 = 4.9
        players.put(3l, (Player) new Player().withId(3l)); // lost and won 2:(3 + 0.2)/2 2l:(1 + 0.2)/2 = 4.4/2 = 2.2
        players.put(4l, (Player) new Player().withId(4l)); // only played one 2:(3 + 0.2)/2 = 3.2/2 = 1.6
        players.put(5l, (Player) new Player().withId(5l)); // no matches = 0

        Division division = new Division().withName(2);
        List<Match> matches = Arrays.asList(
                new Match().withDivision(division).withPlayerOne(players.get(1l)).withPlayerTwo(players.get(2l)).withScore("0-2"),
                new Match().withDivision(division).withPlayerOne(players.get(2l)).withPlayerTwo(players.get(1l)).withScore("3-0"),
                new Match().withDivision(division).withPlayerOne(players.get(1l)).withPlayerTwo(players.get(3l)).withScore("0-2"),
                new Match().withDivision(division).withPlayerOne(players.get(2l)).withPlayerTwo(players.get(3l)).withScore("3-2"),
                new Match().withDivision(division).withPlayerOne(players.get(1l)).withPlayerTwo(players.get(4l)).withScore("0-2")
        );

        Map<Long, Double> sortedScores = new NewDivisionService().sortPlayersByScore(players, matches);
        assertEquals(Arrays.asList(4.9, 2.2, 2.0, 1.6, 0.0), new ArrayList<>(sortedScores.values()));
        assertEquals(Arrays.asList(2l, 3l, 1l, 4l, 5l), new ArrayList<>(sortedScores.keySet()));
    }

    @Test
    public void shouldAllocatePlayersToDivisions() {
        int numberOfPlayers = 20;

        Map<Long, Player> players = new LinkedHashMap<>();
        for (long p = 0; p < numberOfPlayers; p++) {
            players.put(p, (Player) new Player().withId(p));
        }
        NewDivisionService.DivisionSize divisionSize = new NewDivisionService().calculationDivisionSizeCharacteristics(numberOfPlayers); // 7, 2, 1
        List<Long> sortedPlayerIds = new ArrayList<>(players.keySet());

        Round round = new Round();
        List<Division> divisions = new NewDivisionService().allocationDivisions(players, round, divisionSize, sortedPlayerIds);

        assertEquals(3, divisions.size());
        for (long p = 0; p < numberOfPlayers; p++) {
            System.out.println("player: " + p + " in division: " + players.get(p).getCurrentDivision().getName());
        }

        for (long p = 0; p < 7; p++) {
            int divisionName = 1;
            assertEquals("player: " + p + " in division: " + divisionName, new Integer(divisionName), players.get(p).getCurrentDivision().getName());
        }
        for (long p = 7; p < 14; p++) {
            int divisionName = 2;
            assertEquals("player: " + p + " in division: " + divisionName, new Integer(divisionName), players.get(p).getCurrentDivision().getName());
        }
        for (long p = 14; p < numberOfPlayers; p++) {
            int divisionName = 3;
            assertEquals("player: " + p + " in division: " + divisionName, new Integer(divisionName), players.get(p).getCurrentDivision().getName());
        }
    }

    @Test
    public void shouldAllocatePlayersToDivisionsMultipleSmallerDivisions() {
        int numberOfPlayers = 37;

        Map<Long, Player> players = new LinkedHashMap<>();
        for (long p = 0; p < numberOfPlayers; p++) {
            players.put(p, (Player) new Player().withId(p));
        }
        NewDivisionService.DivisionSize divisionSize = new NewDivisionService().calculationDivisionSizeCharacteristics(numberOfPlayers); // 8, 2, 3
        List<Long> sortedPlayerIds = new ArrayList<>(players.keySet());

        Round round = new Round();
        List<Division> divisions = new NewDivisionService().allocationDivisions(players, round, divisionSize, sortedPlayerIds);

        assertEquals(5, divisions.size());
        for (long p = 0; p < numberOfPlayers; p++) {
            System.out.println("player: " + p + " in division: " + players.get(p).getCurrentDivision().getName());
        }

        for (long p = 0; p < 8; p++) {
            int divisionName = 1;
            assertEquals("player: " + p + " in division: " + divisionName, new Integer(divisionName), players.get(p).getCurrentDivision().getName());
        }
        for (long p = 8; p < 16; p++) {
            int divisionName = 2;
            assertEquals("player: " + p + " in division: " + divisionName, new Integer(divisionName), players.get(p).getCurrentDivision().getName());
        }
        for (long p = 16; p < 23; p++) {
            int divisionName = 3;
            assertEquals("player: " + p + " in division: " + divisionName, new Integer(divisionName), players.get(p).getCurrentDivision().getName());
        }
        for (long p = 23; p < 30; p++) {
            int divisionName = 4;
            assertEquals("player: " + p + " in division: " + divisionName, new Integer(divisionName), players.get(p).getCurrentDivision().getName());
        }
        for (long p = 30; p < numberOfPlayers; p++) {
            int divisionName = 5;
            assertEquals("player: " + p + " in division: " + divisionName, new Integer(divisionName), players.get(p).getCurrentDivision().getName());
        }
    }

    @Test
    public void shouldCalculateDivisionSizes() {
        assertEquals(new NewDivisionService.DivisionSize(5, 1, 0), new NewDivisionService().calculationDivisionSizeCharacteristics(5));
        assertEquals(new NewDivisionService.DivisionSize(6, 1, 0), new NewDivisionService().calculationDivisionSizeCharacteristics(6));
        assertEquals(new NewDivisionService.DivisionSize(7, 1, 0), new NewDivisionService().calculationDivisionSizeCharacteristics(7));
        assertEquals(new NewDivisionService.DivisionSize(8, 1, 0), new NewDivisionService().calculationDivisionSizeCharacteristics(8));
        assertEquals(new NewDivisionService.DivisionSize(9, 1, 0), new NewDivisionService().calculationDivisionSizeCharacteristics(9));
        assertEquals(new NewDivisionService.DivisionSize(5, 2, 0), new NewDivisionService().calculationDivisionSizeCharacteristics(10));
        assertEquals(new NewDivisionService.DivisionSize(6, 1, 1), new NewDivisionService().calculationDivisionSizeCharacteristics(11));
        assertEquals(new NewDivisionService.DivisionSize(6, 2, 0), new NewDivisionService().calculationDivisionSizeCharacteristics(12));
        assertEquals(new NewDivisionService.DivisionSize(7, 1, 1), new NewDivisionService().calculationDivisionSizeCharacteristics(13));
        assertEquals(new NewDivisionService.DivisionSize(7, 2, 0), new NewDivisionService().calculationDivisionSizeCharacteristics(14));
        assertEquals(new NewDivisionService.DivisionSize(8, 1, 1), new NewDivisionService().calculationDivisionSizeCharacteristics(15));
        assertEquals(new NewDivisionService.DivisionSize(8, 2, 0), new NewDivisionService().calculationDivisionSizeCharacteristics(16));
        assertEquals(new NewDivisionService.DivisionSize(6, 2, 1), new NewDivisionService().calculationDivisionSizeCharacteristics(17));
        assertEquals(new NewDivisionService.DivisionSize(6, 3, 0), new NewDivisionService().calculationDivisionSizeCharacteristics(18));
        assertEquals(new NewDivisionService.DivisionSize(7, 1, 2), new NewDivisionService().calculationDivisionSizeCharacteristics(19));
        assertEquals(new NewDivisionService.DivisionSize(7, 2, 1), new NewDivisionService().calculationDivisionSizeCharacteristics(20));
        assertEquals(new NewDivisionService.DivisionSize(7, 3, 0), new NewDivisionService().calculationDivisionSizeCharacteristics(21));
        assertEquals(new NewDivisionService.DivisionSize(8, 1, 2), new NewDivisionService().calculationDivisionSizeCharacteristics(22));
        assertEquals(new NewDivisionService.DivisionSize(8, 2, 1), new NewDivisionService().calculationDivisionSizeCharacteristics(23));
        assertEquals(new NewDivisionService.DivisionSize(8, 3, 0), new NewDivisionService().calculationDivisionSizeCharacteristics(24));
        assertEquals(new NewDivisionService.DivisionSize(7, 1, 3), new NewDivisionService().calculationDivisionSizeCharacteristics(25));
        assertEquals(new NewDivisionService.DivisionSize(7, 2, 2), new NewDivisionService().calculationDivisionSizeCharacteristics(26));
        assertEquals(new NewDivisionService.DivisionSize(7, 3, 1), new NewDivisionService().calculationDivisionSizeCharacteristics(27));
        assertEquals(new NewDivisionService.DivisionSize(7, 4, 0), new NewDivisionService().calculationDivisionSizeCharacteristics(28));
        assertEquals(new NewDivisionService.DivisionSize(8, 1, 3), new NewDivisionService().calculationDivisionSizeCharacteristics(29));
        assertEquals(new NewDivisionService.DivisionSize(8, 2, 2), new NewDivisionService().calculationDivisionSizeCharacteristics(30));
        assertEquals(new NewDivisionService.DivisionSize(8, 3, 1), new NewDivisionService().calculationDivisionSizeCharacteristics(31));
        assertEquals(new NewDivisionService.DivisionSize(8, 4, 0), new NewDivisionService().calculationDivisionSizeCharacteristics(32));
        assertEquals(new NewDivisionService.DivisionSize(7, 3, 2), new NewDivisionService().calculationDivisionSizeCharacteristics(33));
        assertEquals(new NewDivisionService.DivisionSize(7, 4, 1), new NewDivisionService().calculationDivisionSizeCharacteristics(34));
        assertEquals(new NewDivisionService.DivisionSize(7, 5, 0), new NewDivisionService().calculationDivisionSizeCharacteristics(35));
        assertEquals(new NewDivisionService.DivisionSize(8, 1, 4), new NewDivisionService().calculationDivisionSizeCharacteristics(36));
        assertEquals(new NewDivisionService.DivisionSize(8, 2, 3), new NewDivisionService().calculationDivisionSizeCharacteristics(37));
        assertEquals(new NewDivisionService.DivisionSize(8, 3, 2), new NewDivisionService().calculationDivisionSizeCharacteristics(38));
        assertEquals(new NewDivisionService.DivisionSize(8, 4, 1), new NewDivisionService().calculationDivisionSizeCharacteristics(39));
        assertEquals(new NewDivisionService.DivisionSize(8, 5, 0), new NewDivisionService().calculationDivisionSizeCharacteristics(40));
        assertEquals(new NewDivisionService.DivisionSize(7, 5, 1), new NewDivisionService().calculationDivisionSizeCharacteristics(41));
        assertEquals(new NewDivisionService.DivisionSize(7, 6, 0), new NewDivisionService().calculationDivisionSizeCharacteristics(42));
        assertEquals(new NewDivisionService.DivisionSize(8, 1, 5), new NewDivisionService().calculationDivisionSizeCharacteristics(43));
        assertEquals(new NewDivisionService.DivisionSize(8, 2, 4), new NewDivisionService().calculationDivisionSizeCharacteristics(44));
        assertEquals(new NewDivisionService.DivisionSize(8, 3, 3), new NewDivisionService().calculationDivisionSizeCharacteristics(45));
        assertEquals(new NewDivisionService.DivisionSize(8, 4, 2), new NewDivisionService().calculationDivisionSizeCharacteristics(46));
        assertEquals(new NewDivisionService.DivisionSize(8, 5, 1), new NewDivisionService().calculationDivisionSizeCharacteristics(47));
        assertEquals(new NewDivisionService.DivisionSize(8, 6, 0), new NewDivisionService().calculationDivisionSizeCharacteristics(48));
        assertEquals(new NewDivisionService.DivisionSize(7, 7, 0), new NewDivisionService().calculationDivisionSizeCharacteristics(49));
    }
}
