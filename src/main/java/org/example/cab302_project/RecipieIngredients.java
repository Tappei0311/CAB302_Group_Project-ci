package org.example.cab302_project;

public class RecipieIngredients {
    private int id;
    private int recipeId;
    private Ingredient ingredient;
    private int amount;

    public RecipieIngredients(int id,int RecipeId, Ingredient ingredient, int Amount){
        this.id = id;
        this.recipeId = RecipeId;
        this.ingredient = ingredient;
        this.amount = Amount;

    }
    public RecipieIngredients(int RecipeId, Ingredient ingredient, int Amount){
        this.recipeId = RecipeId;
        this.ingredient = ingredient;
        this.amount = Amount;
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
