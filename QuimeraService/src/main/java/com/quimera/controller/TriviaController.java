package com.quimera.controller;

import com.quimera.model.Trivia;
import com.quimera.model.TriviaStatus;
import com.quimera.services.TriviaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Manu on 31/1/16.
 */
@RestController
@CrossOrigin
@RequestMapping("/trivia")
public class TriviaController {

    @Autowired
    private TriviaService triviaService;

    @RequestMapping(method = RequestMethod.POST)
    public void insert(@RequestBody Trivia trivia) {
        triviaService.insert(trivia);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public void update(@RequestBody Trivia trivia) {
        trivia.setTriviaStatus(TriviaStatus.NEW);
        triviaService.update(trivia);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Trivia> getAll() {
        return triviaService.findAll();
    }

    @RequestMapping(params = "id", method = RequestMethod.GET)
    public Trivia get(@RequestParam String id) {
        return triviaService.find(id);
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public void delete(@RequestParam String id) {
        triviaService.delete(id);
    }

}