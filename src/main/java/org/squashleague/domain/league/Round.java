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

    public League getLeague() {
        return league;
    }

    public void setLeague(League division) {
        this.league = division;
    }

    public Round withLeague(League division) {
        setLeague(division);
        return this;
    }

    @Override
    public int compareTo(Round other) {
        int divisionComparison = league.compareTo(other.league);
        return (divisionComparison == 0 ? startDate.compareTo(other.startDate) : divisionComparison);
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
        return this;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toStringExclude(this, "logger");
    }

    @Override
    public boolean equals(Object other) {
        return EqualsBuilder.reflectionEquals(this, other);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
}
