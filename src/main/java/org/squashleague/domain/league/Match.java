package org.squashleague.domain.league;

import com.google.common.base.Strings;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.squashleague.domain.ModelObject;
import org.squashleague.domain.account.User;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Entity
@Cacheable
@Table(name = "Matches")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Match extends ModelObject<Match> implements Comparable<Match> {

    public final static String SCORE_PATTERN = "[0-9]{1,2}-[0-9]{1,2}";
    @NotNull(message = "{validation.match.playerOne}")
    @ManyToOne
    private Player playerOne;
    @NotNull(message = "{validation.match.playerTwo}")
    @ManyToOne
    private Player playerTwo;
    @NotNull(message = "{validation.match.round}")
    @ManyToOne
    private Round round;
    @Pattern(regexp = "\\d{1,2}-\\d{1,2}", message = "{validation.match.score}")
    private String score;
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime scoreEntered;

    public boolean isMyMatch(User user) {
        return user != null && (playerOne.getUser().getId().equals(user.getId()) || playerTwo.getUser().getId().equals(user.getId()));
    }

    public Player getPlayerOne() {
        return playerOne;
    }

    public void setPlayerOne(Player playerOne) {
        this.playerOne = playerOne;
    }

    public Match withPlayerOne(Player playerOne) {
        setPlayerOne(playerOne);
        return this;
    }

    public Player getPlayerTwo() {
        return playerTwo;
    }

    public void setPlayerTwo(Player playerTwo) {
        this.playerTwo = playerTwo;
    }

    public Match withPlayerTwo(Player playerTwo) {
        setPlayerTwo(playerTwo);
        return this;
    }

    public Round getRound() {
        return round;
    }

    public void setRound(Round round) {
        this.round = round;
    }

    public Match withRound(Round round) {
        setRound(round);
        return this;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        if (!Strings.isNullOrEmpty(score)) {
            this.score = score;
            this.scoreEntered = DateTime.now();
        }
    }

    public Match withScore(String score) {
        setScore(score);
        return this;
    }

    public DateTime getScoreEntered() {
        return scoreEntered;
    }

    public Match merge(Match match) {
        if (match.playerOne != null) {
            this.playerOne = match.playerOne;
        }
        if (match.playerTwo != null) {
            this.playerTwo = match.playerTwo;
        }
        if (match.round != null) {
            this.round = match.round;
        }
        if (match.score != null) {
            this.score = match.score;
        }
        if (match.scoreEntered != null) {
            this.scoreEntered = match.scoreEntered;
        }
        return this;
    }

    @Override
    public int compareTo(Match other) {
        int roundComparison = round.compareTo(other.round);
        if (roundComparison == 0) {
            int playerOneComparison = playerOne.getUser().getName().compareTo(other.playerOne.getUser().getName());
            if (playerOneComparison == 0) {
                return playerTwo.getUser().getName().compareTo(other.playerTwo.getUser().getName());
            } else {
                return playerOneComparison;
            }
        } else {
            return roundComparison;
        }
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
