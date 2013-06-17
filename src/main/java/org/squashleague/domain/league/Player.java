package org.squashleague.domain.league;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.squashleague.domain.ModelObject;
import org.squashleague.domain.account.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.*;

@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "league_id"}),
        @UniqueConstraint(columnNames = {"user_id", "currentDivision_id"})
})
public class Player extends ModelObject<Player> {

    @NotNull(message = "{validation.player.user}")
    @ManyToOne
    private User user;
    @NotNull(message = "{validation.player.status}")
    private PlayerStatus status;
    @ManyToOne
    private Division currentDivision;
    @NotNull(message = "{validation.player.league}")
    @ManyToOne
    private League league;
    @Transient
    private transient Map<Long, Match> matches = new HashMap<>();

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

    public Player withStatus(PlayerStatus playerStatus) {
        setStatus(playerStatus);
        return this;
    }

    public Division getCurrentDivision() {
        return currentDivision;
    }

    public void setCurrentDivision(Division currentDivision) {
        this.currentDivision = currentDivision;
        this.league = currentDivision.getRound().getLeague();
    }

    public Player withCurrentDivision(Division currentDivision) {
        setCurrentDivision(currentDivision);
        return this;
    }

    public League getLeague() {
        return league;
    }

    public void setLeague(League league) {
        this.league = league;
    }

    public Player withLeague(League league) {
        if (this.league == null) {
            setLeague(league);
        }
        return this;
    }

    public Collection<Match> getMatches() {
        List<Match> matches = new ArrayList<>(this.matches.values());
        Collections.sort(matches);
        return matches;
    }

    public Player addMatch(Match match) {
        matches.put(match.getId(), match);
        return this;
    }

    public List<String> getAllOpponentsEmails() {
        List<String> emails = new ArrayList<>();
        String myEmail = user.getEmail();
        if (matches != null && !matches.isEmpty()) {
            List<Match> values = new ArrayList<>(matches.values());
            Collections.sort(values);
            for (Match match : values) {
                String playerOneEmail = match.getPlayerOne().user.getEmail();
                if (!playerOneEmail.equals(myEmail) && !emails.contains(playerOneEmail)) {
                    emails.add(playerOneEmail);
                }
                String playerTwoEmail = match.getPlayerTwo().user.getEmail();
                if (!playerTwoEmail.equals(myEmail) && !emails.contains(playerTwoEmail)) {
                    emails.add(playerTwoEmail);
                }
            }
        }
        return emails;
    }

    public Player merge(Player player) {
        if (player.user != null) {
            this.user = player.user;
        }
        if (player.status != null) {
            this.status = player.status;
        }
        if (player.currentDivision != null) {
            this.currentDivision = player.currentDivision;
        }
        if (player.league != null) {
            this.league = player.league;
        }
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
