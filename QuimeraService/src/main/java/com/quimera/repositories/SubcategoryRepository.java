package com.quimera.repositories;

import com.quimera.model.Subcategory;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by Manu on 9/2/16.
 */
public interface SubcategoryRepository extends MongoRepository<Subcategory, String> {

}
