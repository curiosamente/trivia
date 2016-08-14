package com.quimera.model;

import java.util.*;

/**
 * Created by Manu on 25/06/2016.
 */
public class Game {

    private Trivia trivia;
    private Bar bar;
    private Question currentQuestion;
    private GameStatus gameStatus = GameStatus.READY;
//    private Set<Player> playerList = new TreeSet<>();
    private Set<Answer> answerSet = new TreeSet<>();
    private Map<Player, Score> scoreMap = new TreeMap<>();
    private int elapsedTime;
    private int questionPosition;

//
//    public Set<Player> getPlayerList() {
//        return playerList;
//    }
//
//    public void setPlayerList(Set<Player> playerList) {
//        this.playerList = playerList;
//    }


    public int getQuestionPosition() {
        return questionPosition;
    }

    public void setQuestionPosition(int questionPosition) {
        this.questionPosition = questionPosition;
    }

    public Map<Player, Score> getScoreMap() {
        return scoreMap;
    }

    public void setScoreMap(Map<Player, Score> scoreMap) {
        this.scoreMap = scoreMap;
    }

    public Set<Answer> getAnswerSet() {
        return answerSet;
    }

    public void setAnswerSet(Set<Answer> answerSet) {
        this.answerSet = answerSet;
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
