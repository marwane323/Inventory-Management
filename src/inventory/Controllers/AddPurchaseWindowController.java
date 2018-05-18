package inventory.Controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListCell;
import com.jfoenix.controls.JFXTextField;
import inventory.Entity.Brand;
import inventory.Entity.Product;
import inventory.Entity.Supplier;
import inventory.Model.BrandModel;
import inventory.Model.ProductModel;
import inventory.Model.PurchaseModel;
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
import javafx.stage.Stage;
import javafx.util.Callback;

import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.util.Optional;
import java.util.function.Predicate;

public class AddPurchaseWindowController {

    /* ------------------------------- FXML Objects ------------------------------- */

    @FXML
    private Label labelFields, labelTotalCost, label;

    @FXML
    private JFXTextField txtSearchProduct, txtSearchSupplier, txtCostPrice, txtQuantity;

    @FXML
    private JFXButton doneButton;

    @FXML
    private TableView<Product> productsTable;

    @FXML
    private TableColumn<Product, String> brandNameColumn;

    @FXML
    private TableColumn<Product, String> productNameColumn;

    @FXML
    private TableColumn<Product, Integer> productMinQuantityColumn;

    @FXML
    private TableColumn<Product, Integer> productQuantityColumn;

    @FXML
    private TableView<Supplier> suppliersTable;

    @FXML
    private TableColumn<Supplier, String> supplierNameColumn;

    @FXML
    private TableColumn<Supplier, String> supplierAddressColumn;

    /* ------------------------------- Other Objects & Variables ------------------------------- */

//    private int invoiceID = PurchaseInvoiceWindowController.getInvoiceID();
    private Service productService;
    private Service supplierService;

    /* ------------------------------- Initialize Method ------------------------------- */

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    public void initialize() {
        //Initializing product service for the Products TableView
        productService = new Service<ObservableList<Product>>() {
            @Override
            protected Task<ObservableList<Product>> createTask() {
                return new Task<ObservableList<Product>>() {
                    @Override
                    protected ObservableList<Product> call() throws Exception {
                        return FXCollections.observableArrayList(ProductModel.getInstance().queryProducts());
                    }
                };
            }
        };
        //Initializing supplier service for the Suppliers TableView
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

        //Setting up Cell Value Factory with Value Properties in the productsTable
        brandNameColumn.setCellValueFactory(cellData -> cellData.getValue().getBrandNameProperty());
        productNameColumn.setCellValueFactory(cellData -> cellData.getValue().getProductNameProperty());
        productMinQuantityColumn.setCellValueFactory(cellData -> cellData.getValue().getQuantityMinimumProperty().asObject());
        productQuantityColumn.setCellValueFactory(cellData -> cellData.getValue().getQuantityInStockProperty().asObject());

        //Setting up Cell Value Factory with Value Properties in the suppliersTable
        supplierNameColumn.setCellValueFactory(cellData -> cellData.getValue().getSupplierNameProperty());
        supplierAddressColumn.setCellValueFactory(cellData -> cellData.getValue().getSupplierAddressProperty());

        //Binding data of the TableView and the observable list coming from the database
        productsTable.itemsProperty().bind(productService.valueProperty());
        //Binding data of the TableView and the observable list coming from the database
        suppliersTable.itemsProperty().bind(supplierService.valueProperty());
        //Setting up data in the TableViews
        setTableViews();
        //Setting up fields in the UI
        setFields();
    }
    /* ------------------------------- Other Methods ------------------------------- */

    /**
     * Sets up the TableView
     */
    public void setTableViews() {
        //Restarting the product service if its state is already SUCCEEDED, otherwise just start the service
        if (productService.getState() == Service.State.SUCCEEDED) {
            productService.reset();
            productService.start();
        } else if (productService.getState() == Service.State.READY) {
            productService.start();
        }
        //Restarting the supplier service if its state is already SUCCEEDED, otherwise just start the service
        if (supplierService.getState() == Service.State.SUCCEEDED) {
            supplierService.reset();
            supplierService.start();
        } else if (supplierService.getState() == Service.State.READY) {
            supplierService.start();
        }

        //Setting the Products TableView Row to appear in red if the product's quantity <= quantityMin
        productsTable.setRowFactory(row -> new TableRow<Product>() {
            @Override
            protected void updateItem(Product item, boolean empty) {
                super.updateItem(item, empty);
                //Checking the empty rows in the TableView
                if (item == null || empty) {
                    setStyle("");
                } else {
                    //Checking if the product's quantityInStock <= product's QuantityMinimum
                    if (item.getQuantityInStock() <= item.getQuantityMinimum()) {
                        //Setting style using CSS
                        setStyle("-fx-background-color: #FA1A42; -fx-text-inner-color: white");
                    }
                }
                ;
            }
        });
    }

