package com.quimera.repositories;

import com.quimera.model.Bar;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by Manu on 6/2/16.
 */
public interface BarRepository extends MongoRepository<Bar, String> {

    Bar findByUsernameAndPassword(String username, String password);

}
