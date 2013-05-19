package org.squashleague.domain.league;

import com.google.common.base.Strings;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.squashleague.domain.ModelObject;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Entity
public class Match extends ModelObject {

    @NotNull(message = "{round.playerOne}")
    @ManyToOne
    private Player playerOne;
    @NotNull(message = "{round.playerTwo}")
    @ManyToOne
    private Player playerTwo;
    @NotNull(message = "{round.round}")
    @ManyToOne
    private Round round;
    @Pattern(regexp = "\\d{1,2}-\\d{1,2}", message = "{round.score}")
    private String score;
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime scoreEntered;

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
