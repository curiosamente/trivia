package com.quimera.controller;

import com.quimera.model.User;
import com.quimera.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Manu on 6/2/16.
 */
@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(method = RequestMethod.POST)
    public void insert(@RequestBody User user) {
        userService.insert(user);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public void update(@RequestBody User user) {
        userService.update(user);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<User> getAll() {
        return userService.findAll();
    }

    @RequestMapping(params = "id", method = RequestMethod.GET)
    public User get(@RequestParam String id) {
        return userService.find(id);
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public void delete(@RequestParam String id) {
        userService.delete(id);
    }

    @RequestMapping(params = {"username"}, method = RequestMethod.GET)
    public User getByUsername(@RequestParam String username) {
        return userService.getByUsername(username);
    }

    @RequestMapping("/deleteAll")
    public void deleteAll() {
        userService.deleteAll();
    }

    @RequestMapping(value = "/authenticate", method = RequestMethod.GET)
    public User authenticate(@RequestParam String username, @RequestParam String password) {
        return userService.authenticate(username, password);
    }

}
