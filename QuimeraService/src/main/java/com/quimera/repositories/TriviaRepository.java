package com.quimera.repositories;

import com.quimera.model.Trivia;
import com.quimera.model.TriviaStatus;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created by Manu on 14/3/16.
 */
public interface TriviaRepository extends MongoRepository<Trivia, String> {


    List<Trivia> findByTriviaStatus(TriviaStatus triviaStatus);

}
