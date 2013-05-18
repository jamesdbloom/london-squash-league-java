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
public class League extends ModelObject {

    @NotNull(message = "{league.name}")
    @Size(min = 5, max = 25, message = "{league.name}")
    private String name;
    @NotNull(message = "{league.club}")
    @ManyToOne
    private Club club;
    @OneToMany(cascade = CascadeType.ALL)
    private Set<Division> divisions;

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

    public Set<Division> getDivisions() {
        return divisions;
    }

    public void setDivisions(Set<Division> divisions) {
        this.divisions = divisions;
    }

    public League withDivisions(Division... divisions) {
        this.divisions = new HashSet<>();
        for (Division division : divisions) {
            this.divisions.add(division.withLeague(this));
        }
        return this;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toStringExclude(this, "logger", "divisions");
    }

    @Override
    public boolean equals(Object other) {
        return EqualsBuilder.reflectionEquals(this, other, "divisions");
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this, "divisions");
    }
}
