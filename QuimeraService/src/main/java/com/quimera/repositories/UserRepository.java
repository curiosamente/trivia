package com.quimera.repositories;

import com.quimera.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by Manu on 9/2/16.
 */
public interface UserRepository extends MongoRepository<User, String> {

    User findByUsername(String username);

    User findByUsernameAndPassword(String username, String password);

}
