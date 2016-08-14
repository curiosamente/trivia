package com.quimera.controller;

import com.quimera.model.*;
import com.quimera.services.GameManagerService;
import com.quimera.services.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Created by Manu on 24/06/2016.
 */
@RestController
@CrossOrigin
@RequestMapping("/game")
public class GameManagerController {

    @Autowired
    private GameManagerService gameManagerService;

    @Autowired
    private QuestionService questionService;


    @RequestMapping(method = RequestMethod.GET)
    private List<Game> getGames() {
        return gameManagerService.getGames();
    }


    @RequestMapping(value = "finishTrivia", method = RequestMethod.PATCH)
    public ResponseEntity finishTrivia(@RequestParam String idBar) {
        if (gameManagerService.finishTrivia(idBar)) {
            return new ResponseEntity(HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "currentTrivia", method = RequestMethod.GET)
    public ResponseEntity<Trivia> getCurrentTrivia(@RequestParam String idBar) {

        Trivia trivia = gameManagerService.getCurrentTrivia(idBar);
        if (trivia != null) {
            return new ResponseEntity<>(trivia, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "currentQuestion", method = RequestMethod.GET)
    public ResponseEntity<Question> getCurrentQuestion(@RequestParam String idBar) {

        Question question = gameManagerService.getCurrentQuestion(idBar);

        if (question != null) {
            return new ResponseEntity<>(question, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "currentQuestion", method = RequestMethod.PUT)
    public ResponseEntity setCurrentQuestion(@RequestParam String idBar, @RequestBody Question question) {

        if (question != null && gameManagerService.setCurrentQuestion(idBar, question)) {
            return new ResponseEntity(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @RequestMapping(value = "status", method = RequestMethod.PATCH)
    public ResponseEntity setStatus(@RequestParam String idBar, @RequestBody String status) {

        if (gameManagerService.setStatus(idBar, status)) {
            return new ResponseEntity(HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

    }

    @RequestMapping(value = "status", method = RequestMethod.GET)
    public ResponseEntity<GameStatus> getStatus(String idBar) {

        GameStatus gameStatus = gameManagerService.getStatus(idBar);

        if (gameStatus != null) {
            return new ResponseEntity<>(gameStatus, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @RequestMapping(value = "scores", method = RequestMethod.GET)
    public ResponseEntity<List<Score>> getScores(@RequestParam String idBar) {

        List<Score> scores = gameManagerService.getScores(idBar);
        if (scores != null) {
            return new ResponseEntity<>(scores, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "winner", method = RequestMethod.GET)
    public ResponseEntity<Player> getWinner(@RequestParam String idBar) {

        List<Score> scores = gameManagerService.getScores(idBar);
        Score winner = null;
        if (scores != null & !scores.isEmpty()) {
            winner = scores.get(0);
        }
        if (winner != null && winner.getScore() > 0) {
            return new ResponseEntity<>(scores.get(0).getPlayer(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "pushAnswer", method = RequestMethod.POST)
    public ResponseEntity pushAnswer(@RequestParam String idBar, @RequestBody Answer answer) {

        if (gameManagerService.pushAnswer(idBar, answer)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        }
    }

    @Scheduled(fixedRate = 5000)
    public void triviaScheduler() {

        gameManagerService.triviaScheduler();

    }

}
