package org.squashleague.domain.league;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.squashleague.domain.ModelObject;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Division extends ModelObject {

    @NotNull(message = "{validation.division.name}")
    @Size(min = 5, max = 25,message = "{validation.division.name}")
    private String name;
    @NotNull(message = "{validation.club.name}")
    @ManyToOne
    private League league;
    @OneToMany(cascade = CascadeType.ALL)
    private Set<Round> rounds;

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

    public Set<Round> getRounds() {
        return rounds;
    }

    public void setRounds(Set<Round> rounds) {
        this.rounds = rounds;
    }

    public Division withRounds(Round... rounds) {
        this.rounds = new HashSet<>();
        for (Round round : rounds) {
            this.rounds.add(round.withDivision(this));
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
