package org.squashleague.web.controller.round;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableSortedMap;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.squashleague.domain.league.Division;
import org.squashleague.domain.league.Match;
import org.squashleague.domain.league.Player;
import org.squashleague.domain.league.Round;

import java.util.*;

public class NewDivisionService {
    static final int MAX_DIVISIONS = 7;

    @VisibleForTesting
    Map<Long, Double> sortPlayersByScore(Map<Long, Player> players, List<Match> matches) {
        final Map<Long, Double> matchScores = new TreeMap<>();
        for (Match match : matches) {
            incrementMap(matchScores, match.getPlayerOne().getId(), match.getPlayerOnePoints());
            incrementMap(matchScores, match.getPlayerTwo().getId(), match.getPlayerTwoPoints());
        }
        // add player who did not have scores entered for any of their matches
        for (Long playerId : players.keySet()) {
            if (!matchScores.containsKey(playerId)) {
                matchScores.put(playerId, 0.0);
            }
        }
        // remove players who are no longer active
        for (Long playerId : matchScores.keySet()) {
            if (!players.containsKey(playerId)) {
                matchScores.remove(playerId);
            }
        }

        return ImmutableSortedMap.copyOf(matchScores, new Comparator<Long>() {
            public int compare(Long a, Long b) {
                if (matchScores.get(a) > matchScores.get(b)) {
                    return -1;
                } else {
                    return 1;
                }
            }
        });
    }

    private void incrementMap(Map<Long, Double> map, Long id, Double value) {
        if (map.containsKey(id)) {
            map.put(id, map.get(id) + value);
        } else {
            map.put(id, value);
        }
    }

    public List<Division> allocateDivisions(Map<Long, Player> players, List<Match> matches, Round round) {
        DivisionSize divisionSize = calculationDivisionSizeCharacteristics(players.size());
        List<Long> sortedPlayerIds = new ArrayList<>(sortPlayersByScore(players, matches).keySet());
        return allocationDivisions(players, round, divisionSize, sortedPlayerIds);
    }

    @VisibleForTesting
    List<Division> allocationDivisions(Map<Long, Player> players, Round round, DivisionSize divisionSizeAllocations, List<Long> sortedPlayerIds) {
        List<Division> divisions = new ArrayList<>();
        int divisionCounter = 0;
        int totalPlayerCounter = 0;
        for (; divisionCounter < (divisionSizeAllocations.noOfFullSizeDivisions + divisionSizeAllocations.noOfSmallerDivisions); divisionCounter++) {
            Division division = new Division().withName(divisionCounter + 1).withRound(round);
            int maxDivisionPlayer =  totalPlayerCounter + (divisionSizeAllocations.divisionSize - (divisionCounter < divisionSizeAllocations.noOfFullSizeDivisions ? 0 : 1));
            for (int divisionPlayerCounter = totalPlayerCounter; divisionPlayerCounter < maxDivisionPlayer; divisionPlayerCounter++, totalPlayerCounter++) {
                players.get(sortedPlayerIds.get(divisionPlayerCounter)).setCurrentDivision(division);
            }
            divisions.add(division);
        }
        return divisions;
    }

    @VisibleForTesting
    List<Match> createMatches(List<Player> players, Division division) {
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

    @VisibleForTesting
    DivisionSize calculationDivisionSizeCharacteristics(int sum) {
        DivisionSize divisionSizeValues = calculateDivisionSize(sum, new int[]{8, 7, 6});
        if (divisionSizeValues == null) {
            divisionSizeValues = calculateDivisionSize(sum, new int[]{9});
        }
        if (divisionSizeValues == null) {
            divisionSizeValues = calculateDivisionSize(sum, new int[]{5});
        }
        if (divisionSizeValues == null) {
            divisionSizeValues = calculateDivisionSize(sum, new int[]{10});
        }
        return divisionSizeValues;
    }

    DivisionSize calculateDivisionSize(int sum, int[] allowedSizes) {
        for (int size : allowedSizes) {
            for (int smallerDivisions = 0; smallerDivisions <= MAX_DIVISIONS; smallerDivisions++) {
                for (int fullSizeDivisions = 1; fullSizeDivisions <= (MAX_DIVISIONS - smallerDivisions); fullSizeDivisions++) {
                    if (fullSizeDivisions * size + smallerDivisions * (size - 1) == sum) {
                        return new DivisionSize(size, fullSizeDivisions, smallerDivisions);
                    }
                }
            }
        }
        return null;
    }

    static class DivisionSize {
        public final int divisionSize;
        public final int noOfFullSizeDivisions;
        public final int noOfSmallerDivisions;

        public DivisionSize(int divisionSize, int noOfFullSizeDivisions, int noOfSmallerDivisions) {
            this.divisionSize = divisionSize;
            this.noOfFullSizeDivisions = noOfFullSizeDivisions;
            this.noOfSmallerDivisions = noOfSmallerDivisions;
        }

        @Override
        public boolean equals(Object other) {
            return EqualsBuilder.reflectionEquals(this, other);
        }

        @Override
        public int hashCode() {
            return HashCodeBuilder.reflectionHashCode(this);
        }

        @Override
        public String toString() {
            return ReflectionToStringBuilder.toString(this);
        }
    }
}