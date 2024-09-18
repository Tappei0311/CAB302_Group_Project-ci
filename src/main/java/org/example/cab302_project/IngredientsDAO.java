package org.example.cab302_project;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class IngredientsDAO {
    private Connection connection;

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
    //create
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

    //read
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

    //update
    public void updateContact(Ingredient ingredient) {
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE ingredient SET Ingredient = ?, Quantity = ?, MinQuantity = ?, quick_access = ? WHERE id = ?");
            statement.setString(1, ingredient.getIngredient());
            statement.setInt(2, ingredient.getQuantity());
            statement.setInt(3, ingredient.getMinQuantity());
            statement.setBoolean(4, ingredient.isQuick_access());
            statement.setInt(5, ingredient.getId());
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
//delete
public void delete(Ingredient ingredient) {
    try {
        PreparedStatement statement = connection.prepareStatement("DELETE FROM Ingredient WHERE id = ?");
        statement.setInt(1, ingredient.getId());
        statement.executeUpdate();
    } catch (Exception e) {
        e.printStackTrace();

    }
}

    public void close() {
        try {
            connection.close();
        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }

}

