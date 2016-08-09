package com.quimera.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Objects;

/**
 * Created by Manu on 31/1/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Answer implements Comparable<Answer> {

    private Player player;
    private String idQuestion;
    private String answer;

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }


    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getIdQuestion() {
        return idQuestion;
    }

    public void setIdQuestion(String idQuestion) {
        this.idQuestion = idQuestion;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Answer)) return false;
        Answer answer = (Answer) o;
        return Objects.equals(player, answer.player) &&
                Objects.equals(idQuestion, answer.idQuestion);
    }

    @Override
    public int compareTo(Answer o) {
        int playerResult = this.getPlayer().compareTo(o.getPlayer());
        if(playerResult==0){
            return this.getIdQuestion().compareTo(o.getIdQuestion());
        } else {
            return this.getPlayer().compareTo(o.getPlayer());
        }
    }
}
