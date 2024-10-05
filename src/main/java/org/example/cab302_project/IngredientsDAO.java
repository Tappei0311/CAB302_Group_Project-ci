package org.example.cab302_project;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// Constructor: Initialize database connection
public class IngredientsDAO {
    private Connection connection;


    // Create the Ingredients table if it doesn't exist
    public IngredientsDAO() {
        connection = DatabaseConnection.getInstance();
    }
    public void createTable() {
        try {
            Statement createTable = connection.createStatement();
            createTable.execute(
                    "CREATE TABLE IF NOT EXISTS Ingredients ("
                            + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                            + "Ingredient VARCHAR NOT NULL, "
                            + "Quantity INTEGER NOT NULL, "
                            + "MinQuantity INTEGER NOT NULL, "
                            + "quick_access INTEGER NOT NULL"
                            + ")"
            );
        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }
    // Insert a new ingredient into the database
    public void Insert(Ingredient ingredients) {
        try {
            PreparedStatement insertIngredient = connection.prepareStatement(
                    "INSERT INTO Ingredients (Ingredient, Quantity, MinQuantity,quick_access) VALUES (?, ?, ?,?)"
            );
            insertIngredient.setString(1, ingredients.getIngredient());
            insertIngredient.setInt(2, ingredients.getQuantity());
            insertIngredient.setInt(3, ingredients.getMinQuantity());
            insertIngredient.setBoolean(4, ingredients.isQuick_access());
            insertIngredient.execute();
        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }

    // Retrieve all ingredients from the database
    public List<Ingredient> getAll() {
        List<Ingredient> ingredients = new ArrayList<>();
        try {
            Statement getAll = connection.createStatement();
            ResultSet rs = getAll.executeQuery("SELECT * FROM Ingredients");
            while (rs.next()) {
                ingredients.add(
                        new Ingredient(
                                rs.getInt("id"),
                                rs.getString("Ingredient"),
                                rs.getInt("Quantity"),
                                rs.getInt("MinQuantity"),
                                rs.getBoolean("quick_access")
                        )
                );
            }
        } catch (SQLException ex) {
            System.err.println(ex);
        }
        return ingredients;
    }

    // Get Ingredients if quick access is true
    public List<Ingredient> getQuickAccessIngredients() {
        List<Ingredient> ingredients = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Ingredients WHERE quick_access = ?");
            statement.setBoolean(1, true);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                ingredients.add(new Ingredient(
                        rs.getInt("id"),
                        rs.getString("Ingredient"),
                        rs.getInt("Quantity"),
                        rs.getInt("MinQuantity"),
                        rs.getBoolean("quick_access")
                ));
            }
        } catch (SQLException ex) {
            System.err.println("Error while fetching quick access ingredients: " + ex.getMessage());
        }
        return ingredients;
    }
    // Retrieve an ingredient by its ID
    public Ingredient getById(int id) {
        try {
            PreparedStatement getingredient = connection.prepareStatement("SELECT * FROM Ingredients WHERE id = ?");
            getingredient.setInt(1, id);
            ResultSet rs = getingredient.executeQuery();
            if (rs.next()) {
                return new Ingredient(
                        rs.getInt("id"),
                        rs.getString("Ingredient"),
                        rs.getInt("Quantity"),
                        rs.getInt("MinQuantity"),
                        rs.getBoolean("quick_access")
                );
            }
        } catch (SQLException ex) {
            System.err.println(ex);
        }
        return null;
    }

    // Update an existing ingredient in the database
    public void update(Ingredient ingredient) {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE Ingredients SET Ingredient = ?, Quantity = ?, MinQuantity = ?, quick_access = ? WHERE id = ?"
            );
            statement.setString(1, ingredient.getIngredient());
            statement.setInt(2, ingredient.getQuantity());
            statement.setInt(3, ingredient.getMinQuantity());
            statement.setBoolean(4, ingredient.isQuick_access());
            statement.setInt(5, ingredient.getId());
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 0) {
                System.err.println("No rows updated for ingredient with ID: " + ingredient.getId());
            }
        } catch (SQLException e) {
            System.err.println("Error updating ingredient: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Check if an ingredient with the given name already exists
    public boolean ingredientExists(String ingredientName) {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT COUNT(*) FROM Ingredients WHERE LOWER(Ingredient) = LOWER(?)"
            );
            statement.setString(1, ingredientName);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error checking if ingredient exists: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    // Delete an ingredient from the database
    public boolean delete(Ingredient ingredient) {
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM Ingredients WHERE id = ?");
            statement.setInt(1, ingredient.getId());
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting ingredient: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Close the database connection
    public void close() {
        try {
            connection.close();
        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }

    // Fetch ingredients that need restocking (current quantity < minimum quantity)
    public List<Ingredient> getIngredientsForRestocking() {
        List<Ingredient> ingredients = new ArrayList<>();
        try {
            String query = "SELECT * FROM Ingredients WHERE Quantity < MinQuantity";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                ingredients.add(new Ingredient(
                        rs.getInt("id"),
                        rs.getString("Ingredient"),
                        rs.getInt("Quantity"),
                        rs.getInt("MinQuantity"),
                        rs.getBoolean("quick_access")
                ));
            }
        } catch (SQLException ex) {
            System.err.println("Error fetching ingredients for restocking: " + ex.getMessage());
        }
        return ingredients;
    }

}

