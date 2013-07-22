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
import java.math.BigDecimal;
import java.math.RoundingMode;

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
    private Division division;
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

    public Division getDivision() {
        return division;
    }

    public void setDivision(Division division) {
        this.division = division;
    }

    public Match withDivision(Division division) {
        setDivision(division);
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

    public Double getPlayerOneTotalPoints() {
        return calculateTotalPoints(getPlayerScore(0), getPlayerScore(1));
    }

    public Integer getPlayerOneWonOrLostPoints() {
        return calculateWonOrLostPoints(getPlayerScore(0), getPlayerScore(1));
    }

    public Double getPlayerOneGamesPoints() {
        return calculateGamesPoints(getPlayerScore(0));
    }

    public int getPlayerOneScore() {
        return getPlayerScore(0);
    }

    public Double getPlayerTwoTotalPoints() {
        return calculateTotalPoints(getPlayerScore(1), getPlayerScore(0));
    }

    public Integer getPlayerTwoWonOrLostPoints() {
        return calculateWonOrLostPoints(getPlayerScore(1), getPlayerScore(0));
    }

    public Double getPlayerTwoGamesPoints() {
        return calculateGamesPoints(getPlayerScore(1));
    }

    public int getPlayerTwoScore() {
        return getPlayerScore(1);
    }

    private Double calculateTotalPoints(int currentPlayer, int opponent) {
        if (score != null) {
            int wonOrLostPoints = calculateWonOrLostPoints(currentPlayer, opponent);
            double totalPoints = (wonOrLostPoints + calculateGamesPoints(currentPlayer)) / division.getName();
            return new BigDecimal(totalPoints).setScale(2, RoundingMode.HALF_UP).doubleValue();
        }
        return 0.0;
    }

    private double calculateGamesPoints(int currentPlayer) {
        return (currentPlayer / 10.0);
    }

    private int calculateWonOrLostPoints(int currentPlayer, int opponent) {
        int wonOrLostPoints = 2;
        if (currentPlayer > opponent) {
            wonOrLostPoints = 3;
        } else if (opponent > currentPlayer) {
            wonOrLostPoints = 1;
        }
        return wonOrLostPoints;
    }

    private int getPlayerScore(int i) {
        if (score != null) {
            String[] split = score.split("-");
            if (split.length == 2) {
                return Integer.parseInt(split[i]);
            }
        }
        return 0;
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
        if (match.division != null) {
            this.division = match.division;
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
        int roundComparison = division.compareTo(other.division);
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
