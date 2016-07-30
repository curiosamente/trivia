package com.quimera.repositories;

import com.quimera.model.Banner;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by Manu on 6/2/16.
 */
public interface BannerRepository extends MongoRepository<Banner, String> {

}
