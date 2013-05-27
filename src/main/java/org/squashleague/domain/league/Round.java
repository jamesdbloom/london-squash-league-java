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
import java.util.List;

@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Round extends ModelObject<Round> {

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
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "round_id")
    private List<Match> matches;

    public DateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(DateTime startDate) {
        this.startDate = startDate;
        updateStatus();
    }

    private void updateStatus() {

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
        updateStatus();
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

    public List<Match> getMatches() {
        return matches;
    }

    public void setMatches(List<Match> matches) {
        this.matches = matches;
    }

    public Round withMatches(List<Match> matches) {
        this.matches = new ArrayList<>();
        for (Match match : matches) {
            this.matches.add(match.withRound(this));
        }
        return this;
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
        return ReflectionToStringBuilder.toStringExclude(this, "logger", "matches");
    }

    @Override
    public boolean equals(Object other) {
        return EqualsBuilder.reflectionEquals(this, other, "matches");
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this, "matches");
    }
}
