package org.squashleague.domain.league;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.squashleague.domain.ModelObject;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Club extends ModelObject {

    @NotNull(message = "{club.name}")
    @Size(min = 5, max = 25, message = "{club.name}")
    private String name;
    @NotNull(message = "{club.address}")
    @Size(min = 5, max = 50, message = "{club.address}")
    private String address;
    @OneToMany(cascade = CascadeType.ALL)
    private Set<League> leagues;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Club withName(String name) {
        setName(name);
        return this;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Club withAddress(String address) {
        setAddress(address);
        return this;
    }

    public Set<League> getLeagues() {
        return leagues;
    }

    public Club withLeagues(League... leagues) {
        this.leagues = new HashSet<>();
        for (League league : leagues) {
            this.leagues.add(league.withClub(this));
        }
        return this;
    }

    public Club withLeagues(Set<League> leagues) {
        this.leagues = leagues;
        return this;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toStringExclude(this, "logger", "leagues");
    }

    @Override
    public boolean equals(Object other) {
        return EqualsBuilder.reflectionEquals(this, other, "leagues");
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this, "leagues");
    }
}
