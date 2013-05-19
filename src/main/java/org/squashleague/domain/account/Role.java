package org.squashleague.domain.account;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.squashleague.domain.ModelObject;
import org.squashleague.domain.league.Club;
import org.squashleague.domain.league.Player;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

/**
 * @author jamesdbloom
 */
@Entity
public class Role extends ModelObject {

    public static final Role ROLE_ANONYMOUS = new Role().withName("ROLE_ANONYMOUS").withDescription("Anonymous User Role");
    public static final Role ROLE_USER = new Role().withName("ROLE_USER").withDescription("Authentication User Role");
    public static final Role ROLE_ADMIN = new Role().withName("ROLE_ADMIN").withDescription("Administrator Role");

    @NotNull(message = "{role.name}")
    @Size(min = 5, max = 50, message = "{role.name}")
    private String name;
    @NotNull(message = "{role.description}")
    @Size(min = 5, max = 50, message = "{role.description}")
    private String description;
    @ManyToOne(fetch = FetchType.EAGER)
    private Club club;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Role withName(String name) {
        setName(name);
        return this;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Role withDescription(String description) {
        setDescription(description);
        return this;
    }

    public Club getClub() {
        return club;
    }

    public void setClub(Club club) {
        this.club = club;
    }

    public Role withClub(Club club) {
        setClub(club);
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
