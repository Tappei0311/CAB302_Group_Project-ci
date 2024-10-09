package org.example.cab302_project;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * A small suite of tests which test the functionality of the IngredientsDAO class
 * this includes testing adding, deleting and updating functionality to ensure that the DAO is functionally correct
 *
 */
class IngredientsDAOTest {
    private IngredientsDAO ingredientsDAO;

    /**
     * Initialize IngredientsDAO to ensure the database tables exist before testing occurs,  also clears the ingredient
     * table to ensure a fresh test environment
     */
    @BeforeEach
    void setUp() {
        ingredientsDAO = new IngredientsDAO();
        ingredientsDAO.createTable();  // This will ensure the table exists before each test
        clearTable();  // Clear the table before each test
    }

    /**
     * Clears the ingredients table in the database to provide a clean slate for each test
     */
    private void clearTable() {
        try {
            Connection connection = DatabaseConnection.getInstance();
            connection.createStatement().execute("DELETE FROM Ingredients");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Test if an ingredient can be inserted into the database and verifies the ingredients name, quantity,
     * Minimum quantity and if it has a quick access flag
     */
    @Test
    void testInsertAndGetIngredient() {
        Ingredient ingredient = new Ingredient("Test Ingredient", 100, 10, false);
        ingredientsDAO.Insert(ingredient);

        List<Ingredient> ingredients = ingredientsDAO.getAll();
        assertEquals(1, ingredients.size());
        assertEquals("Test Ingredient", ingredients.get(0).getIngredient());
        assertEquals(100, ingredients.get(0).getQuantity());
        assertEquals(10, ingredients.get(0).getMinQuantity());
        assertFalse(ingredients.get(0).isQuick_access());
    }

    //@Test
    //void testUpdateIngredient() {
        //Ingredient ingredient = new Ingredient("Original Ingredient", 100, 10, false);
        //ingredientsDAO.Insert(ingredient);

        //List<Ingredient> ingredients = ingredientsDAO.getAll();
        //Ingredient toUpdate = ingredients.get(0);
        //toUpdate.setIngredient("Updated Ingredient");
        //toUpdate.setQuantity(200);
        //toUpdate.setMinQuantity(20);
        //toUpdate.setQuick_access(true);

        //ingredientsDAO.updateContact(toUpdate);

        //Ingredient updatedIngredient = ingredientsDAO.getById(toUpdate.getId());
        //assertNotNull(updatedIngredient);
        //assertEquals("Updated Ingredient", updatedIngredient.getIngredient());
        //assertEquals(200, updatedIngredient.getQuantity());
        //assertEquals(20, updatedIngredient.getMinQuantity());
        //assertTrue(updatedIngredient.isQuick_access());
    //}

    /**
     * Tests if an ingredient can be deleted from the database and verifies if the table is empty after deletion
     */
    @Test
    void testDeleteIngredient() {
        Ingredient ingredient = new Ingredient("Ingredient to Delete", 100, 10, false);
        ingredientsDAO.Insert(ingredient);

        List<Ingredient> ingredients = ingredientsDAO.getAll();
        assertEquals(1, ingredients.size());

        ingredientsDAO.delete(ingredients.get(0));

        ingredients = ingredientsDAO.getAll();
        assertTrue(ingredients.isEmpty());
    }
}