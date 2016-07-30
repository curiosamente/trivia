package com.quimera.controller;

import com.quimera.model.Banner;
import com.quimera.services.BannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Manu on 6/2/16.
 */
@RestController
@CrossOrigin
@RequestMapping("/banner")
public class BannerController {

    @Autowired
    private BannerService bannerService;

    @RequestMapping(method = RequestMethod.POST)
    public void insert(@RequestBody Banner banner) {
        bannerService.insert(banner);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public void update(@RequestBody Banner banner) {
        bannerService.update(banner);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Banner> getAll() {
        return bannerService.findAll();
    }

    @RequestMapping(params = "id", method = RequestMethod.GET)
    public Banner get(@RequestParam String id) {
        return bannerService.find(id);
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public void delete(@RequestParam String id) {
        bannerService.delete(id);
    }

    @RequestMapping("/deleteAll")
    public void deleteAll() {
        bannerService.deleteAll();
    }
}
