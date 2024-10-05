package org.example.cab302_project;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
    private TableColumn<Ingredient, String> ingredientColumn;

    @FXML
    private TableColumn<Ingredient, Integer> quantityColumn;

    @FXML
    private TableColumn<Ingredient, Integer> minQuantityColumn;

    @FXML
    private TableColumn<Ingredient, Boolean> quickAccessColumn;

    @FXML
    private Button plusButton;

    @FXML
    private Button minusButton;


    @FXML
    private TableColumn<Ingredient, Boolean> checkboxColumn;

    public static ObservableList<Ingredient> ingredientList = FXCollections.observableArrayList();

    private IngredientsDAO ingredientsDAO;


    public ManageIngredientsController() {
        ingredientsDAO = new IngredientsDAO();
    }

    // Initialize method: Set up table columns and load data
    @FXML
    public void initialize() {
        setupTableColumns();
        loadIngredients();
        loadQuickAccessIngredients();
        setupButtonDisabling();
    }

    // Set up table columns with appropriate cell value factories
    private void setupTableColumns() {
        ingredientColumn.setCellValueFactory(new PropertyValueFactory<>("ingredient"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
    }

    private void setupButtonDisabling() {
        plusButton.setDisable(true);
        minusButton.setDisable(true);

        ingredientsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            boolean disableButtons = (newSelection == null);
            plusButton.setDisable(disableButtons);
            minusButton.setDisable(disableButtons);
        });
    }

    // Load all ingredients from the database and populate the table
    public void loadIngredients() {
        try {
            ingredientList.clear();
            List<Ingredient> ingredients = ingredientsDAO.getAll();
            System.out.println("Loaded " + ingredients.size() + " ingredients from database");
            ingredientList.addAll(ingredients);
            ingredientsTable.setItems(ingredientList);
            System.out.println("Table items set, size: " + ingredientsTable.getItems().size());
        } catch (Exception e) {
            System.err.println("Error loading ingredients: " + e.getMessage());
            e.printStackTrace();
            showError("Error while loading ingredients", e.getMessage());
        }
    }

    // Method to dynamically load Quick Access items
    public void loadQuickAccessIngredients() {
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

    // Handle the adding of a new ingredient
    @FXML
    protected void addIngredientButton() throws IOException {
        FXMLLoader loader = new FXMLLoader(IngredientTrackerApplication.class.getResource("new-ingredient-view.fxml"));
        Parent root = loader.load();

        NewIngredientController newIngredientController = loader.getController();
        newIngredientController.setManageIngredientsController(this);

        Stage stage = new Stage();
        Scene scene = new Scene(root, IngredientTrackerApplication.WIDTH, IngredientTrackerApplication.HEIGHT);
        scene.getStylesheets().add(IngredientTrackerApplication.class.getResource("FormStyles.css").toExternalForm());
        stage.setTitle("Add New Ingredient");
        stage.setScene(scene);
        stage.showAndWait();

        loadIngredients();
        loadQuickAccessIngredients();
    }

    // Handle the editing of a selected ingredient
    @FXML
    private void handleEditIngredient() {
        Ingredient selectedIngredient = ingredientsTable.getSelectionModel().getSelectedItem();
        if (selectedIngredient != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("edit-ingredient-view.fxml"));
                Parent root = loader.load();

                EditIngredientController editController = loader.getController();
                editController.setIngredient(selectedIngredient);
                editController.setManageIngredientsController(this);

                Stage stage = new Stage();
                stage.setTitle("Edit Ingredient");
                stage.setScene(new Scene(root));
                stage.showAndWait();

            } catch (IOException e) {
                showError("Error", "Could not open edit window: " + e.getMessage());
            }
        } else {
            showError("No Selection", "Please select an ingredient to edit.");
        }
    }

    // Update the quantity of an ingredient
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

    // Update the table view for a specific ingredient
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

    // Handle the deletion of a selected ingredient
    @FXML
    protected void handleDeleteSelected() {
        Ingredient selectedIngredient = ingredientsTable.getSelectionModel().getSelectedItem();
        if (selectedIngredient != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Ingredient");
            alert.setHeaderText("Are you sure you want to delete this ingredient?");
            alert.setContentText(selectedIngredient.getIngredient());

            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    boolean deleted = ingredientsDAO.delete(selectedIngredient);
                    if (deleted) {
                        ingredientList.remove(selectedIngredient);
                        loadIngredients(); // Refresh the table
                        loadQuickAccessIngredients(); // Refresh quick access section
                        showInfo("Ingredient Deleted", "The ingredient has been successfully deleted.");
                    } else {
                        showError("Delete Failed", "Failed to delete the ingredient. Please try again.");
                    }
                }
            });
        } else {
            showError("No Selection", "Please select an ingredient to delete.");
        }
    }

    @FXML
    private void handlePlusButton() {
        updateSelectedIngredientQuantity(1);
    }

    @FXML
    private void handleMinusButton() {
        updateSelectedIngredientQuantity(-1);
    }

    private void updateSelectedIngredientQuantity(int delta) {
        Ingredient selectedIngredient = ingredientsTable.getSelectionModel().getSelectedItem();
        if (selectedIngredient != null) {
            int newQuantity = Math.max(0, selectedIngredient.getQuantity() + delta);
            selectedIngredient.setQuantity(newQuantity);
            ingredientsDAO.update(selectedIngredient);
            ingredientsTable.refresh();
            loadQuickAccessIngredients(); // Refresh quick access section if needed
        }
    }

    // Display information message
    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Handle back button click
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

    // Show error message
    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void loadAllIngredients() {
        ingredientList.clear();
        ingredientList.addAll(ingredientsDAO.getAll());
        ingredientsTable.refresh();
    }

    // When the program exits or database operations are completed
    public void closeDatabaseConnection() {
        ingredientsDAO.close();
        System.out.println("Database connection closed.");
    }

}


