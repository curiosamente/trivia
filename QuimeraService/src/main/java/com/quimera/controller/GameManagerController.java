package com.quimera.controller;

import com.quimera.model.*;
import com.quimera.services.BarService;
import com.quimera.services.TriviaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by Manu on 24/06/2016.
 */
@RestController
@CrossOrigin
@RequestMapping("/game")
public class GameManagerController {

    private Map<String, Game> gameMap = new ConcurrentHashMap<>();

    private Set<Bar> barSet = new HashSet<>();

    @Autowired
    private TriviaService triviaService;

    @Autowired
    private BarService barService;


    @RequestMapping(method = RequestMethod.GET)
    private List<Game> getGames() {
        return new ArrayList<>(gameMap.values());
    }

    @RequestMapping(value = "initialGame", method = RequestMethod.GET)
    private void initialTrivia() {

        gameMap = new ConcurrentHashMap<>();

        Bar bar = barService.authenticate("mm", "mm");
        Trivia trivia = triviaService.find("5779a663d4c6c81f3cbd1f66");

        Game game = new Game();
        game.setTrivia(trivia);
        game.setBar(bar);
        gameMap.putIfAbsent(game.getBar().getIdBar(), game);

    }

    @RequestMapping(value = "/finishTrivia", method = RequestMethod.PATCH)
    public ResponseEntity setTriviaStatus(@RequestParam String idBar) {
        if (gameMap.containsKey(idBar)) {
            Trivia trivia = gameMap.remove(idBar).getTrivia();
            trivia.setTriviaStatus(TriviaStatus.TERMINATED);
            triviaService.update(trivia);

            return new ResponseEntity(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/currentTrivia", method = RequestMethod.GET)
    public Trivia getCurrentTrivia(@RequestParam String idBar) {
        if (gameMap.containsKey(idBar)) {
            return gameMap.get(idBar).getTrivia();
        } else {
            return null;
        }
    }

    @RequestMapping(value = "/currentQuestion", method = RequestMethod.GET)
    public ResponseEntity<Question> getCurrentQuestion(@RequestParam String idBar) {
        Game game = gameMap.get(idBar);
        if (game == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Question question = gameMap.get(idBar).getCurrentQuestion();
        question.setCorrectAnswer(null);

        return new ResponseEntity<>(question, HttpStatus.OK);
    }

    @RequestMapping(value = "currentQuestion", method = RequestMethod.PUT)
    public ResponseEntity setCurrentQuestion(@RequestParam String idBar, @RequestBody Question question) {

        Game game = gameMap.get(idBar);
        if (game == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            game.setCurrentQuestion(question);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "status", method = RequestMethod.PATCH)
    public ResponseEntity setStatus(@RequestParam String idBar, @RequestBody String status) {
        Game game = gameMap.get(idBar);
        if (game == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        } else {
            gameMap.get(idBar).setGameStatus(GameStatus.valueOf(status));
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "status", method = RequestMethod.GET)
    public ResponseEntity<String> getStatus(String idBar) {
        Game game = gameMap.get(idBar);
        if (game == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(gameMap.get(idBar).getGameStatus().name(), HttpStatus.OK);

    }

    @RequestMapping(value = "elapsedTime", method = RequestMethod.PATCH)
    public ResponseEntity getElapsedTime(@RequestParam String idBar, @RequestBody int elapsedTime) {
        Game game = gameMap.get(idBar);
        if (game == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        } else {
            gameMap.get(idBar).setElapsedTime(elapsedTime);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "elapsedTime", method = RequestMethod.GET)
    public ResponseEntity<Integer> setElapsedTime(String idBar) {
        Game game = gameMap.get(idBar);
        if (game == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(gameMap.get(idBar).getElapsedTime(), HttpStatus.OK);

    }

//    @RequestMapping(value = "/scores", method = RequestMethod.GET)
//    public List<Player> getScores(@RequestParam String idBar) {
//        return gameMap.get(idBar).getScores();
//    }
//
//    @RequestMapping(value = "/pushAnswer", method = RequestMethod.POST)
//    public List<Player> getScores(@RequestParam String idBar, @RequestBody Answer answer) {
//        return gameMap.get(idBar).getScores();
//    }

    @RequestMapping(value = "/start", method = RequestMethod.GET)
    public void startGame() {

    }

    @Scheduled(fixedRate = 5000)
    public void triviaScheduler() {

        List<Trivia> triviaList = triviaService.findByTriviaStatus(TriviaStatus.NEW);

        triviaList.forEach((trivia) -> {
            if (trivia.getLocalDateTime() != null && isTriviaScheduledNextFifteenMinutes(trivia.getLocalDateTime())) {
                trivia.setTriviaStatus(TriviaStatus.LOADED);
                triviaService.update(trivia);
                scheduleTrivia(trivia);
            }
        });

    }

    private void scheduleTrivia(Trivia trivia) {

        LocalDateTime triviaLocalDateTime = LocalDateTime.ofInstant(trivia.getLocalDateTime().toInstant(), ZoneId.systemDefault());

        long seconds = LocalDateTime.now().until(triviaLocalDateTime, ChronoUnit.SECONDS);

        if (!barSet.contains(trivia.getBar())) {

            ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
            scheduler.schedule(() -> {

                Game game = new Game();
                game.setTrivia(trivia);

                gameMap.putIfAbsent(trivia.getBar().getIdBar(), game);

            }, seconds, TimeUnit.SECONDS);

            barSet.add(trivia.getBar());
        }

    }

    private boolean isTriviaScheduledNextFifteenMinutes(Date triviaDateTime) {

        LocalDateTime triviaLocalDateTime = LocalDateTime.ofInstant(triviaDateTime.toInstant(), ZoneId.systemDefault());

        LocalDateTime now = LocalDateTime.now();
        long minutes = triviaLocalDateTime.until(now.plusMinutes(5), ChronoUnit.MINUTES);

        return minutes >= 0 && minutes <= 5;
    }

}
