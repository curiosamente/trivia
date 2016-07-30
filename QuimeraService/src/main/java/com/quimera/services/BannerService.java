package com.quimera.services;

import com.quimera.model.Banner;
import com.quimera.repositories.BannerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Manu on 11/2/16.
 */
@Component
public class BannerService {

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    private BannerRepository bannerRepository;

    public void insertAll(List<Banner> banners) {
        bannerRepository.insert(banners);
    }

    public void insert(Banner banner) {
        bannerRepository.insert(banner);
    }

    public void update(Banner banner) {
        bannerRepository.save(banner);
    }

    public List<Banner> findAll() {
        return bannerRepository.findAll();
    }

    public Banner find(String id) {
        return bannerRepository.findOne(id);
    }

    public void delete(String id) {
        bannerRepository.delete(id);
    }

    public void deleteAll() {
        bannerRepository.deleteAll();
    }

}
