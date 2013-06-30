package org.squashleague.domain.league;

/**
 * @author jamesdbloom
 */
public class ScoreEntry {

    private final Player player;
    private final Match match;
    private final Double points;

    public ScoreEntry(Player player, Match match, Double points) {
        this.player = player;
        this.match = match;
        this.points = points;
    }

    public Player getPlayer() {
        return player;
    }

    public Match getMatch() {
        return match;
    }

    public Double getPoints() {
        return points;
    }
}
