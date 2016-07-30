package com.quimera.services;

import com.quimera.model.Subcategory;
import com.quimera.repositories.SubcategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Manu on 12/2/16.
 */
@SuppressWarnings("SpringJavaAutowiringInspection")
@Component
public class SubcategoryService {

    @Autowired
    private SubcategoryRepository categoryRepository;

    public void insertAll(List<Subcategory> categories) {
        categoryRepository.insert(categories);
    }

    public void insert(Subcategory subCategory) {
        categoryRepository.insert(subCategory);
    }

    public void update(Subcategory subCategory) {
        categoryRepository.save(subCategory);
    }

    public List<Subcategory> findAll() {
        return categoryRepository.findAll();
    }

    public Subcategory find(String id) {
        return categoryRepository.findOne(id);
    }

    public void delete(String id) {
        categoryRepository.delete(id);
    }

    public void deleteAll() {
        categoryRepository.deleteAll();
    }

}
