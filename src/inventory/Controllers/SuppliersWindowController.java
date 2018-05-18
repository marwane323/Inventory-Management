package inventory.Controllers;

import com.jfoenix.controls.JFXListCell;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import inventory.Entity.Brand;
import inventory.Entity.Product;
import inventory.Entity.Supplier;
import inventory.Model.BrandModel;
import inventory.Model.ProductModel;
import inventory.Model.SupplierModel;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.util.Callback;

import java.sql.SQLException;
import java.util.Optional;
import java.util.function.Predicate;

public class SuppliersWindowController {

    /* ------------------------------- FXML Objects ------------------------------- */

    @FXML
    private Label label;

    @FXML
    private JFXTextField txtSearch, txtSupplierName, txtSupplierPhone;
    @FXML
    private JFXTextArea txtSupplierAddress;

    @FXML
    private TableView<Supplier> suppliersTable;

    @FXML
    private TableColumn<Supplier, Integer> supplierIDColumn;

    @FXML
    private TableColumn<Supplier, String> supplierNameColumn;

    @FXML
    private TableColumn<Supplier, String> supplierPhoneColumn;

    @FXML
    private TableColumn<Supplier, String> supplierAddressColumn;

    /* ------------------------------- Other Objects & Variables ------------------------------- */

    private Service supplierService;

    /* ------------------------------- Initialize Method ------------------------------- */

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    public void initialize() {
        //Initializing main service for the TableView
        supplierService = new Service<ObservableList<Supplier>>() {
            @Override
            protected Task<ObservableList<Supplier>> createTask() {
                return new Task<ObservableList<Supplier>>() {
                    @Override
                    protected ObservableList<Supplier> call() throws Exception {
                        return FXCollections.observableArrayList(SupplierModel.getInstance().querySuppliers());
                    }
                };
            }
        };

        //Setting up Cell Value Factory with Value Properties in the table
        supplierIDColumn.setCellValueFactory(cellData -> cellData.getValue().getSupplierIDProperty().asObject());
        supplierNameColumn.setCellValueFactory(cellData -> cellData.getValue().getSupplierNameProperty());
        supplierPhoneColumn.setCellValueFactory(cellData -> cellData.getValue().getSupplierPhoneProperty());
        supplierAddressColumn.setCellValueFactory(cellData -> cellData.getValue().getSupplierAddressProperty());

        //Binding data of the TableView and the observable list coming from the database
        suppliersTable.itemsProperty().bind(supplierService.valueProperty());
        //Setting up data in the TableView
        setTableView();
        //Setting up fields in the UI
        setFields();
    }
    /* ------------------------------- Other Methods ------------------------------- */

    /**
     * Sets up the TableView
     */
    public void setTableView() {
        //Restarting the service if its state is already SUCCEEDED, otherwise just start the service
        if (supplierService.getState() == Service.State.SUCCEEDED) {
            supplierService.reset();
            supplierService.start();
        } else if (supplierService.getState() == Service.State.READY) {
            supplierService.start();
        }
    }

