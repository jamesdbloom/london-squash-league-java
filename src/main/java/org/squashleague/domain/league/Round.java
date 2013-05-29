package org.squashleague.domain.league;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.springframework.format.annotation.DateTimeFormat;
import org.squashleague.domain.ModelObject;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.util.*;

@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Round extends ModelObject<Round> implements Comparable<Round> {

    @NotNull(message = "{validation.round.startDate}")
    @Future(message = "{validation.round.startDate}")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime startDate;
    @NotNull(message = "{validation.round.endDate}")
    @Future(message = "{validation.round.endDate}")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime endDate;
    @NotNull(message = "{validation.round.division}")
    @ManyToOne
    private Division division;
    @Transient
    private transient Map<Long, Match> matches = new LinkedHashMap<>();
    @Transient
    private transient Map<Long, Map<Long, Match>> matchGrid = new HashMap<>();
    @Transient
    private transient Map<Long, Player> players = new LinkedHashMap<>();

    public DateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(DateTime startDate) {
        this.startDate = startDate;
    }

    public Round withStartDate(DateTime startDate) {
        setStartDate(startDate);
        return this;
    }

    public DateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(DateTime endDate) {
        this.endDate = endDate;
    }

    public Round withEndDate(DateTime endDate) {
        setEndDate(endDate);
        return this;
    }

    public RoundStatus getStatus() {
        RoundStatus status = RoundStatus.UNKNOWN;
        if (startDate != null && endDate != null) {
            DateTime offset = DateTime.now().plusDays(2);
            if (startDate.isAfter(offset)) {
                status = RoundStatus.NOT_STARTED;
            } else if (startDate.isAfterNow() && (startDate.isBefore(offset) || startDate.isEqual(offset))) {
                status = RoundStatus.STARTING_SOON;
            } else if ((startDate.isBeforeNow() || startDate.isEqualNow()) && (endDate.isAfterNow() || endDate.isEqualNow())) {
                status = RoundStatus.INPLAY;
            } else if (endDate.isBeforeNow()) {
                status = RoundStatus.FINISHED;
            }
        }
        return status;
    }

    public Division getDivision() {
        return division;
    }

    public void setDivision(Division division) {
        this.division = division;
    }

    public Round withDivision(Division division) {
        setDivision(division);
        return this;
    }

    public Round addMatch(Match match) {
        Player playerOne = match.getPlayerOne();
        Player playerTwo = match.getPlayerTwo();
        matches.put(match.getId(), match);
        players.put(playerOne.getId(), playerOne);
        players.put(playerTwo.getId(), playerTwo);
        if (!matchGrid.containsKey(playerOne.getId())) {
            matchGrid.put(playerOne.getId(), new HashMap<Long, Match>());
        }
        matchGrid.get(playerOne.getId()).put(playerTwo.getId(), match);
        return this;
    }

    public List<Match> getMatches() {
        return new ArrayList<>(matches.values());
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
    public int compareTo(Round other) {
        return (division.compareTo(other.division) == 0 ? startDate.compareTo(other.startDate) : division.compareTo(other.division));
    }

    public Round merge(Round round) {
        if (round.startDate != null) {
            this.startDate = round.startDate;
        }
        if (round.endDate != null) {
            this.endDate = round.endDate;
        }
        if (round.division != null) {
            this.division = round.division;
        }
        if (round.matches != null) {
            this.matches = round.matches;
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
        return HashCodeBuilder.reflectionHashCode(this, "matches", "matches", "matchGrid", "players");
    }
}
