package com.quimera.controller;

import com.quimera.CuriosamenteMainConfiguration;
import com.quimera.model.*;
import com.quimera.util.DataGenerator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by Manu on 9/2/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(CuriosamenteMainConfiguration.class)
@WebIntegrationTest

public class TriviaIntegrationTest {

    private static final String URL_QUIMERA_SERVICES = "http://localhost:9000";
    private RestTemplate restTemplate = new TestRestTemplate();
    private List<Bar> barList = DataGenerator.barsExamples();
    private List<User> userList = DataGenerator.usersExamples();
    private List<Question> questionsList = DataGenerator.questionsExamples();


    @Test
    public void testPushAnswer() throws Exception {

//        Question activeQuestion = questionsList.get(0);
//        Bar bar = barList.get(0);
//        Answer answer1 = new Answer();
//        answer1.setBar(bar);
//        answer1.setQuestion(activeQuestion);
//        answer1.setPlayer(userList.get(0));
//        answer1.setAnswer("wrong answer");
//
//        assertThat(answer1.isCorrectAnswer(), equalTo(Boolean.FALSE));
//
//        TriviaService.currentQuestion = activeQuestion;
//
//        restTemplate.postForObject(URL_QUIMERA_SERVICES + "/trivia/pushAnswer", answer1, Answer.class);
//
//        Answer answer2 = new Answer();
//        answer2.setBar(bar);
//        answer2.setQuestion(activeQuestion);
//        answer2.setPlayer(userList.get(1));
//        answer2.setAnswer(activeQuestion.getCorrectAnswer());
//
//        restTemplate.postForObject(URL_QUIMERA_SERVICES + "/trivia/pushAnswer", answer2, Answer.class);
//
//        restTemplate.postForObject(URL_QUIMERA_SERVICES + "/trivia/pushAnswer", answer2, Answer.class);
//
//
//        activeQuestion = questionsList.get(1);
//        TriviaService.currentQuestion = activeQuestion;
//
//        answer1.setQuestion(activeQuestion);
//        answer1.setAnswer(activeQuestion.getCorrectAnswer());
//
//        restTemplate.postForObject(URL_QUIMERA_SERVICES + "/trivia/pushAnswer", answer1, Answer.class);
//
//        answer2.setQuestion(activeQuestion);
//        answer2.setAnswer(activeQuestion.getCorrectAnswer());
//
//        restTemplate.postForObject(URL_QUIMERA_SERVICES + "/trivia/pushAnswer", answer2, Answer.class);
//
//
//        Score[] scores = restTemplate.postForObject(URL_QUIMERA_SERVICES + "/trivia/getScore", bar, Score[].class);
//
//        System.out.print(Arrays.toString(scores));
    }

}