    /**
     * Setup TextFields in the UI
     */
    public void setFields() {
        //Setup the Cost Price TextField
        txtCostPrice.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d{0,7}([\\.]\\d{0,4})?")) {
                    txtCostPrice.setText(oldValue);
                }
                if (!txtCostPrice.getText().isEmpty() || !txtQuantity.getText().isEmpty()){
                    float firstValue, secondValue;
                    try {
                        firstValue = Float.parseFloat(txtCostPrice.getText());
                    } catch (NumberFormatException exception1){
                        firstValue = 0.0f;
                    }
                    try {
                        secondValue = Float.parseFloat(txtQuantity.getText());
                    } catch (NumberFormatException exception1){
                        secondValue = 0.0f;
                    }
                    labelTotalCost.setText(String.valueOf(firstValue * secondValue));
                }
            }
        });
        //Setup the Quantity TextField
        txtQuantity.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d{0,4}?")) {
                    txtQuantity.setText(oldValue);
                }
                if (!txtQuantity.getText().isEmpty() || !txtCostPrice.getText().isEmpty()){
                    float firstValue, secondValue;
                    try {
                        firstValue = Float.parseFloat(txtQuantity.getText());
                    } catch (NumberFormatException exception1){
                        firstValue = 0.0f;
                    }
                    try {
                        secondValue = Float.parseFloat(txtCostPrice.getText());
                    } catch (NumberFormatException exception1){
                        secondValue = 0.0f;
                    }
                    labelTotalCost.setText(String.valueOf(firstValue * secondValue));
                }
            }
        });
    }

    /**
     * Check if the fields are empty
     */
    public boolean fieldsEmpty() {
        return (txtCostPrice.getText().trim().isEmpty() || txtQuantity.getText().trim().isEmpty());
    }

    /* ------------------------------- FXML Methods ------------------------------- */

    @FXML
    public void hideStage(){
        Stage stage = (Stage) doneButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void searchProducts() {
        /*--!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!--*/
        FilteredList<Product> filteredProductsData = new FilteredList<>((ObservableList<Product>) productService.getValue(), e -> true);
        txtSearchProduct.textProperty().addListener((observableValue, oldValue, newValue) -> {
            filteredProductsData.setPredicate((Predicate<? super Product>) product -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (product.getProductName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
        });
        SortedList<Product> sortedProductsData = new SortedList<>(filteredProductsData);
        sortedProductsData.comparatorProperty().bind(productsTable.comparatorProperty());

        //Need a way to show (or bind) results to my principal and only TableView after that I have already bidden it with the service
    }

    @FXML
    public void searchSuppliers() {
        /*--!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!--*/
        FilteredList<Supplier> filteredSuppliersData = new FilteredList<>((ObservableList<Supplier>) supplierService.getValue(), e -> true);
        txtSearchSupplier.textProperty().addListener((observableValue, oldValue, newValue) -> {
            filteredSuppliersData.setPredicate((Predicate<? super Supplier>) supplier -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (supplier.getSupplierName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
        });
        SortedList<Supplier> sortedSuppliersData = new SortedList<>(filteredSuppliersData);
        sortedSuppliersData.comparatorProperty().bind(suppliersTable.comparatorProperty());

        //Need a way to show (or bind) results to my principal and only TableView after that I have already bidden it with the service
    }

    /**
     * Clears all fields
     */
    @FXML
    public void clearFields() {
        labelFields.setText("");
        labelFields.setTextFill(Color.RED);
        txtCostPrice.clear();
        txtQuantity.clear();
        labelTotalCost.setText("");
        label.setText("");
        label.setTextFill(Color.RED);
    }

    /**
     * Inserting new purchase using informations provided in the UI fields
     */
    @FXML
    public void insertPurchase() {
        //Int variable to check if insert is successful
        int isSuccessful = -1;
        //Check if fields are empty
        if (fieldsEmpty()) {
            labelFields.setText("Please Enter Purchase Informations");
            return;
        } else if (productsTable.getSelectionModel().getSelectedItem() == null) {
            label.setText("Please Select A Product");
            return;
        } else if (suppliersTable.getSelectionModel().getSelectedItem() == null) {
            label.setText("Please Select A Supplier");
            return;
        }

        int productID = productsTable.getSelectionModel().getSelectedItem().getProductID();
        int supplierID = suppliersTable.getSelectionModel().getSelectedItem().getSupplierID();
        int invoiceID = PurchaseInvoiceWindowController.getInvoiceID();


        //Getting informations entered in the UI fields
        float costPrice = Float.parseFloat(txtCostPrice.getText());
        int quantity = Integer.parseInt(txtQuantity.getText());
        float totalCost = Float.parseFloat(labelTotalCost.getText());

        //Inserting the purchase in the database
        try {
            isSuccessful = PurchaseModel.getInstance().insertPurchase(invoiceID, supplierID, productID, costPrice, quantity, totalCost);
        } catch (SQLException exception) {
            System.out.println("Couldn't Insert Purchase: " + exception.getMessage());
        }

        //Testing operation has been executed successfully
        if (isSuccessful != -1) {
            //Clearing data in UI fields
            clearFields();
            //Setting label text
            label.setTextFill(Color.GREEN);
            label.setText("Successfuly bought product: \n" + productsTable.getSelectionModel().getSelectedItem().getProductName());
            //Refreshing the TableViews
            setTableViews();
            //Requesting focus for the TableView
            productsTable.requestFocus();
            //Selecting the new product in the TableView
            productsTable.getSelectionModel().select(ProductModel.getInstance().getProductByID(productID));
            productsTable.getFocusModel().focus(productsTable.getSelectionModel().getSelectedIndex());
        } else {
            labelFields.setTextFill(Color.RED);
            labelFields.setText("Inserting purchase FAILED!");
        }
    }

    /* ------------------------------- Getters & Setters ------------------------------- */

//    public int getInvoiceID() {
//        return invoiceID;
//    }
//
//    public void setInvoiceID(int invoiceID) {
//        this.invoiceID = invoiceID;
//    }

    /* ---------------------------------------------------------------------------- */

}
