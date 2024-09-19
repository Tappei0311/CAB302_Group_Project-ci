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

    public NewIngredientController() {
        ingredientsDAO = new IngredientsDAO();
    }

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

    @FXML
    public void addIngredient(ActionEvent actionEvent) {
        String ingredientNameValue = ingredientName.getText().trim();
        String quantityString = quantity.getText().trim();
        String minQuantityString = minQuantity.getText().trim();
        boolean isQuickAccess = quickAccess.isSelected();

        if (ingredientNameValue.isEmpty() || quantityString.isEmpty() || minQuantityString.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "All fields must be filled.");
            return;
        }

        try {
            int quantityValue = Integer.parseInt(quantityString);
            int minQuantityValue = Integer.parseInt(minQuantityString);

            Ingredient newIngredient = new Ingredient(ingredientNameValue, quantityValue, minQuantityValue, isQuickAccess);
            ingredientsDAO.Insert(newIngredient);

            ManageIngredientsController.ingredientList.add(newIngredient);

            showAlert(Alert.AlertType.INFORMATION, "Success", "Ingredient added successfully.");
            clearFields();

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Quantity and Minimum Quantity must be valid numbers.");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while adding the ingredient: " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void clearFields() {
        ingredientName.clear();
        quantity.clear();
        minQuantity.clear();
        quickAccess.setSelected(false);
    }

    @FXML
    protected void backButton() throws IOException {
        Stage stage = (Stage) backButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(IngredientTrackerApplication.class.getResource("manage-ingredients-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), IngredientTrackerApplication.WIDTH, IngredientTrackerApplication.HEIGHT);

        scene.getStylesheets().add(Objects.requireNonNull(IngredientTrackerApplication.class.getResource("FormStyles.css")).toExternalForm());
        stage.setTitle("Ingredient Tracker");
        stage.setScene(scene);
    }
}