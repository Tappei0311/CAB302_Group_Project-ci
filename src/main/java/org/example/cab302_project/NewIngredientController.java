package org.example.cab302_project;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class NewIngredientController {

    @FXML
    private TextField ingredientName;

    @FXML
    private TextField quantity;

    @FXML
    private TextField minQuantity;

    @FXML
    private CheckBox quickAccess;

    @FXML
    private Button backButton;

    @FXML
    private Button createButton;

    private IngredientsDAO ingredientsDAO;
    private ManageIngredientsController manageIngredientsController;
    private ManageRecipesController manageRecipesController;

    public NewIngredientController() {
        ingredientsDAO = new IngredientsDAO();
    }

    // Sets up input validation for quantity fields
    @FXML
    public void initialize() {
        // Existing input validation code
        quantity.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                quantity.setText(oldValue);
            }
        });

        minQuantity.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                minQuantity.setText(oldValue);
            }
        });
    }

    // Set the parent controllers
    public void setManageIngredientsController(ManageIngredientsController controller) {
        this.manageIngredientsController = controller;
    }
    //Set the parent controllers
    public void setManageRecipesController(ManageRecipesController controller) {
        this.manageRecipesController = controller;
    }

    // Handles the logic for adding a new ingredient
    @FXML
    public void addIngredient() {
        String ingredientNameValue = ingredientName.getText().trim();
        String quantityString = quantity.getText().trim();
        String minQuantityString = minQuantity.getText().trim();
        boolean isQuickAccess = quickAccess.isSelected();

        if (ingredientNameValue.isEmpty() || quantityString.isEmpty() || minQuantityString.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "All fields must be filled.");
            return;
        }

        if (ingredientsDAO.ingredientExists(ingredientNameValue)) {
            showAlert(Alert.AlertType.ERROR, "Error", "An ingredient with this name already exists.");
            return;
        }

        try {
            int quantityValue = Integer.parseInt(quantityString);
            int minQuantityValue = Integer.parseInt(minQuantityString);

            Ingredient newIngredient = new Ingredient(ingredientNameValue, quantityValue, minQuantityValue, isQuickAccess);
            ingredientsDAO.Insert(newIngredient);

            showAlert(Alert.AlertType.INFORMATION, "Success", "Ingredient added successfully.");

            if (manageIngredientsController != null) {
                manageIngredientsController.loadIngredients();
                manageIngredientsController.loadQuickAccessIngredients();
            }
            if (manageRecipesController != null) {
                manageRecipesController.refreshIngredients();
            }

            closeWindow();
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Quantity and Minimum Quantity must be valid numbers.");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while adding the ingredient: " + e.getMessage());
        }
    }

    // Displays alert messages to the user
    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    // Handle closing the add ingredient window
    @FXML
    protected void backButton() {
        closeWindow();
    }

    // Handle closing the add ingredient window
    private void closeWindow() {
        Stage stage = (Stage) createButton.getScene().getWindow();
        stage.close();
    }
}