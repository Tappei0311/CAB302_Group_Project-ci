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
            System.err.println("Error creating Recipe table: " + ex.getMessage());
        }
    }

    public void createRecipeIngredientTable() {
        try {
            Statement createTable = connection.createStatement();
            createTable.execute(
                    "CREATE TABLE IF NOT EXISTS RecipeIngredients ("
                            + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                            + "Recipeid INTEGER, "
                            + "Ingredientid INTEGER, "
                            + "ingredientUsage INTEGER, "
                            + "FOREIGN KEY (Recipeid) REFERENCES Recipe(id) ON DELETE CASCADE, "
                            + "FOREIGN KEY (Ingredientid) REFERENCES Ingredients(id)"
                            + ")"
            );
        } catch (SQLException ex) {
            System.err.println("Error creating RecipeIngredients table: " + ex.getMessage());
        }
    }

    //create
    public int InsertRecipe(Recipe recipe) {
        try {
            PreparedStatement insertRecipe = connection.prepareStatement(
                    "INSERT INTO Recipe (Recipe) VALUES (?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            insertRecipe.setString(1, recipe.getName());
            int affectedRows = insertRecipe.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating recipe failed, no rows affected.");
            }

            try (ResultSet generatedKeys = insertRecipe.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1);
                    recipe.setId(id);
                    return id;
                } else {
                    throw new SQLException("Creating recipe failed, no ID obtained.");
                }
            }
        } catch (SQLException ex) {
            System.err.println("Error inserting recipe: " + ex.getMessage());
        }
        return -1;
    }

    public List<Recipe> getAll() {
        List<Recipe> recipes = new ArrayList<>();
        try {
            Statement getAll = connection.createStatement();
            ResultSet rs = getAll.executeQuery("SELECT id, Recipe FROM Recipe");

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("Recipe");
                Recipe recipe = new Recipe(id, name);
                recipes.add(recipe);
                System.out.println("Retrieved recipe: ID = " + id + ", Name = " + name);
            }
        } catch (SQLException ex) {
            System.err.println("Error getting all recipes: " + ex.getMessage());
        }
        return recipes;
    }

    public void InsertRecipeIngredient(RecipieIngredients ingredient) {
        try {
            PreparedStatement insertRecipeIngredient = connection.prepareStatement(
                    "INSERT INTO RecipeIngredients (Recipeid, Ingredientid, ingredientUsage) VALUES (?,?,?)"
            );
            insertRecipeIngredient.setInt(1, ingredient.getRecipeId());
            insertRecipeIngredient.setInt(2, ingredient.getIngredient().getId());
            insertRecipeIngredient.setInt(3, ingredient.getAmount());

            insertRecipeIngredient.execute();
        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }


    public List<RecipieIngredients> getIngredientsForRecipe(int recipeId) {
        List<RecipieIngredients> ingredients = new ArrayList<>();
        try {
            String query = "SELECT ri.id, ri.Recipeid, ri.Ingredientid, ri.ingredientUsage, i.Ingredient " +
                    "FROM RecipeIngredients ri " +
                    "JOIN Ingredients i ON ri.Ingredientid = i.id " +
                    "WHERE ri.Recipeid = ?";
            System.out.println("Executing query: " + query + " with recipeId: " + recipeId);

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, recipeId);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                Ingredient ingredient = new Ingredient(
                        rs.getInt("Ingredientid"),
                        rs.getString("Ingredient"),
                        0, 0, false
                );
                RecipieIngredients ri = new RecipieIngredients(
                        rs.getInt("id"),
                        rs.getInt("Recipeid"),
                        ingredient,
                        rs.getInt("ingredientUsage")
                );
                ingredients.add(ri);
                System.out.println("Retrieved ingredient: " + ingredient.getIngredient() + " for recipe ID: " + recipeId);
            }
        } catch (SQLException ex) {
            System.err.println("Error fetching ingredients for recipe: " + ex.getMessage());
            ex.printStackTrace();
        }
        System.out.println("Total ingredients found for recipe ID " + recipeId + ": " + ingredients.size());
        return ingredients;
    }





    public boolean deleteRecipe(Recipe recipe) {
        try {
            connection.setAutoCommit(false);

            PreparedStatement deleteRecipe = connection.prepareStatement("DELETE FROM Recipe WHERE id = ?");
            deleteRecipe.setInt(1, recipe.getId());
            int rowsAffected = deleteRecipe.executeUpdate();

            connection.commit();

            if (rowsAffected == 0) {
                System.err.println("No recipe found with id: " + recipe.getId());
                return false;
            }
            return true;
        } catch (Exception e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            System.err.println("Error deleting recipe: " + e.getMessage());
            return false;
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
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
