package org.example.cab302_project;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Recipe {
    private String name;  // Recipe name
    private ObservableList<String> ingredients;  // List of ingredients for the recipe

    // Constructor to initialize the recipe name and create an empty ingredients list
    public Recipe(String name) {
        this.name = name;
        this.ingredients = FXCollections.observableArrayList();
    }

    // Getter for recipe name
    public String getName() {
        return name;
    }

    // Setter for recipe name
    public void setName(String name) {
        this.name = name;
    }

    // Getter for ingredients list
    public ObservableList<String> getIngredients() {
        return ingredients;
    }

    // Method to add an ingredient to the ingredients list
    public void addIngredient(String ingredient) {
        ingredients.add(ingredient);
    }

    // Method to remove an ingredient from the ingredients list
    public void removeIngredient(String ingredient) {
        ingredients.remove(ingredient);
    }

    // Overriding the toString() method to display the recipe name in the ListView
    @Override
    public String toString() {
        return name;
    }
}

