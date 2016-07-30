package com.quimera.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by Manu on 31/1/16.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
@Document
public class Question {

    @Id
    private String idQuestion;
    private String question;
    private final List<String> options = new ArrayList<>();
    private String correctAnswer;
    private String description;
    @DBRef
    private Category category;

    @DBRef
    private Subcategory subcategory;

    @Transient
    private int currentPosition;

    public int getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
    }

    public Subcategory getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(Subcategory subcategory) {
        this.subcategory = subcategory;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<String> getOptions() {
        return options;
    }

    public void addOption(String option) {
        this.options.add(option);
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public String getIdQuestion() {
        return idQuestion;
    }

    public void setIdQuestion(String idQuestion) {
        this.idQuestion = idQuestion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Question)) return false;
        Question question1 = (Question) o;
        return Objects.equals(idQuestion, question1.idQuestion) &&
                Objects.equals(question, question1.question) &&
                Objects.equals(options, question1.options) &&
                Objects.equals(correctAnswer, question1.correctAnswer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idQuestion, question, options, correctAnswer);
    }

    @Override
    public String toString() {
        return "Question{" +
                "idQuestion='" + idQuestion + '\'' +
                ", question='" + question + '\'' +
                ", options=" + options +
                ", correctAnswer='" + correctAnswer + '\'' +
                '}';
    }
}
