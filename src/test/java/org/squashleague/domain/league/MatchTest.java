package org.squashleague.domain.league;

import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MatchTest {

    @Test
    public void shouldMerge() {
        Match existing = new Match()
                .withPlayerOne(new Player().withStatus(PlayerStatus.ACTIVE))
                .withPlayerTwo(new Player().withStatus(PlayerStatus.ACTIVE))
                .withDivision(new Division().withName(1))
                .withScore("1-2");

        Match newVersion = new Match()
                .withPlayerOne(new Player().withStatus(PlayerStatus.INACTIVE))
                .withPlayerTwo(new Player().withStatus(PlayerStatus.INACTIVE))
                .withDivision(new Division().withName(2))
                .withScore("3-4");

        assertEquals(newVersion, existing.merge(newVersion));
        assertEquals(existing, existing.merge(new Match()));
    }

    @Test
    public void shouldCalculatePoints() {
        // divide by division
        Match matchOne = new Match().withDivision(new Division().withName(3).withRound((Round) new Round().withId(3l))).withScore("5-2");
        assertEquals(new Double(1.17), matchOne.getPlayerOneTotalPoints()); // (3 + .5) / 3.0  = 1.1666666666666667
        assertEquals(new Double(0.4), matchOne.getPlayerTwoTotalPoints()); // (1 + .2) / 3.0  = 0.39999999999999997

        Match matchTwo = new Match().withDivision(new Division().withName(1).withRound((Round) new Round().withId(3l))).withScore("100-200");
        assertEquals(new Double(11.0), matchTwo.getPlayerOneTotalPoints()); // (1 + 10) / 1.0 = 11.0
        assertEquals(new Double(23.0), matchTwo.getPlayerTwoTotalPoints()); // (3 + 20) / 1.0 = 23.0

        Match matchThree = new Match().withDivision(new Division().withName(5).withRound((Round) new Round().withId(3l))).withScore("1-0");
        assertEquals(new Double(0.62), matchThree.getPlayerOneTotalPoints()); // (3 + 0.1) / 5.0 = 0.62
        assertEquals(new Double(0.2), matchThree.getPlayerTwoTotalPoints()); // (1 + 0.0) / 5.0 = 0.2

        Match matchFour = new Match().withDivision(new Division().withName(5).withRound((Round) new Round().withId(3l))).withScore("1-1");
        assertEquals(new Double(0.42), matchFour.getPlayerOneTotalPoints()); // (2 + 0.1) / 5.0 = 0.42000000000000004
        assertEquals(new Double(0.42), matchFour.getPlayerTwoTotalPoints()); // (2 + 0.1) / 5.0 = 0.42000000000000004

        // DO NOT divide by division
        Match matchFive = new Match().withDivision(new Division().withName(3).withRound((Round) new Round().withId(1l))).withScore("5-2");
        assertEquals(new Double(3.5), matchFive.getPlayerOneTotalPoints()); // (3 + .5) / 1.0  = 3.5
        assertEquals(new Double(1.2), matchFive.getPlayerTwoTotalPoints()); // (1 + .2) / 1.0  = 1.2

        Match matchSix = new Match().withDivision(new Division().withName(1).withRound((Round) new Round().withId(2l))).withScore("100-200");
        assertEquals(new Double(11.0), matchSix.getPlayerOneTotalPoints()); // (1 + 10) / 1.0 = 11.0
        assertEquals(new Double(23.0), matchSix.getPlayerTwoTotalPoints()); // (3 + 20) / 1.0 = 23.0

        Match matchSeven = new Match().withDivision(new Division().withName(5).withRound((Round) new Round().withId(1l))).withScore("1-0");
        assertEquals(new Double(3.1), matchSeven.getPlayerOneTotalPoints()); // (3 + 0.1) / 1.0 = 3.1
        assertEquals(new Double(1.0), matchSeven.getPlayerTwoTotalPoints()); // (1 + 0.0) / 1.0 = 1.0

        Match matchEight = new Match().withDivision(new Division().withName(5).withRound((Round) new Round().withId(2l))).withScore("1-1");
        assertEquals(new Double(2.1), matchEight.getPlayerOneTotalPoints()); // (2 + 0.1) / 1.0 = 2.1
        assertEquals(new Double(2.1), matchEight.getPlayerTwoTotalPoints()); // (2 + 0.1) / 1.0 = 2.1
    }
}
