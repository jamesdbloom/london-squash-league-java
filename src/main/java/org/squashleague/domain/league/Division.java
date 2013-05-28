package org.squashleague.domain.league;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.squashleague.domain.ModelObject;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Division extends ModelObject<Division> implements Comparable<Division> {

    @NotNull(message = "{validation.division.name}")
    @Size(min = 5, max = 25, message = "{validation.division.name}")
    private String name;
    @NotNull(message = "{validation.club.name}")
    @ManyToOne
    private League league;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "division_id")
    private List<Round> rounds;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Division withName(String name) {
        setName(name);
        return this;
    }

    public League getLeague() {
        return league;
    }

    public void setLeague(League league) {
        this.league = league;
    }

    public Division withLeague(League league) {
        setLeague(league);
        return this;
    }

    public List<Round> getRounds() {
        return rounds;
    }

    public void setRounds(List<Round> rounds) {
        this.rounds = rounds;
    }

    public Division withRounds(Round... rounds) {
        this.rounds = new ArrayList<>();
        for (Round round : rounds) {
            this.rounds.add(round.withDivision(this));
        }
        return this;
    }

    public List<Round> getCurrentRounds() {
        List<Round> rounds = new ArrayList<>();
        for (Round round : this.rounds) {
            if (round.getStatus().equals(RoundStatus.INPLAY)) {
                rounds.add(round);
            }
        }
        return rounds;
    }


    @Override
    public int compareTo(Division other) {
        return (league.compareTo(other.league) == 0 ? name.compareTo(other.name) : league.compareTo(other.league));
    }

    public Division merge(Division division) {
        if (division.name != null) {
            this.name = division.name;
        }
        if (division.league != null) {
            this.league = division.league;
        }
        if (division.rounds != null) {
            this.rounds = division.rounds;
        }
        return this;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toStringExclude(this, "logger", "rounds");
    }

    @Override
    public boolean equals(Object other) {
        return EqualsBuilder.reflectionEquals(this, other, "rounds");
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this, "rounds");
    }
}
