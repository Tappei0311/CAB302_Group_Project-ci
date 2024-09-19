package org.example.cab302_project;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class ManageIngredientsController {

    public VBox quickAccessSection;

    @FXML
    private Button backButton;

    @FXML
    private Button addIngredientButton;

    @FXML
    private TableView<Ingredient> ingredientsTable;

    @FXML
    private TableColumn<Ingredient, Boolean> checkboxColumn;

    @FXML
    private TableColumn<Ingredient, String> ingredientColumn;

    public static ObservableList<Ingredient> ingredientList = FXCollections.observableArrayList();

    private IngredientsDAO ingredientsDAO;

    public ManageIngredientsController() {
        ingredientsDAO = new IngredientsDAO();
    }

    @FXML
    public void initialize() {
        // ingredientColumn
        ingredientColumn.setCellValueFactory(new PropertyValueFactory<>("ingredient"));

        // checkboxColumn
        checkboxColumn.setCellFactory(tc -> new CheckBoxTableCell<>());

        loadIngredients(); // Load ingredients list
        ingredientsTable.setItems(ingredientList); // Connect TableView to the list

        ingredientsTable.refresh();
    }

    // Load ingredients list from the database
    private void loadIngredients() {
        try {
            ingredientList.clear();
            ingredientList.addAll(ingredientsDAO.getAll());
        } catch (Exception e) {
            showError("Error while loading ingredients", e.getMessage());
        }
    }

    // Method to dynamically load Quick Access items
    private void loadQuickAccessIngredients() {
        VBox quickAccessVBox = new VBox(10);
        List<Ingredient> quickAccessIngredients = ingredientsDAO.getQuickAccessIngredients();

        for (Ingredient ingredient : quickAccessIngredients) {
            Label ingredientLabel = new Label(ingredient.getIngredient());
            ingredientLabel.setStyle("-fx-text-fill: white;");

            HBox buttonBox = new HBox(5);
            Button subtractButton = new Button("-");
            Button addButton = new Button("+");

            subtractButton.setStyle("-fx-background-color: #ff5c5c; -fx-text-fill: white;");
            addButton.setStyle("-fx-background-color: #00d970; -fx-text-fill: white;");

            // subtract
            subtractButton.setOnAction(event -> {
                updateIngredientQuantity(ingredient, -1);
            });

            // add
            addButton.setOnAction(event -> {
                updateIngredientQuantity(ingredient, 1);
            });

            buttonBox.getChildren().addAll(subtractButton, addButton);
            quickAccessVBox.getChildren().addAll(ingredientLabel, buttonBox);
        }

        quickAccessSection.getChildren().setAll(quickAccessVBox); // Add to section dynamically
    }

    // Update ingredient quantity
    private void updateIngredientQuantity(Ingredient ingredient, int delta) {

        int newQuantity = ingredient.getQuantity() + delta;
        if (newQuantity < 0) {
            newQuantity = 0;
        }
        ingredient.setQuantity(newQuantity);

        // update database
        ingredientsDAO.update(ingredient);

        updateTableView(ingredient);
    }

    // Update table
    private void updateTableView(Ingredient updatedIngredient) {

        for (Ingredient ingredient : ingredientList) {
            if (ingredient.getId() == updatedIngredient.getId()) {
                ingredient.setQuantity(updatedIngredient.getQuantity());
                break;
            }
        }
        // refresh table
        ingredientsTable.refresh();
    }

    // Retrieve and use ingredient information based on a specific ingredient's ID
    public void viewIngredientDetails(int ingredientId) {
        Ingredient ingredient = ingredientsDAO.getById(ingredientId);

        if (ingredient != null) {
            // Display ingredient information or show it in the UI TBD
            System.out.println("Ingredient: " + ingredient.getIngredient());
            System.out.println("Quantity: " + ingredient.getQuantity());
            System.out.println("Min Quantity: " + ingredient.getMinQuantity());
        } else {
            System.out.println("Ingredient not found.");
        }
    }

    // Delete selected ingredient
    public void deleteSelectedIngredient(int id) {
        Ingredient selectedIngredient = ingredientsTable.getSelectionModel().getSelectedItem();

        if (selectedIngredient != null) {
            ingredientsDAO.delete(selectedIngredient);
            System.out.println("Ingredient with ID" + selectedIngredient.getId() + "deleted.");

            // refresh table
            loadAllIngredients();
        } else {
            showError("No selection", "Please select an ingredient to delete.");
        }

    }

    @FXML
    protected void handleDeleteSelected() {

        Ingredient selectedIngredient = ingredientsTable.getSelectionModel().getSelectedItem();

        if (selectedIngredient != null) {
            // Call the delete method using the ingredient ID
            deleteSelectedIngredient(selectedIngredient.getId());
        } else {
            showError("No selection", "Please select an ingredient to delete.");
        }
    }

    // When the program exits or database operations are completed
    public void closeDatabaseConnection() {
        ingredientsDAO.close();
        System.out.println("Database connection closed.");
    }

    private void loadAllIngredients() {
        ingredientList.clear();
        ingredientList.addAll(ingredientsDAO.getAll());
        ingredientsTable.refresh();
    }


    @FXML
    protected void backButton() throws IOException {
        Stage stage = (Stage) backButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(IngredientTrackerApplication.class.getResource("menu-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), IngredientTrackerApplication.WIDTH, IngredientTrackerApplication.HEIGHT);

        // Add stylesheet to the new scene
        scene.getStylesheets().add(Objects.requireNonNull(IngredientTrackerApplication.class.getResource("FormStyles.css")).toExternalForm());
        stage.setTitle("Ingredient Tracker");
        stage.setScene(scene);
    }

    @FXML
    protected void addIngredientButton() throws IOException {
        Stage stage = (Stage) addIngredientButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(IngredientTrackerApplication.class.getResource("new-ingredient-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), IngredientTrackerApplication.WIDTH, IngredientTrackerApplication.HEIGHT);

        // Add stylesheet to the new scene
        scene.getStylesheets().add(Objects.requireNonNull(IngredientTrackerApplication.class.getResource("FormStyles.css")).toExternalForm());

        stage.setScene(scene);
    }

    // Show error message
    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}


