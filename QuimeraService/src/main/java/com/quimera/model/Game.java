package com.quimera.model;

import java.util.*;

/**
 * Created by Manu on 25/06/2016.
 */
public class Game {

    private Trivia trivia;
    private Bar bar;
    private Question currentQuestion;
    private GameStatus gameStatus;
    private Set<Player> playerList = new TreeSet<>();
    private Set<Answer> answerHashSet = new HashSet<>();
    private List<Score> scoreList = new ArrayList<>();
    private int elapsedTime;

    public Set<Player> getPlayerList() {
        return playerList;
    }

    public void setPlayerList(Set<Player> playerList) {
        this.playerList = playerList;
    }

    public Set<Answer> getAnswerHashSet() {
        return answerHashSet;
    }

    public void setAnswerHashSet(Set<Answer> answerHashSet) {
        this.answerHashSet = answerHashSet;
    }

    public List<Score> getScoreList() {
        return scoreList;
    }

    public void setScoreList(List<Score> scoreList) {
        this.scoreList = scoreList;
    }

    public int getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(int elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    public Trivia getTrivia() {
        return trivia;
    }

    public void setTrivia(Trivia trivia) {
        this.trivia = trivia;
    }

    public Bar getBar() {
        return bar;
    }

    public void setBar(Bar bar) {
        this.bar = bar;
    }

    public Question getCurrentQuestion() {
        return currentQuestion;
    }

    public void setCurrentQuestion(Question currentQuestion) {
        this.currentQuestion = currentQuestion;
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }

    public void setGameStatus(GameStatus gameStatus) {
        this.gameStatus = gameStatus;
    }

}
