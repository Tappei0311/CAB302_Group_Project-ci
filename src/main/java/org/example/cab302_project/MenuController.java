package org.example.cab302_project;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class MenuController {

    @FXML
    private Button shoppingButton;

    @FXML
    private Button recommendRecipeButton;


    @FXML
    protected void recommendRecipeButton() throws IOException {
        Stage stage = (Stage) recommendRecipeButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(IngredientTrackerApplication.class.getResource("recommend-recipe-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), IngredientTrackerApplication.WIDTH, IngredientTrackerApplication.HEIGHT);
        stage.setScene(scene);
    }


    @FXML
    protected void ShoppingListButton() throws IOException {
        Stage stage = (Stage) shoppingButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(IngredientTrackerApplication.class.getResource("shopping-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), IngredientTrackerApplication.WIDTH, IngredientTrackerApplication.HEIGHT);
        stage.setScene(scene);
    }



}