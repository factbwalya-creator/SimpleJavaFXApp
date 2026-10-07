package com.example.hellofx;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Optional;

public class CustomerManagerApp extends Application {

    // Step 2: the data model - an ObservableList the TableView watches
    private final ObservableList<Customer> customers = FXCollections.observableArrayList();

    @Override
    public void start(Stage stage) {
        // Step 1: form with name field and province list
        Label nameLabel = new Label("_Name:");
        TextField nameField = new TextField();
        nameField.setPromptText("Customer name");
        nameLabel.setLabelFor(nameField);
        nameLabel.setMnemonicParsing(true);

        Label provinceLabel = new Label("_Province:");
        ComboBox<String> provinceBox = new ComboBox<>(FXCollections.observableArrayList(
                "Central", "Copperbelt", "Eastern", "Luapula", "Lusaka",
                "Muchinga", "Northern", "North-Western", "Southern", "Western"));
        provinceBox.setPromptText("Select province");
        provinceLabel.setLabelFor(provinceBox);
        provinceLabel.setMnemonicParsing(true);

        Button addButton = new Button("_Add Customer");
        addButton.setDefaultButton(true);          // Enter key submits the form
        Button deleteButton = new Button("_Delete Selected");

        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: #c62828;");
        errorLabel.setFocusTraversable(false);

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        form.add(nameLabel, 0, 0);
        form.add(nameField, 1, 0);
        form.add(provinceLabel, 0, 1);
        form.add(provinceBox, 1, 1);
        GridPane.setHgrow(nameField, Priority.ALWAYS);
        provinceBox.setMaxWidth(Double.MAX_VALUE);

        // Step 3: TableView with name and province columns
        TableView<Customer> table = new TableView<>(customers);
        TableColumn<Customer, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(c -> c.getValue().nameProperty());
        nameCol.setPrefWidth(220);
        TableColumn<Customer, String> provinceCol = new TableColumn<>("Province");
        provinceCol.setCellValueFactory(c -> c.getValue().provinceProperty());
        provinceCol.setPrefWidth(160);
        table.getColumns().addAll(nameCol, provinceCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        table.setPlaceholder(new Label("No customers yet"));
        VBox.setVgrow(table, Priority.ALWAYS);

        // Delete is only possible when a row is selected
        deleteButton.disableProperty().bind(table.getSelectionModel().selectedItemProperty().isNull());

        // Step 4: validate, then add
        addButton.setOnAction(e -> {
            String name = nameField.getText() == null ? "" : nameField.getText().trim();
            String province = provinceBox.getValue();

            if (name.isEmpty()) {
                showError("Please enter a customer name.", errorLabel, nameField);
                return;
            }
            if (province == null) {
                showError("Please select a province.", errorLabel, provinceBox);
                return;
            }
            boolean duplicate = customers.stream().anyMatch(c ->
                    c.getName().equalsIgnoreCase(name) && c.getProvince().equals(province));
            if (duplicate) {
                showError("That customer already exists in " + province + ".", errorLabel, nameField);
                return;
            }

            customers.add(new Customer(name, province));
            errorLabel.setText("");
            nameField.clear();
            provinceBox.setValue(null);
            nameField.requestFocus();
        });

        // Step 5: confirm before deleting the selected customer
        deleteButton.setOnAction(e -> deleteSelected(table));
        table.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.DELETE && !table.getSelectionModel().isEmpty()) {
                deleteSelected(table);
            }
        });

        HBox buttons = new HBox(10, addButton, deleteButton);
        buttons.setAlignment(Pos.CENTER_LEFT);

        VBox root = new VBox(12, form, buttons, errorLabel, table);
        root.setPadding(new Insets(16));

        stage.setTitle("Customer Manager - 202500585");
        stage.setScene(new Scene(root, 520, 460));
        stage.show();
        nameField.requestFocus();
    }

    private void showError(String message, Label errorLabel, javafx.scene.Node focusTarget) {
        errorLabel.setText(message);
        focusTarget.requestFocus();
    }

    private void deleteSelected(TableView<Customer> table) {
        Customer selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Delete " + selected.getName() + " (" + selected.getProvince() + ")?",
                ButtonType.YES, ButtonType.NO);
        confirm.setTitle("Confirm deletion");
        confirm.setHeaderText(null);
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.YES) {
            customers.remove(selected);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}