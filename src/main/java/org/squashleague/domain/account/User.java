package org.squashleague.domain.account;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.squashleague.domain.ModelObject;
import org.squashleague.domain.league.Player;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

/**
 * @author squashleague
 */
@Entity
public class User extends ModelObject {

    @NotNull(message = "{user.name}")
    @Size(min = 3, max = 25, message = "{user.name}")
    private String name;
    @NotNull(message = "{user.email}")
    @Email(message = "{user.email}")
    @NotEmpty(message = "{user.email}")
    private String email;
    @Pattern(regexp = "[\\d\\s]{6,15}", message = "{user.mobile}")
    private String mobile;
    @NotNull(message = "{user.mobilePrivate}")
    private MobilePrivacy mobilePrivate;
    @OneToMany(cascade = CascadeType.ALL)
    private Set<Player> players;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User withName(String name) {
        setName(name);
        return this;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public User withEmail(String email) {
        setEmail(email);
        return this;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public User withMobile(String mobile) {
        setMobile(mobile);
        return this;
    }

    public MobilePrivacy getMobilePrivate() {
        return mobilePrivate;
    }

    public void setMobilePrivate(MobilePrivacy mobilePrivate) {
        this.mobilePrivate = mobilePrivate;
    }

    public User withMobilePrivate(MobilePrivacy mobilePrivate) {
        setMobilePrivate(mobilePrivate);
        return this;
    }

    public Set<Player> getPlayers() {
        return players;
    }

    public void setPlayers(Set<Player> players) {
        this.players = players;
    }

    public User withPlayers(Player... players) {
        this.players = new HashSet<>();
        for (Player player : players) {
            this.players.add(player.withUser(this));
        }
        return this;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toStringExclude(this, "logger", "players");
    }

    @Override
    public boolean equals(Object other) {
        return EqualsBuilder.reflectionEquals(this, other, "players");
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this, "players");
    }
}
