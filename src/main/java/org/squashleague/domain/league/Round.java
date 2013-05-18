package org.squashleague.domain.league;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.springframework.format.annotation.DateTimeFormat;
import org.squashleague.domain.ModelObject;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Round extends ModelObject {

    @NotNull(message = "{round.startDate}")
    @Future(message = "{round.startDate}")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime startDate;
    @NotNull(message = "{round.endDate}")
    @Future(message = "{round.endDate}")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime endDate;
    @NotNull
    private RoundStatus status = RoundStatus.UNKNOWN;
    @NotNull(message = "{round.division}")
    @ManyToOne
    private Division division;
    @OneToMany(cascade = CascadeType.ALL)
    private Set<Match> matches;

    public DateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(DateTime startDate) {
        this.startDate = startDate;
        updateStatus();
    }

    private void updateStatus() {
        if (startDate != null && endDate != null) {
            DateTime offset = DateTime.now().plusDays(2);
            if (startDate.isAfter(offset)) {
                status = RoundStatus.NOT_STARTED;
            } else if (startDate.isAfterNow() && startDate.isBefore(offset)) {
                status = RoundStatus.STARTING_SOON;
            } else if (startDate.isBeforeNow() && endDate.isAfterNow()) {
                status = RoundStatus.INPLAY;
            } else if (endDate.isBeforeNow()) {
                status = RoundStatus.FINISHED;
            }
        }
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
        return status;
    }

    public void setStatus(RoundStatus roundStatus) {
        this.status = roundStatus;
    }

    public Round withRoundStatus(RoundStatus roundStatus) {
        setStatus(roundStatus);
        return this;
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

    public Set<Match> getMatches() {
        return matches;
    }

    public void setMatches(Set<Match> matches) {
        this.matches = matches;
    }

    public Round withMatches(Match... matches) {
        this.matches = new HashSet<>();
        for (Match match : matches) {
            this.matches.add(match.withRound(this));
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
