package org.example.cab302_project;

import java.util.List;

public interface IIngredientDAO {
    public  void addIngredient(Ingredient ingredient);

    public  void deleteIngredient(Ingredient ingredient);

    public Ingredient getIngredient(int id);

    public List<Ingredient> getAllIngredients();
}
