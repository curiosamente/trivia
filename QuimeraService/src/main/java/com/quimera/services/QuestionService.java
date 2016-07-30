package com.quimera.services;

import com.quimera.model.Question;
import com.quimera.repositories.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Manu on 12/2/16.
 */
@Component
public class QuestionService {

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    private QuestionRepository questionRepository;

    public void insertAll(List<Question> questions) {
        questionRepository.insert(questions);
    }

    public void insert(Question question) {
        questionRepository.insert(question);
    }

    public void update(Question question) {
        questionRepository.save(question);
    }

    public List<Question> findAll() {
        return questionRepository.findAll();
    }

    public Question find(String id) {
        return questionRepository.findOne(id);
    }

    public void delete(String id) {
        questionRepository.delete(id);
    }

}
