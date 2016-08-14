package com.quimera.model;

/**
 * Created by Manu on 11/3/16.
 */
public enum GameStatus {

    RUNNABLE, READY, TERMINATED,

    STARTING_TRIVIA, SHOWING_QUESTION, SHOWING_OPTIONS, SHOWING_CORRECT_ANSWER, SHOWING_DESCRIPTION, SHOWING_PARTIAL_WINNERS,
    SHOWING_BANNER, SHOWING_FINAL_WINNERS, WAITING_CORRECT_ANSWER
}
