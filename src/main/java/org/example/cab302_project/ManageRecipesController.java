package org.example.cab302_project;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.event.ActionEvent;
import java.io.*;
import java.util.List;
import java.util.Objects;

public class ManageRecipesController {

    @FXML
    private ListView<Recipe> recipeList;

    @FXML
    private ListView<String> ingredientListView;

    @FXML
    private Button newRecipeButton;

    @FXML
    private Button editRecipeButton;

    @FXML
    private Button deleteRecipeButton;

    @FXML
    private Button backButton;

    private RecipeDAO recipeDAO;
    private IngredientsDAO ingredientsDAO;

    public ManageRecipesController() {
        recipeDAO = new RecipeDAO();
        ingredientsDAO = new IngredientsDAO();
    }

    @FXML
    public void initialize() {
        loadRecipes();
        recipeList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                System.out.println("Selected recipe: " + newValue.getName() + ", ID: " + newValue.getId());
                loadIngredientsForRecipe(newValue);
                editRecipeButton.setDisable(false);
                deleteRecipeButton.setDisable(false);
            } else {
                ingredientListView.getItems().clear();
                editRecipeButton.setDisable(true);
                deleteRecipeButton.setDisable(true);
            }
        });
    }

    private void loadRecipes() {
        List<Recipe> recipes = recipeDAO.getAll();
        ObservableList<Recipe> observableRecipes = FXCollections.observableArrayList(recipes);
        recipeList.setItems(observableRecipes);
        System.out.println("Loaded " + recipes.size() + " recipes");
        for (Recipe recipe : recipes) {
            System.out.println("Loaded Recipe: " + recipe.getName() + ", ID: " + recipe.getId());
        }
    }

    private void loadIngredientsForRecipe(Recipe recipe) {
        List<RecipieIngredients> ingredients = recipeDAO.getIngredientsForRecipe(recipe.getId());
        ingredientListView.getItems().clear();
        for (RecipieIngredients ri : ingredients) {
            ingredientListView.getItems().add(ri.getIngredient().getIngredient() + " (Qty: " + ri.getAmount() + ")");
        }
        System.out.println("Loaded " + ingredients.size() + " ingredients for recipe: " + recipe.getName());
    }



    private void loadIngredients(Recipe recipe) {
        // This method needs to be implemented in RecipeDAO
        // List<RecipieIngredients> recipeIngredients = recipeDAO.getIngredientsForRecipe(recipe.getId());
        // ObservableList<String> ingredients = FXCollections.observableArrayList();
        // for (RecipieIngredients ri : recipeIngredients) {
        //     ingredients.add(ri.getIngredient().getIngredient() + " (Qty: " + ri.getAmount() + ")");
        // }
        // ingredientListView.setItems(ingredients);
    }

    @FXML
    public void handleNewRecipeButtonClick(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/cab302_project/new-recipe.fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 650, 420);
        stage.setTitle("New Recipes");
        // Add stylesheet to the new scene
        scene.getStylesheets().add(Objects.requireNonNull(IngredientTrackerApplication.class.getResource("FormStyles.css")).toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void handleEditRecipeClick(ActionEvent event) throws IOException {
        Recipe selectedRecipe = recipeList.getSelectionModel().getSelectedItem();
        if (selectedRecipe != null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/cab302_project/edit-recipe.fxml"));
            Parent root = loader.load();
            EditRecipeController controller = loader.getController();
            controller.setRecipe(selectedRecipe);

            Stage stage = (Stage) editRecipeButton.getScene().getWindow();
            Scene scene = new Scene(root, 650, 420);
            stage.setTitle("Edit Recipes");
            // Add stylesheet to the new scene
            scene.getStylesheets().add(Objects.requireNonNull(IngredientTrackerApplication.class.getResource("FormStyles.css")).toExternalForm());
            stage.setScene(scene);
            stage.show();
        }
    }

    @FXML
    public void handleDeleteRecipeClick(ActionEvent event) {
        Recipe selectedRecipe = recipeList.getSelectionModel().getSelectedItem();
        if (selectedRecipe != null) {
            System.out.println("Attempting to delete recipe: " + selectedRecipe.getName() + ", ID: " + selectedRecipe.getId());
            boolean deleted = recipeDAO.deleteRecipe(selectedRecipe);
            if (deleted) {
                System.out.println("Successfully deleted recipe: " + selectedRecipe.getName());
            } else {
                System.out.println("Failed to delete recipe: " + selectedRecipe.getName());
            }
            loadRecipes();
            ingredientListView.getItems().clear();
        }
    }

    @FXML
    protected void backButton() throws IOException {
        Stage stage = (Stage) backButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(IngredientTrackerApplication.class.getResource("menu-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), IngredientTrackerApplication.WIDTH, IngredientTrackerApplication.HEIGHT);
        scene.getStylesheets().add(IngredientTrackerApplication.class.getResource("FormStyles.css").toExternalForm());
        stage.setTitle("Ingredient Tracker");
        stage.setScene(scene);
    }
}