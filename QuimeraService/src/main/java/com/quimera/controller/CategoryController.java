package com.quimera.controller;

import com.quimera.model.Category;
import com.quimera.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Manu on 6/2/16.
 */
@RestController
@CrossOrigin
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @RequestMapping(method = RequestMethod.POST)
    public void insert(@RequestBody Category category) {
        categoryService.insert(category);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public void update(@RequestBody Category category) {
        categoryService.update(category);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Category> getAll() {
        return categoryService.findAll();
    }

    @RequestMapping(params = "id", method = RequestMethod.GET)
    public Category get(@RequestParam String id) {
        return categoryService.find(id);
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public void delete(@RequestParam String id) {
        categoryService.delete(id);
    }

    @RequestMapping("/deleteAll")
    public void deleteAll() {
        categoryService.deleteAll();
    }

}
