package com.quimera.services;

import com.quimera.model.Category;
import com.quimera.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Manu on 12/2/16.
 */
@SuppressWarnings("SpringJavaAutowiringInspection")
@Component
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public void insertAll(List<Category> categorys) {
        categoryRepository.insert(categorys);
    }

    public void insert(Category category) {
        categoryRepository.insert(category);
    }

    public void update(Category category) {
        categoryRepository.save(category);
    }

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public Category find(String id) {
        return categoryRepository.findOne(id);
    }

    public void delete(String id) {
        categoryRepository.delete(id);
    }

    public void deleteAll() {
        categoryRepository.deleteAll();
    }

}
