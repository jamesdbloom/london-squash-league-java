package org.squashleague.dao.league;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.squashleague.domain.league.Round;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author jamesdbloom
 */
@RunWith(MockitoJUnitRunner.class)
public class MatchDAOTest {

    @Test
    public void shouldDeterminePreviousRoundIdsListWhenMultiplePreviousRounds() {
        List<Long> roundIds = new MatchDAO().getPreviousRoundIds(
                ((Round) new Round()
                        .withId(1l))
                        .withPreviousRound(
                                ((Round) new Round()
                                        .withId(2l))
                                        .withPreviousRound(
                                                (Round) new Round().withId(3l)
                                        )
                        ),
                2
        );

        assertEquals(Arrays.asList(1l, 2l, 3l), roundIds);
    }

    @Test
    public void shouldDeterminePreviousRoundIdsListWhenMultiplePreviousRoundsButRequestingLess() {
        List<Long> roundIds = new MatchDAO().getPreviousRoundIds(
                ((Round) new Round()
                        .withId(1l))
                        .withPreviousRound(
                                ((Round) new Round()
                                        .withId(2l))
                                        .withPreviousRound(
                                                (Round) new Round().withId(3l)
                                        )
                        ),
                1
        );

        assertEquals(Arrays.asList(1l, 2l), roundIds);
    }

    @Test
    public void shouldDeterminePreviousRoundIdsListWhenMultiplePreviousRoundsButRequestingMore() {
        List<Long> roundIds = new MatchDAO().getPreviousRoundIds(
                ((Round) new Round()
                        .withId(1l))
                        .withPreviousRound(
                                ((Round) new Round()
                                        .withId(2l))
                                        .withPreviousRound(
                                                (Round) new Round().withId(3l)
                                        )
                        ),
                10
        );

        assertEquals(Arrays.asList(1l, 2l, 3l), roundIds);
    }

    @Test
    public void shouldDeterminePreviousRoundIdsListWhenOnePreviousRound() {
        List<Long> roundIds = new MatchDAO().getPreviousRoundIds(
                ((Round) new Round()
                        .withId(1l))
                        .withPreviousRound(
                                ((Round) new Round()
                                        .withId(2l))
                        ),
                1
        );

        assertEquals(Arrays.asList(1l, 2l), roundIds);
    }

    @Test
    public void shouldDeterminePreviousRoundIdsListWhenNoPreviousRound() {
        List<Long> roundIds = new MatchDAO().getPreviousRoundIds(
                (Round) new Round().withId(1l),
                0
        );

        assertEquals(Arrays.asList(1l), roundIds);
    }

}
