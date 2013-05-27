package org.squashleague.domain.account;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.squashleague.domain.ModelObject;
import org.squashleague.domain.league.Club;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author jamesdbloom
 */
@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Role extends ModelObject<Role> {

    public static final Role ROLE_ANONYMOUS = new Role().withName("ROLE_ANONYMOUS").withDescription("Anonymous User Role");
    public static final Role ROLE_USER = new Role().withName("ROLE_USER").withDescription("Authentication User Role");
    public static final Role ROLE_ADMIN = new Role().withName("ROLE_ADMIN").withDescription("Administrator Role");
    @Column(unique = true)
    @NotNull(message = "{validation.role.name}")
    @Size(min = 5, max = 25, message = "{validation.role.name}")
    private String name;
    @NotNull(message = "{validation.role.description}")
    @Size(min = 5, max = 50, message = "{validation.role.description}")
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
    public Role merge(Role role) {
        if (role.name != null) {
            this.name = role.name;
        }
        if (role.description != null) {
            this.description = role.description;
        }
        if (role.club != null) {
            this.club = role.club;
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
