package inventory.Controllers;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListCell;
import com.jfoenix.controls.JFXTextField;
import inventory.Entity.Brand;
import inventory.Entity.Product;
import inventory.Model.BrandModel;
import inventory.Model.Datasource;
import inventory.Model.ProductModel;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.util.Callback;
import javafx.util.Pair;

import java.sql.SQLException;
import java.util.Optional;
import java.util.function.Predicate;

public class ProductsWindowController {

    /* ------------------------------- FXML Objects ------------------------------- */

    @FXML
    private Label label, labelBrand;

    @FXML
    private JFXTextField txtSearch, txtProductID, txtProductName, txtSellingPrice, txtQuantityMinimum;

    @FXML
    private JFXComboBox<Brand> boxBrand;

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

    private Service service;
    private Service brandService;

    /* ------------------------------- Initialize Method ------------------------------- */

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    public void initialize() {
        //Initializing main service for the TableView
        service = new Service<ObservableList<Product>>() {
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
        //Initializing brand service for the ComboBox
        brandService = new Service<ObservableList<Brand>>() {
            @Override
            protected Task<ObservableList<Brand>> createTask() {
                return new Task<ObservableList<Brand>>() {
                    @Override
                    protected ObservableList<Brand> call() throws Exception {
                        return FXCollections.observableArrayList(BrandModel.getInstance().queryBrands());
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
        productsTable.itemsProperty().bind(service.valueProperty());
        //Binding data of the ComboBox and the observable list coming from the database
        boxBrand.itemsProperty().bind(brandService.valueProperty());
        //Setting up data in the ComboBox
        setBrandsBox();
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
        if (service.getState() == Service.State.SUCCEEDED) {
            service.reset();
            service.start();
        } else if (service.getState() == Service.State.READY) {
            service.start();
        }

        //Setting the TableView Row to appear in red if the product's quantity <= quantityMin
        productsTable.setRowFactory(row -> new TableRow<Product>(){
            @Override
            protected void updateItem(Product item, boolean empty) {
                super.updateItem(item, empty);
                //Checking the empty rows in the TableView
                if (item == null || empty){
                    setStyle("");
                } else {
                    //Checking if the product's quantityInStock <= product's QuantityMinimum
                    if (item.getQuantityInStock() <= item.getQuantityMinimum()) {
                        //Setting style using CSS
                        setStyle("-fx-background-color: #FA1A42; -fx-text-inner-color: white");
                    }
                };
            }
        });
    }

    /**
     * Setting the brands ComboBox
     */
    public void setBrandsBox(){
        //Restarting the service if its state is already SUCCEEDED, otherwise just start the service
        if (brandService.getState() == Service.State.SUCCEEDED) {
            brandService.reset();
            brandService.start();
        } else if (brandService.getState() == Service.State.READY) {
            brandService.start();
        }

        //Customizing the cell factory
        Callback<ListView<Brand>, ListCell<Brand>> cellFactory = new Callback<ListView<Brand>, ListCell<Brand>>() {
            @Override
            public JFXListCell<Brand> call(ListView<Brand> l) {
                return new JFXListCell<Brand>() {
                    @Override
                    public void updateItem(Brand item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setGraphic(null);
                        } else {
                            setAlignment(Pos.CENTER);
                        }
                    }
                } ;
            }
        };
        //Setting the cell factory
        boxBrand.setCellFactory(cellFactory);
        //Clearing the selection from the ComboBox
        boxBrand.getSelectionModel().clearSelection();
    }

    /**
     * Setup TextFields in the UI
     */
    public void setFields(){
        //Setup the Product ID TextField
        txtProductID.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d{0,7}?")) {
                    txtProductID.setText(oldValue);
                }
            }
        });
        //Setup the Selling Price TextField
        txtSellingPrice.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d{0,7}([\\.]\\d{0,4})?")) {
                    txtSellingPrice.setText(oldValue);
                }
            }
        });
        //Setup the Quantity Minimum TextField
        txtQuantityMinimum.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d{0,4}?")) {
                    txtQuantityMinimum.setText(oldValue);
                }
            }
        });
    }

    /**
     * Error message that appears when no product is selected
     */
    public void noProductSelectedAlert(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Select A Product!");
        alert.setHeaderText("Select A Product!");
        alert.setContentText("No product was selected! Please select a product first.");
        alert.showAndWait();
    }

    /**
     * Check if the fields are empty
     */
    public boolean fieldsEmpty() {
        return (txtProductID.getText().trim().isEmpty() || boxBrand.getSelectionModel().isEmpty() || txtProductName.getText().trim().isEmpty()
                || txtSellingPrice.getText().trim().isEmpty() || txtQuantityMinimum.getText().trim().isEmpty());
    }

    /* ------------------------------- FXML Methods ------------------------------- */

    /**
     * Shows selected product's data in the UI fields
     */
    @FXML
    public void showData(){
        //Get the selected item from the table
        Product product = productsTable.getSelectionModel().getSelectedItem();

        if (product != null) {
            //Setting the fields in the UI
            txtProductID.setText(String.valueOf(product.getProductID()));
            txtProductName.setText(product.getProductName());
            boxBrand.getSelectionModel().select(BrandModel.getInstance().getBrandByID(product.getBrandID()));
            txtSellingPrice.setText(String.valueOf(product.getSellingPrice()));
            txtQuantityMinimum.setText(String.valueOf(product.getQuantityMinimum()));

            //Printing the selected product in the console
            System.out.println("Selected Product: " + product);
        }
    }

    @FXML
    public void searchProducts(){
        /*--!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!--*/
        FilteredList<Product> filteredData = new FilteredList<>((ObservableList<Product>) service.getValue(), e -> true);
        txtSearch.textProperty().addListener((observableValue, oldValue, newValue) -> {
            filteredData.setPredicate((Predicate<? super Product>) product -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (product.getProductName().toLowerCase().contains(lowerCaseFilter)){
                    return true;
                }
                return false;
            });
        });
        SortedList<Product> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(productsTable.comparatorProperty());

        //Need a way to show (or bind) results to my principal and only TableView after that I have already bidden it with the service
    }

    /**
     * Clears all fields
     */
    @FXML
    public void clearFields() {
        txtSearch.clear();
        txtProductID.clear();
        boxBrand.getSelectionModel().clearSelection();
        txtProductName.clear();
        txtSellingPrice.clear();
        txtQuantityMinimum.clear();
        labelBrand.setText("");
        labelBrand.setTextFill(Color.GREEN);
        label.setText("");
        label.setTextFill(Color.GREEN);
    }

    /**
     * Inserting new product using informations provided in the UI fields
     */
    @FXML
    public void insertProduct(){
        //Int variable to check if insert is successful
        int isSuccessful = -1;
        //Check if fields are empty
        if (fieldsEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Enter All Product's Informations!");
            alert.setHeaderText("Enter All Product's Informations!");
            alert.setContentText("Please enter all product's informations first, before trying to add a new product.");
            alert.showAndWait();
            return;
        }

        //Creating the dialog for initializing product's quantityInStock
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Insert A Product");
        dialog.setHeaderText("Quantity In Stock..");
        dialog.setContentText("Product's initial quantity:");

        //Setting some validation and styling to the TextField in the dialog
        dialog.getEditor().setAlignment(Pos.CENTER);
        dialog.getDialogPane().lookupButton(ButtonType.OK).setDisable(true);
        dialog.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            dialog.getDialogPane().lookupButton(ButtonType.OK).setDisable(newValue.trim().isEmpty());
        });
        dialog.getEditor().textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d{0,4}?")) {
                    dialog.getEditor().setText(oldValue);
                }
            }
        });

        //Getting the result from the dialog (if Canceled is pressed, result.isPresent() = false)
        Optional<String> result = dialog.showAndWait();

        if (result.isPresent()) {
            //Getting informations entered in the UI fields
            int productID = Integer.parseInt(txtProductID.getText());
            int brandID = boxBrand.getSelectionModel().getSelectedItem().getBrandID();
            String productName = txtProductName.getText();
            float sellingPrice = Float.parseFloat(txtSellingPrice.getText());
            int quantityMinimum = Integer.parseInt(txtQuantityMinimum.getText());
            int quantityInStock = Integer.parseInt(result.get());

            //Inserting the product in the database
            try {
                isSuccessful = ProductModel.getInstance().insertProduct(productID, productName, brandID, sellingPrice, quantityMinimum, quantityInStock);
            } catch (SQLException exception) {
                System.out.println("Couldn't Insert Product: " + exception.getMessage());
            }

            //Testing with productID because ProductModel.getInstance().insertProduct() returns the generated key
            if (isSuccessful == productID) {
                //Clearing data in UI fields
                clearFields();
                //Setting label text
                label.setTextFill(Color.GREEN);
                label.setText("Successfuly inserted product!");
                //Refreshing the TableView
                setTableView();
                //Requesting focus for the TableView
                productsTable.requestFocus();
                //Selecting the new product in the TableView
                productsTable.getSelectionModel().select(ProductModel.getInstance().getProductByID(productID));
                productsTable.getFocusModel().focus(productsTable.getSelectionModel().getSelectedIndex());
                showData();

            } else {
                label.setTextFill(Color.RED);
                label.setText("Inserting product FAILED!");
            }
        }
    }

    /**
     * Updating selected product using informations provided in the UI fields
     */
    @FXML
    public void updateProduct(){
        //Boolean variable to check if update is successful
        boolean isSuccessful;

        //Get the selected item from the table
        Product product = productsTable.getSelectionModel().getSelectedItem();
        if (product == null){
            //Alert No Product Is Selected
            noProductSelectedAlert();
            return;
        }

        //Getting informations entered in the UI fields
        int productID = product.getProductID();
        int brandID = boxBrand.getSelectionModel().getSelectedItem().getBrandID();
        String productName = txtProductName.getText();
        float sellingPrice = Float.parseFloat(txtSellingPrice.getText());
        int quantityMinimum = Integer.parseInt(txtQuantityMinimum.getText());
        int quantityInStock = product.getQuantityInStock();

        //Updating the item in the database
        isSuccessful = ProductModel.getInstance().updateProduct(productID, productName, brandID,
                                   sellingPrice, quantityMinimum, quantityInStock);
        if (isSuccessful){
            //Clearing data in UI fields
            clearFields();
            //Setting label text
            label.setTextFill(Color.GREEN);
            label.setText("Successfuly updated product!");
            //Refreshing the TableView
            setTableView();
            //Requesting focus for the TableView
            productsTable.requestFocus();
            //Selecting the updated product in the TableView
            productsTable.getSelectionModel().select(ProductModel.getInstance().getProductByID(productID));
            productsTable.getFocusModel().focus(productsTable.getSelectionModel().getSelectedIndex());
            showData();
        } else {
            label.setTextFill(Color.RED);
            label.setText("Updating product FAILED!");
        }
    }

    /**
     * Deleting selected product
     */
    @FXML
    public void deleteProduct(){
        //Boolean variable to check if delete is successful
        boolean isSuccessful;

        //Get the selected item from the table
        Product product = productsTable.getSelectionModel().getSelectedItem();
        if (product == null){
            //Alert No Product Is Selected
            noProductSelectedAlert();
            return;
        }

        //Deleting the item in the database
        isSuccessful = ProductModel.getInstance().deleteProduct(product.getProductID());

        if (isSuccessful){
            //Clearing data in UI fields
            clearFields();
            //Setting label text
            label.setTextFill(Color.GREEN);
            label.setText("Successfuly deleted product!");
            //Refreshing the TableView
            setTableView();
        } else {
            label.setTextFill(Color.RED);
            label.setText("Deleting product FAILED!");
        }
    }

    /**
     * Inserting a new brand
     */
    @FXML
    public void insertBrand(){
        //Boolean variable to check if insert is successful
        boolean isSuccessful = false;

        //Creating the dialog
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Insert A Brand");
        dialog.setHeaderText("Inserting A New Brand..");
        dialog.setContentText("Please enter the new brand name:");

        //Setting some validation and styling to the TextField in the dialog
        dialog.getEditor().setAlignment(Pos.CENTER);
        dialog.getDialogPane().lookupButton(ButtonType.OK).setDisable(true);
        dialog.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            dialog.getDialogPane().lookupButton(ButtonType.OK).setDisable(newValue.trim().isEmpty());
        });

        //Getting the result from the dialog (if Canceled is pressed, result.isPresent() = false)
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()){
            String brandName = result.get();
            //Checking if Brand already exists
            if (BrandModel.getInstance().getBrandsNames().contains(brandName)){
                //Showing an error message
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Brand already exists");
                alert.setHeaderText(null);
                alert.setContentText("Brand already exists! ");
                alert.show();
            } else {
                try {
                    //Inserting the product
                    BrandModel.getInstance().insertBrand(result.get());
                    isSuccessful = true;
                } catch (SQLException e){
                    System.out.println("Inserting brand failed: " + e.getMessage());
                }
            }
            //Showing information dialog to confirm success of the insert operation
            if (isSuccessful){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Succeeded");
                alert.setHeaderText(null);
                alert.setContentText("Adding " + brandName + " has been done successfully!");
                alert.show();
                //Clearing data in UI fields
                clearFields();
                //Refreshing the ComboBox
                setBrandsBox();
            }
        }
    }

    /**
     * Updating a selected brand
     */
    @FXML
    public void updateBrand(){
        //Boolean variable to check if delete is successful
        boolean isSuccessful = false;
        int brandID;

        //Get the selected item from the table
        Brand brand = boxBrand.getSelectionModel().getSelectedItem();
        if (brand == null){
            //Alert No Product Is Selected
            labelBrand.setTextFill(Color.RED);
            labelBrand.setText("No Brand Was Selected!");
            return;
        }
        //Getting ID of the selected brand
        brandID = brand.getBrandID();

        //Creating the dialog
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Update A Brand");
        dialog.setHeaderText("Updating A New Brand..");
        dialog.setContentText("Please enter the new brand name:");

        //Setting some validation and styling to the TextField in the dialog
        dialog.getEditor().setAlignment(Pos.CENTER);
        dialog.getDialogPane().lookupButton(ButtonType.OK).setDisable(true);
        dialog.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            dialog.getDialogPane().lookupButton(ButtonType.OK).setDisable(newValue.trim().isEmpty());
        });

        //Getting the result from the dialog (if Canceled is pressed, result.isPresent() = false)
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()){
            String brandName = result.get();
            //Checking if Brand already exists
            if (BrandModel.getInstance().getBrandsNames().contains(brandName)){
                //Showing an error message
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Brand already exists");
                alert.setHeaderText(null);
                alert.setContentText("Brand already exists! ");
                alert.show();
            } else {
                //Updating the product
                BrandModel.getInstance().updateBrandName(brandID, result.get());
                isSuccessful = true;
            }
            //Showing information dialog to confirm success of the insert operation
            if (isSuccessful){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Succeeded");
                alert.setHeaderText(null);
                alert.setContentText("Updating " + brandName + " has been done successfully!");
                alert.show();
                //Clearing data in UI fields
                clearFields();
                //Refreshing the ComboBox
                setBrandsBox();

            }
        }
    }

    /**
     * Deleting selected brand
     */
    @FXML
    public void deleteBrand(){
        //Boolean variable to check if delete is successful
        boolean isSuccessful;

        //Get the selected item from the table
        Brand brand = boxBrand.getSelectionModel().getSelectedItem();
        if (brand == null){
            //Alert No Product Is Selected
            labelBrand.setTextFill(Color.RED);
            labelBrand.setText("No Brand Was Selected!");
            return;
        }

        //Deleting the item in the database
        isSuccessful = BrandModel.getInstance().deleteBrand(brand.getBrandID());

        if (isSuccessful){
            //Clearing data in UI fields
            clearFields();
            //Setting label text
            labelBrand.setTextFill(Color.GREEN);
            labelBrand.setText("Successfuly deleted brand!");
            //Refreshing the ComboBox
            setBrandsBox();
        } else {
            labelBrand.setTextFill(Color.RED);
            labelBrand.setText("Deleting brand FAILED!");
        }
    }

    /* ---------------------------------------------------------------------------- */

}
