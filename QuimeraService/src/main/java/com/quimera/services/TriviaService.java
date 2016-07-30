package com.quimera.services;

import com.quimera.model.Question;
import com.quimera.model.Trivia;
import com.quimera.model.TriviaStatus;
import com.quimera.repositories.TriviaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Manu on 12/2/16.
 */
@Component
@SuppressWarnings("SpringJavaAutowiringInspection")
public class TriviaService {

    public static Trivia trivia;
    public static Question currentQuestion;

    @Autowired
    private TriviaRepository triviaRepository;

    public void insertAll(List<Trivia> trivias) {
        triviaRepository.insert(trivias);
    }


    public void insert(Trivia trivia) {
        triviaRepository.insert(trivia);
    }

    public void update(Trivia trivia) {
        triviaRepository.save(trivia);
    }

    public List<Trivia> findAll() {
        return triviaRepository.findAll();
    }

    public Trivia find(String id) {
        return triviaRepository.findOne(id);
    }

    public void delete(String id) {
        triviaRepository.delete(id);
    }

    public List<Trivia> findByTriviaStatus(TriviaStatus triviaStatus) {
        return triviaRepository.findByTriviaStatus(triviaStatus);
    }

}
