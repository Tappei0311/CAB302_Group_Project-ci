package org.example.cab302_project;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
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

    @FXML
    public void initialize() {
        // allow integer to quantity
        quantity.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                // if the value doesn't integer, set back to old value
                quantity.setText(oldValue);
            }
        });

        minQuantity.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                // if the value doesn't integer, set back to old value
                minQuantity.setText(oldValue);
            }
        });
    }

    @FXML
    public void addIngredient(ActionEvent actionEvent){
        String IngredientName = ingredientName.getText();
        String Quantity_String = quantity.getText();
        String minthreshold_String = minQuantity.getText();
        boolean quickaccess = quickAccess.isSelected();

        if (ingredientName != null && Quantity_String != null && minthreshold_String != null){
            int Quantity_int = Integer.parseInt(Quantity_String);
            int minthreshold_int = Integer.parseInt(minthreshold_String);
            Ingredient newIngredient = new Ingredient(IngredientName, Quantity_int, minthreshold_int, quickaccess);
        }
    }





    @FXML
    protected void backButton() throws IOException {
        Stage stage = (Stage) backButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(IngredientTrackerApplication.class.getResource("manage-ingredients-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), IngredientTrackerApplication.WIDTH, IngredientTrackerApplication.HEIGHT);

        // Add stylesheet to the new scene
        scene.getStylesheets().add(Objects.requireNonNull(IngredientTrackerApplication.class.getResource("FormStyles.css")).toExternalForm());

        stage.setScene(scene);
    }

}
