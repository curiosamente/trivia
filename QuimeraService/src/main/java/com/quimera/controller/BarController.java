package com.quimera.controller;

import com.quimera.model.Bar;
import com.quimera.services.BarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Manu on 6/2/16.
 */
@RestController
@CrossOrigin
@RequestMapping("/bar")
public class BarController {

    @Autowired
    private BarService barService;

    @RequestMapping(method = RequestMethod.POST)
    public void insert(@RequestBody Bar bar) {
        barService.insert(bar);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public void update(@RequestBody Bar bar) {
        barService.update(bar);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Bar> getAll() {
        return barService.findAll();
    }

    @RequestMapping(params = "id", method = RequestMethod.GET)
    public Bar get(@RequestParam String id) {
        return barService.find(id);
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public void delete(@RequestParam String id) {
        barService.delete(id);
    }

    @RequestMapping("/deleteAll")
    public void deleteAll() {
        barService.deleteAll();
    }

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public Bar authenticate(@RequestParam String username, @RequestParam String password) {
        return barService.authenticate(username, password);
    }
}
