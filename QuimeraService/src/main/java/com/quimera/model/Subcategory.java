package com.quimera.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by Manu on 15/06/2016.
 */
@Document
public class Subcategory {

    @Id
    private String idSubcategory;
    private String name;
    @DBRef
    private Category category;

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getIdSubcategory() {
        return idSubcategory;
    }

    public void setIdSubcategory(String idSubcategory) {
        this.idSubcategory = idSubcategory;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
