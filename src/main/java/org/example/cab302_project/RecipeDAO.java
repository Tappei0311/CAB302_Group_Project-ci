package org.example.cab302_project;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RecipeDAO {
    private Connection connection;

    public RecipeDAO() {
        connection = DatabaseConnection.getInstance();
    }

    public void createRecipeTable() {
        try {
            Statement createTable = connection.createStatement();
            createTable.execute(
                    "CREATE TABLE IF NOT EXISTS Recipe ("
                            + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                            + "Recipe VARCHAR NOT NULL "
                            + ")"
            );
        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }
    public void createRecipeIngredientTable() {
        try {
            Statement createTable = connection.createStatement();
            createTable.execute(
                    "CREATE TABLE IF NOT EXISTS RecipeIngredients ("
                            + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                            + "Recipeid VARCHAR REFERENCES Recipe(id) ON DELETE CASCADE, "
                            + "Ingredientid INTEGER REFERENCES Ingredient(ingredientid), "
                            + "ingredientUsage INTEGER "
                            + ")"
            );
        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }

    //create
    public void InsertRecipe(Recipe recipe) {
        try {
            PreparedStatement insertRecipe = connection.prepareStatement(
                    "INSERT INTO Recipe (Recipe) VALUES (?)"
            );
            insertRecipe.setString(1, recipe.getName());

            insertRecipe.execute();
        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }

    public void InsertRecipeIngredient(RecipieIngredients ingredient) {
        try {
            PreparedStatement insertRecipeIngredient = connection.prepareStatement(
                    "INSERT INTO Recipe (Recipeid, Ingredientid, ingredientUsage) VALUES (?,?,?)"
            );
            insertRecipeIngredient.setInt(1, ingredient.getRecipeId());
            insertRecipeIngredient.setInt(2,ingredient.getIngredient().getId());
            insertRecipeIngredient.setInt(3,ingredient.getAmount());

            insertRecipeIngredient.execute();
        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }


    public List<Recipe> getAll() {
        List<Recipe> recipes = new ArrayList<>();
        try {
            Statement getAll = connection.createStatement();
            ResultSet rs = getAll.executeQuery("SELECT Recipie.* FROM Recipe JOIN RecipeIngredients ON Recipie.id = RecipeIngredients.Recipe JOIN Ingredients ON Ingredients.Ingredientid = RecipeIngredients.Ingredientid GROUP BY Recipie.id;");

            while (rs.next()) {
                recipes.add(
                        new Recipe(
                                rs.getInt("id"),
                                rs.getString("Recipe")
                        )
                );
            }
        } catch (SQLException ex) {
            System.err.println(ex);
        }
        return recipes;
    }





    //delete
    public void deleteRecipe(Recipe recipe) {
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM Recipe WHERE id = ?");
            statement.setInt(1, recipe.getId());
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void deleteRecipeIngredient(RecipieIngredients Ingredient) {
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM RecipeIngredients WHERE id = ?");
            statement.setInt(1, Ingredient.getId());
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
