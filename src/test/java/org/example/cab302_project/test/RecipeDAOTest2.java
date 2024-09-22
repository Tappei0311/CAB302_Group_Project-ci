package org.example.cab302_project;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

class RecipeDAOTest2 {
    private static Connection testConnection;
    private RecipeDAO recipeDAO;
    private IngredientsDAO ingredientsDAO;

    @BeforeAll
    static void setUpClass() throws SQLException {
        testConnection = DriverManager.getConnection("jdbc:sqlite:test_database.db");
        DatabaseConnection.setTestInstance(testConnection);
    }

    @BeforeEach
    void setUp() throws SQLException {
        recipeDAO = new RecipeDAO();
        ingredientsDAO = new IngredientsDAO();
        recipeDAO.createRecipeTable();
        recipeDAO.createRecipeIngredientTable();
        ingredientsDAO.createTable();
        clearAllTables();
    }

    @AfterEach
    void tearDown() throws SQLException {
        clearAllTables();
    }

    @AfterAll
    static void tearDownClass() throws SQLException {
        testConnection.close();
    }

    private void clearAllTables() throws SQLException {
        testConnection.createStatement().execute("DELETE FROM Recipe");
        testConnection.createStatement().execute("DELETE FROM RecipeIngredients");
        testConnection.createStatement().execute("DELETE FROM Ingredients");
    }

    @Test
    void testInsertRecipe() {
        Recipe recipe = new Recipe("Spaghetti Carbonara");
        int id = recipeDAO.InsertRecipe(recipe);
        assertTrue(id > 0, "Spaghetti Carbonara recipe should be inserted with a valid ID");

        // Verify the recipe was actually inserted
        List<Recipe> recipes = recipeDAO.getAll();
        assertEquals(1, recipes.size(), "There should be one recipe in the database");
        assertEquals("Spaghetti Carbonara", recipes.get(0).getName(), "The inserted recipe should be Spaghetti Carbonara");
        assertEquals(id, recipes.get(0).getId(), "The ID should match the one returned by InsertRecipe");
    }

    @Test
    void testGetAllRecipes() {
        Recipe recipe1 = new Recipe("Chicken Parmesan");
        Recipe recipe2 = new Recipe("Vegetable Stir Fry");
        int id1 = recipeDAO.InsertRecipe(recipe1);
        int id2 = recipeDAO.InsertRecipe(recipe2);

        List<Recipe> recipes = recipeDAO.getAll();
        assertEquals(2, recipes.size(), "Should retrieve both inserted recipes");
        assertTrue(recipes.stream().anyMatch(r -> r.getName().equals("Chicken Parmesan") && r.getId() == id1), "Should contain Chicken Parmesan with correct ID");
        assertTrue(recipes.stream().anyMatch(r -> r.getName().equals("Vegetable Stir Fry") && r.getId() == id2), "Should contain Vegetable Stir Fry with correct ID");
    }

    @Test
    void testDatabaseIsEmptyAtStart() {
        List<Recipe> recipes = recipeDAO.getAll();
        List<Ingredient> ingredients = ingredientsDAO.getAll();

        assertTrue(recipes.isEmpty(), "Recipe table should be empty at start");
        assertTrue(ingredients.isEmpty(), "Ingredients table should be empty at start");
    }
}