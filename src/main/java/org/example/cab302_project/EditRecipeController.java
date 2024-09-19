package org.example.cab302_project;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.util.Callback;

import java.io.IOException;
import java.util.List;

public class EditRecipeController {
    @FXML
    private ListView<RecipieIngredients> ingredientList;

    @FXML
    private TextField recipeName;

    @FXML
    private ComboBox<Ingredient> ingredientComboBox;

    @FXML
    private ComboBox<String> quantityComboBox;

    @FXML
    private Button addIngredientButton;

    @FXML
    private Button updateIngredientButton;

    private Recipe recipe;
    private int editingIngredientIndex = -1;

    private RecipeDAO recipeDAO;
    private IngredientsDAO ingredientsDAO;

    public EditRecipeController() {
        recipeDAO = new RecipeDAO();
        ingredientsDAO = new IngredientsDAO();
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
        recipeName.setText(recipe.getName());
        loadRecipeIngredients();
    }

    @FXML
    public void initialize() {
        quantityComboBox.setItems(FXCollections.observableArrayList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10"));
        loadIngredients();
    }

    private void loadIngredients() {
        List<Ingredient> allIngredients = ingredientsDAO.getAll();
        ingredientComboBox.setItems(FXCollections.observableArrayList(allIngredients));
    }

    private void loadRecipeIngredients() {
        List<RecipieIngredients> recipeIngredients = recipeDAO.getIngredientsForRecipe(recipe.getId());
        ingredientList.setItems(FXCollections.observableArrayList(recipeIngredients));
    }

    private void setupIngredientListView() {
        ingredientList.setCellFactory(new Callback<ListView<RecipieIngredients>, ListCell<RecipieIngredients>>() {
            @Override
            public ListCell<RecipieIngredients> call(ListView<RecipieIngredients> param) {
                return new ListCell<RecipieIngredients>() {
                    private final Button deleteButton = new Button("Delete");
                    private final Button editButton = new Button("Edit");

                    {
                        deleteButton.setOnAction(event -> {
                            RecipieIngredients ingredient = getItem();
                            recipeDAO.deleteRecipeIngredient(ingredient);
                            loadRecipeIngredients();
                        });

                        editButton.setOnAction(event -> {
                            editingIngredientIndex = getIndex();
                            RecipieIngredients ingredient = getItem();
                            ingredientComboBox.setValue(ingredient.getIngredient());
                            quantityComboBox.setValue(String.valueOf(ingredient.getAmount()));
                            updateIngredientButton.setDisable(false);
                            addIngredientButton.setDisable(true);
                        });
                    }

                    @Override
                    protected void updateItem(RecipieIngredients ingredient, boolean empty) {
                        super.updateItem(ingredient, empty);
                        if (empty || ingredient == null) {
                            setText(null);
                            setGraphic(null);
                        } else {
                            setText(ingredient.getIngredient().getIngredient() + " (Qty: " + ingredient.getAmount() + ")");
                            setGraphic(new javafx.scene.layout.HBox(deleteButton, editButton));
                        }
                    }
                };
            }
        });
    }

    @FXML
    public void handleAddIngredient(ActionEvent event) {
        Ingredient selectedIngredient = ingredientComboBox.getValue();
        String quantity = quantityComboBox.getValue();

        if (selectedIngredient != null && quantity != null) {
            RecipieIngredients newIngredient = new RecipieIngredients(recipe.getId(), selectedIngredient, Integer.parseInt(quantity));
            recipeDAO.InsertRecipeIngredient(newIngredient);
            loadRecipeIngredients();
            ingredientComboBox.setValue(null);
            quantityComboBox.setValue(null);
        }
    }

    @FXML
    public void handleSaveRecipe(ActionEvent event) throws IOException {
        recipe.setName(recipeName.getText());
        // Update recipe name in database
        // This method needs to be implemented in RecipeDAO
        // recipeDAO.updateRecipe(recipe);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/cab302_project/manage-recipes.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) recipeName.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void handleUpdateIngredient(ActionEvent actionEvent) {
        Ingredient updatedIngredient = ingredientComboBox.getValue();
        String updatedQuantity = quantityComboBox.getValue();

        if (editingIngredientIndex >= 0 && updatedIngredient != null && updatedQuantity != null) {
            RecipieIngredients ingredient = ingredientList.getItems().get(editingIngredientIndex);
            ingredient.setIngredient(updatedIngredient);
            ingredient.setAmount(Integer.parseInt(updatedQuantity));

            // Update ingredient in database
            // This method needs to be implemented in RecipeDAO
            // recipeDAO.updateRecipeIngredient(ingredient);

            loadRecipeIngredients();
            ingredientComboBox.setValue(null);
            quantityComboBox.setValue(null);
            editingIngredientIndex = -1;
            addIngredientButton.setDisable(false);
            updateIngredientButton.setDisable(true);
        }
    }
}