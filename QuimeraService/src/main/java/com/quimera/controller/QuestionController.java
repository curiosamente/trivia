package com.quimera.controller;

import com.quimera.model.Question;
import com.quimera.services.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Manu on 5/2/16.
 */
@CrossOrigin
@RestController
@RequestMapping("/question")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @RequestMapping(method = RequestMethod.POST)
    public void insert(@RequestBody Question question) {
        questionService.insert(question);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public void update(@RequestBody Question question) {
        questionService.update(question);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Question> getAll() {
        return questionService.findAll();
    }

    @RequestMapping(params = "id", method = RequestMethod.GET)
    public Question get(@RequestParam String id) {
        return questionService.find(id);
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public void delete(@RequestParam String id) {
        questionService.delete(id);
    }


}
