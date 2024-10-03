package org.example.cab302_project;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class EditIngredientController {

    @FXML
    private TextField ingredientNameField;

    @FXML
    private TextField quantityField;

    @FXML
    private TextField minQuantityField;

    @FXML
    private CheckBox quickAccessCheckBox;

    @FXML
    private Button saveButton;

    @FXML
    private Button cancelButton;

    private IngredientsDAO ingredientsDAO;
    private Ingredient ingredient;
    private ManageIngredientsController manageIngredientsController;

    // Initializes the IngredientsDAO
    public void initialize() {
        ingredientsDAO = new IngredientsDAO();
    }

    // Sets the ingredient to be edited and populates the fields
    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
        populateFields();
    }

    // Sets the parent controller
    public void setManageIngredientsController(ManageIngredientsController controller) {
        this.manageIngredientsController = controller;
    }

    // Fills the form fields with the ingredient's current data
    private void populateFields() {
        ingredientNameField.setText(ingredient.getIngredient());
        quantityField.setText(String.valueOf(ingredient.getQuantity()));
        minQuantityField.setText(String.valueOf(ingredient.getMinQuantity()));
        quickAccessCheckBox.setSelected(ingredient.isQuick_access());
    }

    // Saves the changes made to the ingredient
    @FXML
    private void handleSave() {
        try {
            ingredient.setName(ingredientNameField.getText());
            ingredient.setQuantity(Integer.parseInt(quantityField.getText()));
            ingredient.setMinQuantity(Integer.parseInt(minQuantityField.getText()));
            ingredient.setQuick_access(quickAccessCheckBox.isSelected());

            ingredientsDAO.update(ingredient);
            manageIngredientsController.loadIngredients();
            manageIngredientsController.loadQuickAccessIngredients();
            closeWindow();
        } catch (NumberFormatException e) {
            showAlert("Invalid Input", "Please enter valid numbers for Quantity and Min Quantity.");
        } catch (Exception e) {
            showAlert("Error", "An error occurred while saving the ingredient: " + e.getMessage());
        }
    }

    // Cancels the edit operation
    @FXML
    private void handleCancel() {
        closeWindow();
    }

    // Closes the edit ingredient window
    private void closeWindow() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    // Displays alert messages to the user
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}