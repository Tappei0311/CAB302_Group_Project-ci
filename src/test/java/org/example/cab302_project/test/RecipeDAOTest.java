package org.example.cab302_project;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

class RecipeDAOTest {
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
    void testDeleteRecipe() {
        Recipe recipe = new Recipe("Chocolate Chip Cookies");
        int id = recipeDAO.InsertRecipe(recipe);
        recipe.setId(id);

        boolean deleted = recipeDAO.deleteRecipe(recipe);
        assertTrue(deleted, "Chocolate Chip Cookies recipe should be deleted successfully");

        List<Recipe> recipes = recipeDAO.getAll();
        assertTrue(recipes.isEmpty(), "No recipes should remain after deletion");
    }

    @Test
    void testInsertIngredient() {
        int initialCount = ingredientsDAO.getAll().size();
        Ingredient ingredient = new Ingredient("Fresh Basil", 50, 10, true);
        ingredientsDAO.Insert(ingredient);

        List<Ingredient> ingredients = ingredientsDAO.getAll();
        assertEquals(initialCount + 1, ingredients.size(), "Should insert Fresh Basil");
        Ingredient insertedIngredient = ingredients.stream()
                .filter(i -> i.getIngredient().equals("Fresh Basil"))
                .findFirst()
                .orElse(null);
        assertNotNull(insertedIngredient, "Should find Fresh Basil in the ingredients");
        assertEquals(50, insertedIngredient.getQuantity(), "Quantity should match");
        assertEquals(10, insertedIngredient.getMinQuantity(), "Min Quantity should match");
        assertTrue(insertedIngredient.isQuick_access(), "Quick access should be true");
    }

    @Test
    void testLinkRecipeWithIngredient() {
        Recipe recipe = new Recipe("Margherita Pizza");
        int recipeId = recipeDAO.InsertRecipe(recipe);

        Ingredient ingredient = new Ingredient("Mozzarella Cheese", 200, 50, false);
        ingredientsDAO.Insert(ingredient);
        List<Ingredient> ingredients = ingredientsDAO.getAll();
        Ingredient insertedIngredient = ingredients.stream()
                .filter(i -> i.getIngredient().equals("Mozzarella Cheese"))
                .findFirst()
                .orElseThrow();

        RecipieIngredients ri = new RecipieIngredients(recipeId, insertedIngredient, 100);
        recipeDAO.InsertRecipeIngredient(ri);

        List<RecipieIngredients> linkedIngredients = recipeDAO.getIngredientsForRecipe(recipeId);
        assertEquals(1, linkedIngredients.size(), "Should link Mozzarella Cheese to Margherita Pizza");
        RecipieIngredients linkedIngredient = linkedIngredients.get(0);
        assertEquals(insertedIngredient.getId(), linkedIngredient.getIngredient().getId(), "Linked Mozzarella Cheese ID should match");
        assertEquals(100, linkedIngredient.getAmount(), "Amount should be 100");
    }

    @Test
    void testGetIngredientsForRecipe() {
        Recipe recipe = new Recipe("Caesar Salad");
        int recipeId = recipeDAO.InsertRecipe(recipe);

        Ingredient ingredient1 = new Ingredient("Romaine Lettuce", 300, 100, false);
        Ingredient ingredient2 = new Ingredient("Croutons", 100, 50, true);
        ingredientsDAO.Insert(ingredient1);
        ingredientsDAO.Insert(ingredient2);

        List<Ingredient> ingredients = ingredientsDAO.getAll();
        Ingredient lettuce = ingredients.stream().filter(i -> i.getIngredient().equals("Romaine Lettuce")).findFirst().orElseThrow();
        Ingredient croutons = ingredients.stream().filter(i -> i.getIngredient().equals("Croutons")).findFirst().orElseThrow();

        recipeDAO.InsertRecipeIngredient(new RecipieIngredients(recipeId, lettuce, 200));
        recipeDAO.InsertRecipeIngredient(new RecipieIngredients(recipeId, croutons, 50));

        List<RecipieIngredients> recipeIngredients = recipeDAO.getIngredientsForRecipe(recipeId);
        assertEquals(2, recipeIngredients.size(), "Caesar Salad should have two ingredients");
        assertTrue(recipeIngredients.stream().anyMatch(ri -> ri.getIngredient().getIngredient().equals("Romaine Lettuce") && ri.getAmount() == 200),
                "Caesar Salad should contain Romaine Lettuce with amount 200");
        assertTrue(recipeIngredients.stream().anyMatch(ri -> ri.getIngredient().getIngredient().equals("Croutons") && ri.getAmount() == 50),
                "Caesar Salad should contain Croutons with amount 50");
    }

    @Test
    void testDatabaseIsEmptyAtStart() {
        List<Recipe> recipes = recipeDAO.getAll();
        List<Ingredient> ingredients = ingredientsDAO.getAll();

        assertTrue(recipes.isEmpty(), "Recipe table should be empty at start");
        assertTrue(ingredients.isEmpty(), "Ingredients table should be empty at start");
    }
}