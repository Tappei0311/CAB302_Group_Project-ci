package org.example.cab302_project;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.util.Callback;

import java.io.IOException;

public class EditRecipeController {
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

    private Recipe recipe;
    private int editingIngredientIndex = -1;  // Track which ingredient is being edited

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
        recipeName.setText(recipe.getName());
        ingredientList.setItems(recipe.getIngredients());
        setupIngredientListView();

        // Initially, disable the Update button
        updateIngredientButton.setDisable(true);
    }

    @FXML
    public void initialize() {
        quantityComboBox.setItems(FXCollections.observableArrayList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10"));
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
                            recipe.getIngredients().remove(ingredient);
                            ingredientList.setItems(recipe.getIngredients());
                        });

                        editButton.setOnAction(event -> {
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
    public void handleAddIngredient(ActionEvent event) {
        String ingredient = ingredientInput.getText() + " (Qty: " + quantityComboBox.getValue() + ")";
        recipe.addIngredient(ingredient);
        ingredientList.setItems(recipe.getIngredients());
        ingredientInput.clear();
    }


    @FXML
    public void handleSaveRecipe(ActionEvent event) throws IOException {
        recipe.setName(recipeName.getText());
        ManageRecipesController.getInstance().saveRecipesToFile();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/ingredienttracker/manage-recipes.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) recipeName.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void handleUpdateIngredient(ActionEvent actionEvent) {

        String updatedIngredient = ingredientInput.getText() + " (Qty: " + quantityComboBox.getValue() + ")";

        if (editingIngredientIndex >= 0 && !updatedIngredient.isEmpty()) {
            recipe.getIngredients().set(editingIngredientIndex, updatedIngredient);  // Update ingredient in the recipe
            ingredientList.setItems(recipe.getIngredients());
            ingredientInput.clear();
            editingIngredientIndex = -1;

            // Re-enable the Add button and disable the Update button
            addIngredientButton.setDisable(false);
            updateIngredientButton.setDisable(true);
        }
    }
}


