package org.squashleague.web.controller.round;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.squashleague.domain.ModelObject;
import org.squashleague.domain.league.Division;
import org.squashleague.domain.league.Match;
import org.squashleague.domain.league.Player;
import org.squashleague.domain.league.Round;

import java.util.*;

public class NewDivisionService {
    static final int MAX_DIVISIONS = 7;

    public Map<Player, Double> sortPlayersByScore(Collection<Match> matches) {
        Map<Long, Player> players = new HashMap<>();
        for (Match match : matches) {
            players.put(match.getPlayerOne().getId(), match.getPlayerOne());
            players.put(match.getPlayerTwo().getId(), match.getPlayerTwo());
        }
        return sortPlayersByScore(players, matches);
    }

    @VisibleForTesting
    Map<Player, Double> sortPlayersByScore(Map<Long, Player> players, Collection<Match> matches) {
        final Map<Player, Double> matchScores = new HashMap<>();
        for (Match match : matches) {
            incrementMap(matchScores, match.getPlayerOne(), match.getPlayerOneTotalPoints());
            incrementMap(matchScores, match.getPlayerTwo(), match.getPlayerTwoTotalPoints());
        }
        // add player who did not have scores entered for any of their matches
        for (Long playerId : players.keySet()) {
            if (!matchScores.containsKey(players.get(playerId))) {
                matchScores.put(players.get(playerId), 0.0);
            }
        }
        // remove players who are no longer active
        for (Player playerId : new ArrayList<>(matchScores.keySet())) {
            if (!players.containsKey(playerId.getId())) {
                matchScores.remove(playerId);
            }
        }

        return ImmutableSortedMap.copyOf(matchScores, new Comparator<Player>() {
            public int compare(Player a, Player b) {
                if (matchScores.get(a) > matchScores.get(b)) {
                    return -1;
                } else if (matchScores.get(a).equals(matchScores.get(b)) && a.getUser().getName().compareTo(b.getUser().getName()) <= 0) {
                    return -1;
                } else {
                    return 1;
                }
            }
        });
    }

    private void incrementMap(Map<Player, Double> map, Player id, Double value) {
        if (map.containsKey(id)) {
            map.put(id, map.get(id) + value);
        } else {
            map.put(id, value);
        }
    }

    public List<Division> allocateDivisions(List<Player> players, List<Match> matches, Round round) {
        Map<Long, Player> playersById = Maps.uniqueIndex(players, ModelObject.TO_MAP);
        DivisionSize divisionSize = calculationDivisionSizeCharacteristics(players.size());
        List<Player> sortedPlayers = new ArrayList<>(sortPlayersByScore(playersById, matches).keySet());
        return allocationDivisions(round, divisionSize, sortedPlayers);
    }

    @VisibleForTesting
    List<Division> allocationDivisions(Round round, DivisionSize divisionSizeAllocations, List<Player> sortedPlayers) {
        List<Division> divisions = new ArrayList<>();
        int divisionCounter = 0;
        int totalPlayerCounter = 0;
        for (; divisionCounter < (divisionSizeAllocations.noOfFullSizeDivisions + divisionSizeAllocations.noOfSmallerDivisions); divisionCounter++) {
            Division division = new Division().withName(divisionCounter + 1).withRound(round);
            int maxDivisionPlayer = totalPlayerCounter + (divisionSizeAllocations.divisionSize - (divisionCounter < divisionSizeAllocations.noOfFullSizeDivisions ? 0 : 1));
            for (int divisionPlayerCounter = totalPlayerCounter; divisionPlayerCounter < maxDivisionPlayer; divisionPlayerCounter++, totalPlayerCounter++) {
                sortedPlayers.get(divisionPlayerCounter).setCurrentDivision(division);
            }
            divisions.add(division);
        }
        return divisions;
    }

    public List<Match> createMatches(Collection<Player> players) {
        List<Match> matches = new ArrayList<>();
        Multimap<Division, Player> playersByDivision = ArrayListMultimap.create();
        for (Player player : players) {
            playersByDivision.put(player.getCurrentDivision(), player);
        }
        for (Division division : playersByDivision.keySet()) {
            matches.addAll(createMatches(playersByDivision.get(division), division));
        }
        return matches;
    }

    @VisibleForTesting
    List<Match> createMatches(Collection<Player> players, Division division) {
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
        if (sum <= 5) {
            return new DivisionSize(sum, 1, 0);
        }
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