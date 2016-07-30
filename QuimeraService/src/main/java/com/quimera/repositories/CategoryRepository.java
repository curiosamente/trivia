package com.quimera.repositories;

import com.quimera.model.Category;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by Manu on 9/2/16.
 */
public interface CategoryRepository extends MongoRepository<Category, String> {

}
