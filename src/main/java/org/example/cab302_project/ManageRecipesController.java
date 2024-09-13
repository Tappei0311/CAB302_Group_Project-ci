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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ManageRecipesController {

    @FXML
    private ListView<Recipe> recipeList;  // Store Recipe objects

    @FXML
    private ListView<String> ingredientListView;  // Display ingredients when recipe is clicked

    @FXML
    private Button newRecipeButton;

    @FXML
    private Button updateIngredientButton;


    @FXML
    private Button editRecipeButton;

    @FXML
    private Button deleteRecipeButton;

    private static ManageRecipesController instance;  // Singleton instance

    // File to store recipes
    private static final String RECIPE_FILE = "recipes.csv";

    private static ObservableList<Recipe> recipes = FXCollections.observableArrayList();

    public ManageRecipesController() {
        if (instance == null) {
            instance = this;
        }
    }

    public static ManageRecipesController getInstance() {
        return instance;
    }

    @FXML
    public void initialize() {
        if (recipes.isEmpty()) {
            loadRecipesFromFile();
        }
        recipeList.setItems(recipes);

        recipeList.getSelectionModel().selectedItemProperty().addListener((obs, oldRecipe, newRecipe) -> {
            if (newRecipe != null) {
                editRecipeButton.setDisable(false);
                deleteRecipeButton.setDisable(false);
                ingredientListView.setItems(newRecipe.getIngredients());
            } else {
                editRecipeButton.setDisable(true);
                deleteRecipeButton.setDisable(true);
            }
        });
    }

    // Handle "New Recipe" button click
    public void handleNewRecipeButtonClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/cab302_project/new-recipe.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleDeleteRecipeClick(ActionEvent event) {
        Recipe selectedRecipe = recipeList.getSelectionModel().getSelectedItem();
        if (selectedRecipe != null) {
            recipes.remove(selectedRecipe);
            saveRecipesToFile();
        }
    }

    @FXML
    public void handleEditRecipeClick(ActionEvent event) {
        Recipe selectedRecipe = recipeList.getSelectionModel().getSelectedItem();
        if (selectedRecipe != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/cab302_project/edit-recipe.fxml"));
                Parent root = loader.load();

                EditRecipeController editRecipeController = loader.getController();
                editRecipeController.setRecipe(selectedRecipe);

                Stage stage = (Stage) editRecipeButton.getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setMinWidth(600);
                stage.setMinHeight(400);
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void addRecipe(Recipe recipe) {
        if (recipe != null && !recipe.getName().isEmpty()) {
            recipes.add(recipe);
            saveRecipesToFile();
        }
    }

    public void saveRecipesToFile() {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(RECIPE_FILE))) {
            for (Recipe recipe : recipes) {
                StringBuilder line = new StringBuilder(recipe.getName());
                for (String ingredient : recipe.getIngredients()) {
                    line.append(",").append(ingredient);
                }
                writer.write(line.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadRecipesFromFile() {
        Path path = Paths.get(RECIPE_FILE);
        if (Files.exists(path)) {
            try (BufferedReader reader = Files.newBufferedReader(path)) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length > 0) {
                        Recipe recipe = new Recipe(parts[0]);
                        for (int i = 1; i < parts.length; i++) {
                            recipe.addIngredient(parts[i]);
                        }
                        recipes.add(recipe);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
