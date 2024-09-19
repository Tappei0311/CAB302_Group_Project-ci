package org.example.cab302_project;

public class RecipieIngredients {
    private int id;
    private int recipeId;
    private int ingredientId;  // Add this line
    private Ingredient ingredient;
    private int amount;

    public RecipieIngredients(int id, int recipeId, Ingredient ingredient, int amount) {
        this.id = id;
        this.recipeId = recipeId;
        this.ingredientId = ingredient.getId();  // Add this line
        this.ingredient = ingredient;
        this.amount = amount;
    }

    public RecipieIngredients(int recipeId, Ingredient ingredient, int amount) {
        this.recipeId = recipeId;
        this.ingredientId = ingredient.getId();  // Add this line
        this.ingredient = ingredient;
        this.amount = amount;
    }

    // Add getter for ingredientId
    public int getIngredientId() {
        return ingredientId;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getRecipeId() {
        return recipeId;
    }
    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }
    public Ingredient getIngredient() {
        return ingredient;
    }
    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }
    public int getAmount() {
        return amount;
    }
    public void setAmount(int amount) {
        this.amount = amount;
    }

}
