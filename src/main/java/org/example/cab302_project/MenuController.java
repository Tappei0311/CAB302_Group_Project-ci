package org.example.cab302_project;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class MenuController {


    @FXML
    private Button shoppingButton;

    @FXML
    private Button recommendRecipeButton;

    @FXML
    private Button manageIngredientsButton;

    @FXML
    private Button manageRecipesButton;



    @FXML
    protected void recommendRecipeButton() throws IOException {

        Stage stage = (Stage) recommendRecipeButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(IngredientTrackerApplication.class.getResource("recommend-recipe-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), IngredientTrackerApplication.WIDTH, IngredientTrackerApplication.HEIGHT);
        stage.setTitle("Recommend Recipe");

        // Add stylesheet to the new scene
        scene.getStylesheets().add(Objects.requireNonNull(IngredientTrackerApplication.class.getResource("FormStyles.css")).toExternalForm());

        stage.setScene(scene);
    }

    @FXML
    protected void manageIngredientsButton() throws IOException {
        Stage stage = (Stage) manageIngredientsButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(IngredientTrackerApplication.class.getResource("manage-ingredients-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), IngredientTrackerApplication.WIDTH, IngredientTrackerApplication.HEIGHT);
        stage.setTitle("Manage Ingredients");

        // Add stylesheet to the new scene
        scene.getStylesheets().add(Objects.requireNonNull(IngredientTrackerApplication.class.getResource("FormStyles.css")).toExternalForm());

        stage.setScene(scene);
    }


    @FXML
    protected void ShoppingListButton() throws IOException {
        Stage stage = (Stage) shoppingButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(IngredientTrackerApplication.class.getResource("shopping-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), IngredientTrackerApplication.WIDTH, IngredientTrackerApplication.HEIGHT);
        stage.setTitle("Shopping List");


        // Add stylesheet to the new scene
        scene.getStylesheets().add(Objects.requireNonNull(IngredientTrackerApplication.class.getResource("FormStyles.css")).toExternalForm());

        stage.setScene(scene);
    }

    @FXML
    protected void manageRecipesButton() throws IOException {
        Stage stage = (Stage) manageRecipesButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(IngredientTrackerApplication.class.getResource("manage-recipes.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 650, 400);
        stage.setTitle("Manage Recipes");

        // Add stylesheet to the new scene
        scene.getStylesheets().add(Objects.requireNonNull(IngredientTrackerApplication.class.getResource("FormStyles.css")).toExternalForm());

        stage.setScene(scene);
    }






}