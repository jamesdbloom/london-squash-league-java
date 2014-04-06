package org.squashleague.domain.league;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.squashleague.domain.ModelObject;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.*;

@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Division extends ModelObject<Division> implements Comparable<Division> {

    @NotNull(message = "{validation.division.name}")
    @Min(value = 1, message = "{validation.division.name}")
    private Integer name;
    @NotNull(message = "{validation.division.round}")
    @ManyToOne
    private Round round;
    @Transient
    private transient Map<Long, Match> matches = new HashMap<>();
    @Transient
    private transient Map<Long, Map<Long, Match>> matchGrid = new HashMap<>();
    @Transient
    private transient Map<Long, Player> players = new LinkedHashMap<>();

    public Integer getName() {
        return name;
    }

    public void setName(Integer name) {
        this.name = name;
    }

    public Division withName(Integer name) {
        setName(name);
        return this;
    }

    public Round getRound() {
        return round;
    }

    public void setRound(Round league) {
        this.round = league;
    }

    public Division withRound(Round league) {
        setRound(league);
        return this;
    }

    public Division addMatches(Match... matches) {
        for (Match match : matches) {
            Player playerOne = match.getPlayerOne();
            Player playerTwo = match.getPlayerTwo();
            this.matches.put(match.getId(), match);
            players.put(playerOne.getId(), playerOne);
            players.put(playerTwo.getId(), playerTwo);
            if (!matchGrid.containsKey(playerOne.getId())) {
                matchGrid.put(playerOne.getId(), new HashMap<Long, Match>());
            }
            matchGrid.get(playerOne.getId()).put(playerTwo.getId(), match);
        }
        return this;
    }

    public Collection<Match> getMatches() {
        List<Match> matches = new ArrayList<>(this.matches.values());
        Collections.sort(matches);
        return matches;
    }

    public Match getMatch(Long playerOneId, Long playerTwoId) {
        if (matchGrid.containsKey(playerOneId)) {
            Map<Long, Match> matchColumn = matchGrid.get(playerOneId);
            if (matchColumn.containsKey(playerTwoId)) {
                return matchColumn.get(playerTwoId);
            }
        }
        return null;
    }

    public List<Player> getPlayers() {
        return new ArrayList<>(players.values());
    }

    @Override
    public int compareTo(Division other) {
        int roundComparison = round.compareTo(other.round);
        return (roundComparison == 0 ? name.compareTo(other.name) : roundComparison);
    }

    public Division merge(Division division) {
        if (division.name != null) {
            this.name = division.name;
        }
        if (division.round != null) {
            this.round = division.round;
        }
        return this;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toStringExclude(this, "logger", "matches", "matchGrid", "players");
    }

    @Override
    public boolean equals(Object other) {
        return EqualsBuilder.reflectionEquals(this, other, "matches", "matchGrid", "players");
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this, "matches", "matchGrid", "players");
    }
}
