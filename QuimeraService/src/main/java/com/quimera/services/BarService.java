package com.quimera.services;

import com.quimera.model.Bar;
import com.quimera.repositories.BarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Manu on 11/2/16.
 */
@Component
public class BarService {

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    private BarRepository barRepository;

    public void insertAll(List<Bar> bars) {
        barRepository.insert(bars);
    }

    public void insert(Bar bar) {
        barRepository.insert(bar);
    }

    public void update(Bar bar) {
        barRepository.save(bar);
    }

    public List<Bar> findAll() {
        return barRepository.findAll();
    }

    public Bar find(String id) {
        return barRepository.findOne(id);
    }

    public void delete(String id) {
        barRepository.delete(id);
    }

    public void deleteAll() {
        barRepository.deleteAll();
    }

    public Bar authenticate(String username, String password) {
        return barRepository.findByUsernameAndPassword(username, password);
    }
}
