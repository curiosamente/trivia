package com.quimera.controller;

import com.quimera.services.*;
import com.quimera.util.DataGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Manu on 13/4/16.
 */
@RestController
@CrossOrigin
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private BannerService bannerService;

    @Autowired
    private BarService barService;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private TriviaService triviaService;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/initialData", method = RequestMethod.GET)
    public void initialData() {

        barService.insertAll(DataGenerator.barsExamples());
        questionService.insertAll(DataGenerator.questionsExamples());
        triviaService.insertAll(DataGenerator.triviaExamples());
        userService.insertAll(DataGenerator.usersExamples());
    }


}
