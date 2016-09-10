package com.quimera.services;

import com.quimera.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
@Component
public class GameManagerService {

    private Map<String, Game> gameMap = new ConcurrentHashMap<>();

    private Set<Bar> barSet = new HashSet<>();

    @Autowired
    private TriviaService triviaService;

    @Autowired
    private BarService barService;

    @Autowired
    private QuestionService questionService;

    public List<Game> getGames() {
        return new ArrayList<>(gameMap.values());
    }

    public boolean finishTrivia(String idBar) {
        boolean isUpdated = false;
        if (gameMap.containsKey(idBar)) {
            Game game = gameMap.remove(idBar);
            game.setGameStatus(GameStatus.TERMINATED);
            Trivia trivia = game.getTrivia();
            trivia.setTriviaStatus(TriviaStatus.TERMINATED);
            triviaService.update(trivia);
            isUpdated = true;
        }

        return isUpdated;
    }

    public Trivia getCurrentTrivia(String idBar) {

        Trivia currentTrivia = null;
        if (gameMap.containsKey(idBar)) {
            currentTrivia = gameMap.get(idBar).getTrivia();
        }
        return currentTrivia;
    }

    public Question getCurrentQuestion(String idBar) {

        Question question = null;

        Game game = gameMap.get(idBar);

        if (game != null) {
            question = gameMap.get(idBar).getCurrentQuestion();
        }

        return question;
    }

    public boolean setCurrentQuestion(String idBar, Question question) {

        boolean isUpdated = false;

        Game game = gameMap.get(idBar);
        if (game != null) {
            game.setCurrentQuestion(question);
            isUpdated = true;
        }
        return isUpdated;
    }

    public boolean setStatus(String idBar, String status) {

        boolean isUpdated = false;

        Game game = gameMap.get(idBar);
        if (game != null) {
            GameStatus gameStatus = GameStatus.valueOf(status);
            if (gameStatus.equals(GameStatus.STARTING_TRIVIA)) {
                game.resetScore();
            }
            gameMap.get(idBar).setGameStatus(gameStatus);
            isUpdated = true;
        }

        return isUpdated;
    }

    public GameStatus getStatus(String idBar) {
        Game game = gameMap.get(idBar);
        GameStatus gameStatus = null;
        if (game != null) {
            gameStatus = gameMap.get(idBar).getGameStatus();
        }
        return gameStatus;

    }

    public boolean pushAnswer(String idBar, Answer answer) {

        Game game = gameMap.get(idBar);

        if (game != null && game.getGameStatus().equals(GameStatus.SHOWING_OPTIONS)
                && game.getCurrentQuestion().getIdQuestion().equals(answer.getIdQuestion())
                && game.getAnswerSet().add(answer)) {

            game.getScoreMap().putIfAbsent(answer.getPlayer(), new Score(answer.getPlayer()));

            game.getScoreMap().compute(answer.getPlayer(), (player, score) -> {
                score.setScore(this.isCorrectAnswer(answer) ? score.getScore() + Constant.POINTS_CORRECT_ANSWER : score.getScore());
                return score;
            });

            return true;
        } else {
            return false;
        }

    }

    private boolean isCorrectAnswer(Answer answer) {
        Question question = questionService.find(answer.getIdQuestion());

        return question.getCorrectAnswer().equals(answer.getAnswer());
    }



    public List<Score> getScores(String idBar) {

        List<Score> scores = null;
        Game game = gameMap.get(idBar);

        if (game != null) {
            scores = new LinkedList<>(game.getScoreMap().values());
            Collections.sort(scores);
        }

        return scores;
    }

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

    private Player getWinner(String idBar) {
        List<Score> scores = this.getScores(idBar);

        Player playerWinner = null;

        if (scores != null && !scores.isEmpty()) {
            Score winner = scores.get(0);
            playerWinner = winner.getPlayer();
        }

        return playerWinner;
    }

    public synchronized Player isWinner(String idBar, Player player) {

        Player winner = getWinner(idBar);

        if (winner != null) {
            if (winner.equals(player)) {

                player.setWinner(true);
                player.setPrizeClaimed(winner.isPrizeClaimed());

                if(!winner.isPrizeClaimed()) {
                    winner.setPrizeClaimed(true);
                }
            }

        }
        return player;
    }

}
