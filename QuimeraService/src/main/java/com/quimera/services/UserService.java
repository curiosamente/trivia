package com.quimera.services;

import com.quimera.model.User;
import com.quimera.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Manu on 12/2/16.
 */
@SuppressWarnings("SpringJavaAutowiringInspection")
@Component
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void insertAll(List<User> users) {
        userRepository.insert(users);
    }

    public void insert(User user) {
        userRepository.insert(user);
    }

    public void update(User user) {
        userRepository.save(user);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User find(String id) {
        return userRepository.findOne(id);
    }

    public void delete(String id) {
        userRepository.delete(id);
    }

    public void deleteAll() {
        userRepository.deleteAll();
    }

    public User getByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User authenticate(String username, String password) {

        return userRepository.findByUsernameAndPassword(username, password);

    }
}
