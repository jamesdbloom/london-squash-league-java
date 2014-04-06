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

import javax.persistence.*;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Round extends ModelObject<Round> implements Comparable<Round> {

    @NotNull(message = "{validation.round.startDate}")
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
    private League league;
    @OneToOne
    private Round previousRound;
    @Transient
    private Collection<Division> divisions;
    @Transient
    private Map<Player, Double> playerSortedByScore;

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

    public boolean notStarted() {
        RoundStatus status = getStatus();
        return status != RoundStatus.INPLAY && status != RoundStatus.FINISHED;
    }

    public boolean inPlay() {
        RoundStatus status = getStatus();
        return status == RoundStatus.INPLAY;
    }

    public League getLeague() {
        return league;
    }

    public void setLeague(League league) {
        this.league = league;
    }

    public Round getPreviousRound() {
        return previousRound;
    }

    public void setPreviousRound(Round previousRound) {
        this.previousRound = previousRound;
    }

    public Round withPreviousRound(Round previousRound) {
        setPreviousRound(previousRound);
        return this;
    }

    public Round withLeague(League division) {
        setLeague(division);
        return this;
    }

    public Collection<Division> getDivisions() {
        return divisions;
    }

    public void setDivisions(Collection<Division> divisions) {
        this.divisions = divisions;
    }

    public Round withDivisions(Collection<Division> divisions) {
        setDivisions(divisions);
        return this;
    }

    public Round addDivision(Division division) {
        if (divisions == null) {
            divisions = new ArrayList<>();
        }
        divisions.add(division);
        return this;
    }

    public Map<Player, Double> getPlayerSortedByScore() {
        return playerSortedByScore;
    }

    public void setPlayerSortedByScore(Map<Player, Double> playerSortedByScore) {
        this.playerSortedByScore = playerSortedByScore;
    }

    @Override
    public int compareTo(Round other) {
        int leagueComparison = league.compareTo(other.league);
        int startDateComparison = other.startDate.compareTo(startDate);
        int endDateComparison = other.endDate.compareTo(endDate);
        return (leagueComparison == 0 ? (startDateComparison == 0 ? endDateComparison : startDateComparison) : leagueComparison);
    }

    public Round merge(Round round) {
        if (round.startDate != null) {
            this.startDate = round.startDate;
        }
        if (round.endDate != null) {
            this.endDate = round.endDate;
        }
        if (round.league != null) {
            this.league = round.league;
        }
        if (round.previousRound != null) {
            this.previousRound = round.previousRound;
        }
        return this;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toStringExclude(this, "logger");
    }

    @Override
    public boolean equals(Object other) {
        return EqualsBuilder.reflectionEquals(this, other, "logger", "previousRound", "divisions", "playerSortedByScore");
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this, "logger", "previousRound", "divisions", "playerSortedByScore");
    }
}
