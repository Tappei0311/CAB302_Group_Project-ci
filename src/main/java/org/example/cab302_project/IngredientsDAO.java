package org.example.cab302_project;

import java.util.ArrayList;
import java.util.List;

public class IngredientsDAO implements IIngredientDAO {

    public static final ArrayList<Ingredient> ingredients = new ArrayList<>();
    private  static int autoIncrementedId = 0;

    public IngredientsDAO() {

    }

    @Override
    public void addIngredient(Ingredient ingredient) {
        ingredient.setId(autoIncrementedId);
        autoIncrementedId++;
        ingredients.add(ingredient);
    }

    @Override
    public void deleteIngredient(Ingredient ingredient) {
        ingredients.remove(ingredient);
    }

    @Override
    public Ingredient getIngredient(int id) {
        for (Ingredient ingredient : ingredients) {
            if (ingredient.getId() == id) {
                return ingredient;
            }
        }
        return null;
    }

    @Override
    public List<Ingredient> getAllIngredients() {
        return new ArrayList<>(ingredients);
    }
}
