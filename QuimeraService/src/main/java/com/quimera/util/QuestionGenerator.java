package com.quimera.util;

import com.quimera.model.Constant;
import com.quimera.model.Question;
import com.quimera.services.TriviaService;

import java.util.concurrent.TimeUnit;

/**
 * Created by Manu on 31/1/16.
 */
public class QuestionGenerator implements Runnable {

    public void run() {

        for (Question question : TriviaService.trivia.getQuestions()) {

            TriviaService.currentQuestion = question;
            try {
                TimeUnit.SECONDS.sleep(Constant.TIME_TO_SHOW_QUESTIONS_IN_SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        TriviaService.currentQuestion = null;

    }
}