    /**
     * Setup TextFields in the UI
     */
    public void setFields(){
        //Setup the Supplier Name TextField
        txtSupplierName.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (txtSupplierName.getText().length() > 40) {
                    String s = txtSupplierName.getText().substring(0, 40);
                    txtSupplierName.setText(s);
                }
            }
        });
        //Setup the Supplier Phone TextField
        txtSupplierPhone.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d{0,12}?")) {
                    txtSupplierPhone.setText(oldValue);
                }
            }
        });
        //Setup the Supplier Address TextField
        txtSupplierAddress.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (txtSupplierAddress.getText().length() > 100) {
                    String s = txtSupplierAddress.getText().substring(0, 100);
                    txtSupplierAddress.setText(s);
                }
            }
        });
    }

    /**
     * Error message that appears when no supplier is selected
     */
    public void noSupplierSelectedAlert(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Select A Supplier!");
        alert.setHeaderText("Select A Supplier!");
        alert.setContentText("No supplier was selected! Please select a supplier first.");
        alert.showAndWait();
    }

    /**
     * Check if the fields are empty
     */
    public boolean fieldsEmpty() {
        return (txtSupplierName.getText().trim().isEmpty() || txtSupplierPhone.getText().trim().isEmpty()
                || txtSupplierAddress.getText().trim().isEmpty());
    }

    /* ------------------------------- FXML Methods ------------------------------- */

    /**
     * Shows selected supplier's data in the UI fields
     */
    @FXML
    public void showData(){
        //Get the selected item from the table
        Supplier supplier = suppliersTable.getSelectionModel().getSelectedItem();

        if (supplier != null) {
            //Setting the fields in the UI
            txtSupplierName.setText(supplier.getSupplierName());
            txtSupplierPhone.setText(supplier.getSupplierPhone());
            txtSupplierAddress.setText(supplier.getSupplierAddress());

            //Printing the selected product in the console
            System.out.println("Selected Supplier: " + supplier);
        }
    }

    @FXML
    public void searchSuppliers(){
        /*--!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!--*/
        FilteredList<Supplier> filteredData = new FilteredList<>((ObservableList<Supplier>) supplierService.getValue(), e -> true);
        txtSearch.textProperty().addListener((observableValue, oldValue, newValue) -> {
            filteredData.setPredicate((Predicate<? super Supplier>) supplier -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (supplier.getSupplierName().toLowerCase().contains(lowerCaseFilter) || supplier.getSupplierAddress().toLowerCase().contains(lowerCaseFilter)
                        || supplier.getSupplierPhone().toLowerCase().contains(lowerCaseFilter)){
                    return true;
                }
                return false;
            });
        });
        SortedList<Supplier> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(suppliersTable.comparatorProperty());

        //Need a way to show (or bind) results to my principal and only TableView after that I have already bidden it with the service
    }

    /**
     * Clears all fields
     */
    @FXML
    public void clearFields() {
        txtSearch.clear();
        txtSupplierName.clear();
        txtSupplierPhone.clear();
        txtSupplierAddress.clear();
        label.setText("");
        label.setTextFill(Color.GREEN);
    }

    /**
     * Inserting new supplier using informations provided in the UI fields
     */
    @FXML
    public void insertSupplier(){
        //Int variable to check if insert is successful
        int isSuccessful = -1;
        //Check if fields are empty
        if (fieldsEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Enter All Supplier's Informations!");
            alert.setHeaderText("Enter All Supplier's Informations!");
            alert.setContentText("Please enter all supplier's informations first, before trying to add a new supplier.");
            alert.showAndWait();
            return;
        }

        //Getting informations entered in the UI fields
        String supplierName = txtSupplierName.getText();
        String supplierPhone = txtSupplierPhone.getText();
        String supplierAddress = txtSupplierAddress.getText();

        //Inserting the supplier in the database
        try {
            isSuccessful = SupplierModel.getInstance().insertSupplier(supplierName, supplierAddress, supplierPhone);
        } catch (SQLException exception) {
            System.out.println("Couldn't Insert Supplier: " + exception.getMessage());
        }

        if (isSuccessful != 1) {
            //Clearing data in UI fields
            clearFields();
            //Setting label text
            label.setTextFill(Color.GREEN);
            label.setText("Successfuly inserted supplier!");
            //Refreshing the TableView
            setTableView();
            //Requesting focus for the TableView
            suppliersTable.requestFocus();
            //Selecting the new product in the TableView
            suppliersTable.getSelectionModel().select(SupplierModel.getInstance().getSupplierByID(isSuccessful));
            suppliersTable.getFocusModel().focus(suppliersTable.getSelectionModel().getSelectedIndex());
            showData();

        } else {
            label.setTextFill(Color.RED);
            label.setText("Inserting supplier failed!");
        }

    }

    /**
     * Updating selected supplier using informations provided in the UI fields
     */
    @FXML
    public void updateSupplier(){
        //Boolean variable to check if update is successful
        boolean isSuccessful;

        //Get the selected item from the table
        Supplier supplier = suppliersTable.getSelectionModel().getSelectedItem();
        if (supplier == null || suppliersTable.getSelectionModel().isEmpty()){
            //Alert No Supplier Is Selected
            noSupplierSelectedAlert();
            return;
        }

        //Getting informations entered in the UI fields
        int supplierID = supplier.getSupplierID();
        String supplierName = txtSupplierName.getText();
        String supplierPhone = txtSupplierPhone.getText();
        String supplierAddress = txtSupplierAddress.getText();

        //Updating the item in the database
        isSuccessful = SupplierModel.getInstance().updateSupplier(supplierID, supplierName,
                                                            supplierAddress, supplierPhone);
        if (isSuccessful){
            //Clearing data in UI fields
            clearFields();
            //Setting label text
            label.setTextFill(Color.GREEN);
            label.setText("Successfuly updated supplier!");
            //Refreshing the TableView
            setTableView();
            //Requesting focus for the TableView
            suppliersTable.requestFocus();
            //Selecting the updated supplier in the TableView
            suppliersTable.getSelectionModel().select(SupplierModel.getInstance().getSupplierByID(supplierID));
            suppliersTable.getFocusModel().focus(suppliersTable.getSelectionModel().getSelectedIndex());
            showData();
        } else {
            label.setTextFill(Color.RED);
            label.setText("Updating supplier failed!");
        }
    }

    /**
     * Deleting selected supplier
     */
    @FXML
    public void deleteSupplier(){
        //Boolean variable to check if delete is successful
        boolean isSuccessful;

        //Get the selected item from the table
        Supplier supplier = suppliersTable.getSelectionModel().getSelectedItem();
        if (supplier == null || suppliersTable.getSelectionModel().isEmpty()){
            //Alert No Supplier Is Selected
            noSupplierSelectedAlert();
            return;
        }

        //Deleting the item in the database
        isSuccessful = SupplierModel.getInstance().deleteSupplier(supplier.getSupplierID());

        if (isSuccessful){
            //Clearing data in UI fields
            clearFields();
            //Setting label text
            label.setTextFill(Color.GREEN);
            label.setText("Successfuly deleted supplier!");
            //Refreshing the TableView
            setTableView();
            //Clear Selection
            suppliersTable.getSelectionModel().clearSelection();
        } else {
            label.setTextFill(Color.RED);
            label.setText("Deleting supplier failed!");
        }
    }

    /* ---------------------------------------------------------------------------- */

}
