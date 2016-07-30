package com.quimera.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Manu on 1/2/16.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class Score implements Comparable<Score> {

    private Player player;
    private int score;

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public int compareTo(Score score) {
        return new Integer(this.score).compareTo(score.getScore());
    }
}
