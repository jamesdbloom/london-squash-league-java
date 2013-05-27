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
public class Club extends ModelObject<Club> {

    @NotNull(message = "{validation.club.name}")
    @Size(min = 5, max = 25, message = "{validation.club.name}")
    private String name;
    @NotNull(message = "{validation.club.address}")
    @Size(min = 5, max = 50, message = "{validation.club.address}")
    private String address;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "club_id")
    private List<League> leagues;

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

    public List<League> getLeagues() {
        return leagues;
    }

    public void setLeagues(List<League> leagues) {
        this.leagues = leagues;
    }

    public Club withLeagues(League... leagues) {
        this.leagues = new ArrayList<>();
        for (League league : leagues) {
            this.leagues.add(league.withClub(this));
        }
        return this;
    }

    public Club merge(Club club) {
        if (club.name != null) {
            this.name = club.name;
        }
        if (club.address != null) {
            this.address = club.address;
        }
        if (club.leagues != null) {
            this.leagues = club.leagues;
        }
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
