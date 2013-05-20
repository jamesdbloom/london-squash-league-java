package org.squashleague.domain.league;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.squashleague.domain.ModelObject;
import org.squashleague.domain.account.User;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Player extends ModelObject {

    @NotNull(message = "{validation.player.user}")
    @ManyToOne
    private User user;
    @NotNull(message = "{validation.player.status}")
    private PlayerStatus status;
    @NotNull(message = "{validation.player.currentDivision}")
    @ManyToOne
    private Division currentDivision;
    @NotNull(message = "{validation.player.league}")
    @ManyToOne
    private League league;
    @OneToMany(cascade = CascadeType.ALL)
    private Set<Match> matches;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Player withUser(User user) {
        setUser(user);
        return this;
    }

    public PlayerStatus getStatus() {
        return status;
    }

    public void setStatus(PlayerStatus playerStatus) {
        this.status = playerStatus;
    }

    public Player withPlayerStatus(PlayerStatus playerStatus) {
        setStatus(playerStatus);
        return this;
    }

    public Division getCurrentDivision() {
        return currentDivision;
    }

    public void setCurrentDivision(Division currentDivision) {
        this.currentDivision = currentDivision;
        this.league = currentDivision.getLeague();
    }

    public Player withCurrentDivision(Division currentDivision) {
        setCurrentDivision(currentDivision);
        return this;
    }

    public Set<Match> getMatches() {
        return matches;
    }

    public void setMatches(Set<Match> matches) {
        this.matches = matches;
    }

    public Player withMatches(Match... matches) {
        this.matches = new HashSet<>();
        Collections.addAll(this.matches, matches);
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
