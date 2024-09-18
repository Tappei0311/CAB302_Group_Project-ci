package org.example.cab302_project;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;

public class NewRecipeController {

    @FXML
    private ListView<String> ingredientList;

    @FXML
    private TextField recipeName;

    @FXML
    private TextField ingredientInput;

    @FXML
    private ComboBox<String> quantityComboBox;

    @FXML
    private Button addIngredientButton;  // Add Ingredient Button
    @FXML
    private Button updateIngredientButton;  // Update Ingredient Button

    private ObservableList<String> ingredients = FXCollections.observableArrayList();
    private int editingIngredientIndex = -1;  // Track which ingredient is being edited

    @FXML
    public void initialize() {
        // Populate the quantity ComboBox with values 1-5
        quantityComboBox.setItems(FXCollections.observableArrayList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10"));

        // Set the ListView to display ingredients with delete functionality
        setupIngredientListView();
        ingredientList.setItems(ingredients);

        // Initially, disable the Update button
        updateIngredientButton.setDisable(true);
    }

    private void setupIngredientListView() {
        ingredientList.setCellFactory(new Callback<>() {
            @Override
            public ListCell<String> call(ListView<String> listView) {
                return new ListCell<>() {
                    private final Button deleteButton = new Button("Delete");
                    private final Button editButton = new Button("Edit");

                    {
                        deleteButton.setOnAction(event -> {
                            String ingredient = getItem();
                            ingredients.remove(ingredient);  // Remove ingredient from the list
                            ingredientList.setItems(ingredients);  // Refresh the list
                        });

                        editButton.setOnAction(event -> {
                            // Enter edit mode for this ingredient
                            editingIngredientIndex = getIndex();
                            String[] parts = getItem().split(" \\(Qty: ");
                            ingredientInput.setText(parts[0]);
                            quantityComboBox.setValue(parts[1].replace(")", ""));

                            // Enable the Update button and disable the Add button
                            updateIngredientButton.setDisable(false);
                            addIngredientButton.setDisable(true);
                        });
                    }

                    @Override
                    protected void updateItem(String ingredient, boolean empty) {
                        super.updateItem(ingredient, empty);
                        if (empty || ingredient == null) {
                            setText(null);
                            setGraphic(null);
                        } else {
                            setText(ingredient);
                            setGraphic(new javafx.scene.layout.HBox(deleteButton, editButton));
                        }
                    }
                };
            }
        });
    }

    @FXML
    public void handleAddIngredientClick(ActionEvent actionEvent) {
        String ingredientText = ingredientInput.getText();
        String quantity = quantityComboBox.getValue();

        if (ingredientText != null && !ingredientText.trim().isEmpty() && quantity != null) {
            String ingredient = ingredientText + " (Qty: " + quantity + ")";
            ingredients.add(ingredient);
            ingredientList.setItems(ingredients);
            ingredientInput.clear();
        } else {
            System.out.println("Please provide both ingredient and quantity.");
        }
    }

    @FXML
    public void handleUpdateIngredientClick(ActionEvent actionEvent) {
        String updatedIngredient = ingredientInput.getText() + " (Qty: " + quantityComboBox.getValue() + ")";

        if (editingIngredientIndex >= 0 && !updatedIngredient.isEmpty()) {
            ingredients.set(editingIngredientIndex, updatedIngredient);  // Update ingredient in the list
            ingredientList.setItems(ingredients);
            ingredientInput.clear();  // Reset input fields
            editingIngredientIndex = -1;  // Reset edit index

            // Re-enable the Add button and disable the Update button
            addIngredientButton.setDisable(false);
            updateIngredientButton.setDisable(true);
        } else {
            System.out.println("Invalid ingredient or quantity.");
        }
    }

    @FXML
    public void handleCreateRecipeClick(ActionEvent actionEvent) throws IOException {
        String recipeText = recipeName.getText();

        if (recipeText != null && !recipeText.trim().isEmpty() && !ingredients.isEmpty()) {
            Recipe newRecipe = new Recipe(recipeText);
//            newRecipe.getIngredients().addAll(ingredients);

            ManageRecipesController.getInstance().addRecipe(newRecipe);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/cab302_project/manage-recipes.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) recipeName.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } else {
            System.out.println("Recipe name and ingredients must not be empty.");
        }
    }
}