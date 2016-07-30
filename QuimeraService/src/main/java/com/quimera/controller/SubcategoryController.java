package com.quimera.controller;

import com.quimera.model.Subcategory;
import com.quimera.services.SubcategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Manu on 6/2/16.
 */
@RestController
@CrossOrigin
@RequestMapping("/subcategory")
public class SubcategoryController {

    @Autowired
    private SubcategoryService subcategoryService;

    @RequestMapping(method = RequestMethod.POST)
    public void insert(@RequestBody Subcategory subCategory) {
        subcategoryService.insert(subCategory);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public void update(@RequestBody Subcategory subCategory) {
        subcategoryService.update(subCategory);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Subcategory> getAll() {
        return subcategoryService.findAll();
    }

    @RequestMapping(params = "id", method = RequestMethod.GET)
    public Subcategory get(@RequestParam String id) {
        return subcategoryService.find(id);
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public void delete(@RequestParam String id) {
        subcategoryService.delete(id);
    }

    @RequestMapping("/deleteAll")
    public void deleteAll() {
        subcategoryService.deleteAll();
    }

}
