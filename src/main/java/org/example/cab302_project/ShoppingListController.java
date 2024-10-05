package org.example.cab302_project;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class ShoppingListController {

    @FXML private ListView<ShoppingListItem> shoppingListView;
    @FXML private ComboBox<Ingredient> ingredientComboBox;
    @FXML private TextField quantityField;
    @FXML private Button addButton;
    @FXML private Button updateButton;
    @FXML private Button deleteButton;
    @FXML private Button newListButton;
    @FXML private Button loadButton;
    @FXML private Button saveButton;
    @FXML private Button generateButton;
    @FXML private Button purchaseButton;
    @FXML private Button backButton;
    @FXML private Label currentListLabel;
    @FXML private Button deleteListButton;


    private IngredientsDAO ingredientsDAO;
    private ShoppingListDAO shoppingListDAO;
    private ObservableList<ShoppingListItem> shoppingList;
    private String currentListName;

    public ShoppingListController() {
        ingredientsDAO = new IngredientsDAO();
        shoppingListDAO = new ShoppingListDAO();
        shoppingList = FXCollections.observableArrayList();
    }

    @FXML
    public void initialize() {
        loadAllIngredients();
        setupListView();
        setupButtons();
        newList();
        deleteListButton.setDisable(true);
        deleteListButton.setOnAction(event -> handleDeleteList());
    }

    private void loadAllIngredients() {
        List<Ingredient> allIngredients = ingredientsDAO.getAll();
        ingredientComboBox.setItems(FXCollections.observableArrayList(allIngredients));
        deleteListButton.setDisable(false);
    }

    private void setupListView() {
        shoppingListView.setCellFactory(param -> new ListCell<ShoppingListItem>() {
            @Override
            protected void updateItem(ShoppingListItem item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getIngredient().getIngredient() + " - Quantity: " + item.getQuantity());
                }
            }
        });

        shoppingListView.setItems(shoppingList);

        shoppingListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                ingredientComboBox.setValue(newValue.getIngredient());
                quantityField.setText(String.valueOf(newValue.getQuantity()));
                updateButton.setDisable(false);
                deleteButton.setDisable(false);
            } else {
                updateButton.setDisable(true);
                deleteButton.setDisable(true);
            }
        });
    }

    private void setupButtons() {
        addButton.setOnAction(event -> handleAddItem());
        updateButton.setOnAction(event -> handleUpdateItem());
        deleteButton.setOnAction(event -> handleDeleteItem());
        newListButton.setOnAction(event -> newList());
        loadButton.setOnAction(event -> handleLoadList());
        saveButton.setOnAction(event -> handleSaveList());
        generateButton.setOnAction(event -> handleGenerateList());
        purchaseButton.setOnAction(event -> handlePurchase());
        backButton.setOnAction(event -> {
            try {
                backButton();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void handleAddItem() {
        Ingredient selectedIngredient = ingredientComboBox.getValue();
        String quantityText = quantityField.getText();
        if (selectedIngredient != null && !quantityText.isEmpty()) {
            int quantity = Integer.parseInt(quantityText);
            ShoppingListItem newItem = new ShoppingListItem(selectedIngredient, quantity);
            if (!shoppingList.contains(newItem)) {
                shoppingList.add(newItem);
            } else {
                showAlert("Duplicate Item", "This ingredient is already in the shopping list.");
            }
        } else {
            showAlert("Invalid Input", "Please select an ingredient and enter a quantity.");
        }
    }

    private void handleUpdateItem() {
        ShoppingListItem selectedItem = shoppingListView.getSelectionModel().getSelectedItem();
        String quantityText = quantityField.getText();
        if (selectedItem != null && !quantityText.isEmpty()) {
            int newQuantity = Integer.parseInt(quantityText);
            selectedItem.setQuantity(newQuantity);
            shoppingListView.refresh();
        }
    }

    private void handleDeleteItem() {
        ShoppingListItem selectedItem = shoppingListView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            shoppingList.remove(selectedItem);
        }
    }

    private void handleGenerateList() {
        shoppingList.clear();
        List<Ingredient> autoList = ingredientsDAO.getIngredientsForRestocking();
        for (Ingredient ingredient : autoList) {
            shoppingList.add(new ShoppingListItem(ingredient, ingredient.getMinQuantity() - ingredient.getQuantity()));
        }
    }

    private void handleSaveList() {
        if (currentListName == null) {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Save Shopping List");
            dialog.setHeaderText("Enter a name for this shopping list:");
            dialog.setContentText("Name:");

            Optional<String> result = dialog.showAndWait();
            result.ifPresent(name -> {
                currentListName = name;
                saveCurrentList();
            });
        } else {
            saveCurrentList();
        }
    }

    private void saveCurrentList() {
        shoppingListDAO.saveShoppingList(currentListName, shoppingList);
        currentListLabel.setText("Current List: " + currentListName);
        showAlert("Save Successful", "The shopping list has been saved as: " + currentListName);
    }

    private void handleLoadList() {
        List<String> listNames = shoppingListDAO.getShoppingListNames();
        ChoiceDialog<String> dialog = new ChoiceDialog<>(null, listNames);
        dialog.setTitle("Load Shopping List");
        dialog.setHeaderText("Choose a shopping list to load:");
        dialog.setContentText("List name:");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            String selectedListName = result.get();
            loadList(selectedListName);
            System.out.println("Selected list to load: " + selectedListName);
        } else {
            System.out.println("No list selected for loading");
        }
    }

    private void loadList(String listName) {
        List<ShoppingListItem> loadedList = shoppingListDAO.loadShoppingList(listName);
        shoppingList.clear();
        shoppingList.addAll(loadedList);
        currentListName = listName;
        currentListLabel.setText("Current List: " + currentListName);
        deleteListButton.setDisable(false);
        System.out.println("Loaded list: " + currentListName);
    }

    private void newList() {
        shoppingList.clear();
        currentListName = null;
        currentListLabel.setText("Current List: New List");

    }

    @FXML
    private void handleDeleteList() {
        System.out.println("Delete list button clicked. Current list name: " + currentListName);
        if (currentListName != null && !currentListName.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Shopping List");
            alert.setHeaderText("Are you sure you want to delete this shopping list?");
            alert.setContentText("This action cannot be undone.");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                deleteCurrentList();
            } else {
                System.out.println("User cancelled list deletion");
            }
        } else {
            System.out.println("No list to delete: currentListName is null or empty");
            showAlert("No List Selected", "Please load a list before attempting to delete.");
        }
    }

    private void deleteCurrentList() {
        System.out.println("Attempting to delete current list: " + currentListName);
        if (currentListName != null && !currentListName.isEmpty()) {
            boolean deleted = shoppingListDAO.deleteShoppingList(currentListName);
            if (deleted) {
                System.out.println("List deleted successfully");
                shoppingList.clear();
                currentListName = null;
                currentListLabel.setText("Current List: New List");
                deleteListButton.setDisable(true);
                showInfo("List Deleted", "The shopping list has been deleted.");
            } else {
                System.out.println("Failed to delete list");
                showError("Delete Failed", "Failed to delete the shopping list. Please try again.");
            }
        } else {
            System.out.println("No current list to delete");
        }
    }

    private void showError(String deleteFailed, String s) {
    }

    // Display information message
    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void handlePurchase() {
        System.out.println("Purchase button clicked. Current list name: " + currentListName);
        if (currentListName == null || currentListName.isEmpty()) {
            System.out.println("No current list to purchase");
            showAlert("No List Selected", "Please load or save a list before marking it as purchased.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Purchase");
        alert.setHeaderText("Update Ingredient Quantities and Delete List");
        alert.setContentText("Are you sure you want to update the ingredient quantities and delete this shopping list?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            System.out.println("User confirmed purchase");
            updateIngredientQuantities();
            deleteCurrentList();
        } else {
            System.out.println("User cancelled purchase");
        }
    }

    private void updateIngredientQuantities() {
        System.out.println("Updating ingredient quantities");
        for (ShoppingListItem item : shoppingList) {
            Ingredient updatedIngredient = ingredientsDAO.getById(item.getIngredient().getId());
            if (updatedIngredient != null) {
                int newQuantity = updatedIngredient.getQuantity() + item.getQuantity();
                updatedIngredient.setQuantity(newQuantity);
                ingredientsDAO.update(updatedIngredient);
                System.out.println("Updated " + updatedIngredient.getIngredient() + " quantity to " + newQuantity);
            } else {
                System.out.println("Failed to update ingredient: " + item.getIngredient().getIngredient());
            }
        }
        System.out.println("Finished updating ingredient quantities");
    }


    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    protected void backButton() throws IOException {
        Stage stage = (Stage) backButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(IngredientTrackerApplication.class.getResource("menu-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), IngredientTrackerApplication.WIDTH, IngredientTrackerApplication.HEIGHT);
        scene.getStylesheets().add(Objects.requireNonNull(IngredientTrackerApplication.class.getResource("FormStyles.css")).toExternalForm());
        stage.setTitle("Ingredient Tracker");
        stage.setScene(scene);
    }
}