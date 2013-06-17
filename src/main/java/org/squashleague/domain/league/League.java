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
public class League extends ModelObject<League> implements Comparable<League> {

    @NotNull(message = "{validation.league.name}")
    @Size(min = 5, max = 25, message = "{validation.league.name}")
    private String name;
    @NotNull(message = "{validation.league.club}")
    @ManyToOne
    private Club club;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "league_id")
    private List<Round> rounds;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public League withName(String name) {
        setName(name);
        return this;
    }

    public Club getClub() {
        return club;
    }

    public void setClub(Club club) {
        this.club = club;
    }

    public League withClub(Club club) {
        setClub(club);
        return this;
    }

    public List<Round> getRounds() {
        return rounds;
    }

    public void setRounds(List<Round> rounds) {
        this.rounds = rounds;
    }

    public League withRounds(Round... rounds) {
        this.rounds = new ArrayList<>();
        for (Round round : rounds) {
            this.rounds.add(round.withLeague(this));
        }
        return this;
    }

    @Override
    public int compareTo(League other) {
        int clubComparison = club.compareTo(other.club);
        return (clubComparison == 0 ? name.compareTo(other.name) : clubComparison);
    }

    public League merge(League league) {
        if (league.name != null) {
            this.name = league.name;
        }
        if (league.club != null) {
            this.club = league.club;
        }
        if (league.rounds != null) {
            this.rounds = league.rounds;
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
