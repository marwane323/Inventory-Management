package inventory.Controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import inventory.Entity.Product;
import inventory.Entity.Supplier;
import inventory.Model.ProductModel;
import inventory.Model.PurchaseModel;
import inventory.Model.SaleModel;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.function.Predicate;

public class AddSaleWindowController {

    /* ------------------------------- FXML Objects ------------------------------- */

    @FXML
    private Label labelTotalCost, label;

    @FXML
    private JFXTextField txtSearch, txtQuantity;

    @FXML
    private JFXButton insertButton;

    @FXML
    private TableView<Product> productsTable;

    @FXML
    private TableColumn<Product, Integer> productIDColumn;

    @FXML
    private TableColumn<Product, String> brandNameColumn;

    @FXML
    private TableColumn<Product, String> productNameColumn;

    @FXML
    private TableColumn<Product, Float> productSellingPriceColumn;

    @FXML
    private TableColumn<Product, Integer> productMinQuantityColumn;

    @FXML
    private TableColumn<Product, Integer> productQuantityColumn;

    /* ------------------------------- Other Objects & Variables ------------------------------- */

    private Service productService;

    /* ------------------------------- Initialize Method ------------------------------- */

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    public void initialize() {
        //Initializing main service for the TableView
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
        //Setting up Cell Value Factory with Value Properties in the table
        productIDColumn.setCellValueFactory(cellData -> cellData.getValue().getProductIDProperty().asObject());
        brandNameColumn.setCellValueFactory(cellData -> cellData.getValue().getBrandNameProperty());
        productNameColumn.setCellValueFactory(cellData -> cellData.getValue().getProductNameProperty());
        productSellingPriceColumn.setCellValueFactory(cellData -> cellData.getValue().getSellingPriceProperty().asObject());
        productMinQuantityColumn.setCellValueFactory(cellData -> cellData.getValue().getQuantityMinimumProperty().asObject());
        productQuantityColumn.setCellValueFactory(cellData -> cellData.getValue().getQuantityInStockProperty().asObject());

        //Binding data of the TableView and the observable list coming from the database
        productsTable.itemsProperty().bind(productService.valueProperty());
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
        //Restarting the product service if its state is already SUCCEEDED, otherwise just start the service
        if (productService.getState() == Service.State.SUCCEEDED) {
            productService.reset();
            productService.start();
        } else if (productService.getState() == Service.State.READY) {
            productService.start();
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
        //Setup the Quantity TextField
        txtQuantity.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d{0,4}?")) {
                    txtQuantity.setText(oldValue);
                }
                updateTotalCost();
            }
        });
    }

    /**
     * Updates the Total Cost label
     */
    public void updateTotalCost(){
        if (!txtQuantity.getText().isEmpty() && !productsTable.getSelectionModel().isEmpty()){
            float firstValue, secondValue;
            try {
                firstValue = Float.parseFloat(txtQuantity.getText());
            } catch (NumberFormatException exception1){
                firstValue = 0.0f;
            }
            try {
                secondValue = productsTable.getSelectionModel().getSelectedItem().getSellingPrice();
            } catch (NumberFormatException exception2){
                secondValue = 0.0f;
            }
            labelTotalCost.setText(String.valueOf(firstValue * secondValue));
        }
    }

    /**
     * Check if the fields are empty
     */
    public boolean fieldsEmpty() {
        return (txtQuantity.getText().trim().isEmpty());
    }

    /* ------------------------------- FXML Methods ------------------------------- */

    public void hideStage(){
        Stage stage = (Stage) insertButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void searchProducts() {
        /*--!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!--*/
        FilteredList<Product> filteredProductsData = new FilteredList<>((ObservableList<Product>) productService.getValue(), e -> true);
        txtSearch.textProperty().addListener((observableValue, oldValue, newValue) -> {
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

    /**
     * Clears all fields
     */
    @FXML
    public void clearFields() {
        txtQuantity.clear();
        labelTotalCost.setText("");
        label.setText("");
        label.setTextFill(Color.RED);
    }

    /**
     * Inserting new sale using informations provided in the UI fields
     */
    @FXML
    public void insertSale() {
        //Int variable to check if insert is successful
        int isSuccessful = -1;
        //Check if fields are empty
        if (fieldsEmpty()) {
            label.setText("Please Enter Quantity");
            return;
        } else if (productsTable.getSelectionModel().getSelectedItem() == null) {
            label.setText("Please Select A Product");
            return;
        }

        int productID = productsTable.getSelectionModel().getSelectedItem().getProductID();
        int invoiceID = SalesWindowController.getInvoiceID();


        //Getting informations entered in the UI fields
        int quantity = Integer.parseInt(txtQuantity.getText());
        float totalCost = Float.parseFloat(labelTotalCost.getText());

        //Checking quantity in stock of the product
        if (ProductModel.getInstance().getProductByID(productID).getQuantityInStock() >= quantity && quantity != 0){
            //Inserting the sale in the database
            try {
                isSuccessful = SaleModel.getInstance().insertSale(invoiceID, productID, quantity, totalCost);
            } catch (SQLException exception) {
                System.out.println("Couldn't Insert Sale: " + exception.getMessage());
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Failed Selling!");
            alert.setHeaderText(null);
            alert.setContentText("You can't sell what you don't have!");
            alert.show();
        }

        //Testing operation has been executed successfully
        if (isSuccessful != -1) {
            hideStage();
        } else {
            label.setTextFill(Color.RED);
            label.setText("Inserting sale FAILED!");
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
