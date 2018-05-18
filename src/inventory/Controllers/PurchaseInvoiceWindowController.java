package inventory.Controllers;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import inventory.Entity.*;
import inventory.Model.*;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class PurchaseInvoiceWindowController {

    /* ------------------------------- FXML Objects ------------------------------- */

    @FXML
    private JFXDatePicker datePicker;

    @FXML
    private Label label, labelTotalCost;

    @FXML
    private JFXTextField txtSearch;

    @FXML
    private TableView<Invoice> invoicesTable;

    @FXML
    private TableColumn<Invoice, Integer> invoiceNumberColumn;

    @FXML
    private TableColumn<Invoice, String> invoiceDateColumn;

    @FXML
    private TableView<Purchase> purchasesTable;

    @FXML
    private TableColumn<Purchase, String> productColumn;

    @FXML
    private TableColumn<Purchase, String> supplierColumn;

    @FXML
    private TableColumn<Purchase, Float> costPriceColumn;

    @FXML
    private TableColumn<Purchase, Integer> quantityColumn;

    @FXML
    private TableColumn<Purchase, Float> totalCostColumn;

    /* ------------------------------- Other Objects & Variables ------------------------------- */

    private Service invoiceService;
    private Service purchaseService;

    private static int invoiceID;

    private double xOffset = 0;
    private double yOffset = 0;

    private Stage purchasesStage = new Stage();

    /* ------------------------------- Initialize Method ------------------------------- */

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    public void initialize() {
        //Initializing invoice service for the Invoices TableView
        invoiceService = new Service<ObservableList<Invoice>>() {
            @Override
            protected Task<ObservableList<Invoice>> createTask() {
                return new Task<ObservableList<Invoice>>() {
                    @Override
                    protected ObservableList<Invoice> call() throws Exception {
                        return FXCollections.observableArrayList(InvoiceSupplierModel.getInstance().queryInvoices());
                    }
                };
            }
        };
        //Initializing purchase service for the Purchases TableView
        purchaseService = new Service<ObservableList<Purchase>>() {
            @Override
            protected Task<ObservableList<Purchase>> createTask() {
                return new Task<ObservableList<Purchase>>() {
                    @Override
                    protected ObservableList<Purchase> call() throws Exception {
                        return FXCollections.observableArrayList(PurchaseModel.getInstance().queryPurchasesByInvoice(invoiceID));
                    }
                };
            }
        };

        //Setting up Cell Value Factory with Value Properties in the invoicesTable
        invoiceNumberColumn.setCellValueFactory(cellData -> cellData.getValue().getInvoiceIDProperty().asObject());
        invoiceDateColumn.setCellValueFactory(cellData -> cellData.getValue().getDateProperty());

        //Setting up Cell Value Factory with Value Properties in the purchasesTable
        productColumn.setCellValueFactory(cellData -> cellData.getValue().getProductNameProperty());
        supplierColumn.setCellValueFactory(cellData -> cellData.getValue().getSupplierNameProperty());
        costPriceColumn.setCellValueFactory(cellData -> cellData.getValue().getCostPriceProperty().asObject());
        quantityColumn.setCellValueFactory(cellData -> cellData.getValue().getQuantityProperty().asObject());
        totalCostColumn.setCellValueFactory(cellData -> cellData.getValue().getTotalCostProperty().asObject());

        //Binding data of the TableView and the observable list coming from the database
        invoicesTable.itemsProperty().bind(invoiceService.valueProperty());
        //Binding data of the TableView and the observable list coming from the database
        purchasesTable.itemsProperty().bind(purchaseService.valueProperty());
        //Setting up data in the TableViews
        setInvoicesTableView();
        setPurchasesTableView();
    }
    /* ------------------------------- Other Methods ------------------------------- */

    /**
     * Sets up the Invoices TableView
     */
    public void setInvoicesTableView() {
        //Restarting the invoice service if its state is already SUCCEEDED, otherwise just start the service
        if (invoiceService.getState() == Service.State.SUCCEEDED) {
            invoiceService.reset();
            invoiceService.start();
        } else if (invoiceService.getState() == Service.State.READY) {
            invoiceService.start();
        }
    }

    /**
     * Sets up the Purchases TableView
     */
    public void setPurchasesTableView() {
        if (invoicesTable.getSelectionModel().getSelectedItem() != null) {
            //Getting selected invoice from the Invoices TableView
            invoiceID = invoicesTable.getSelectionModel().getSelectedItem().getInvoiceID();
            //Restarting the purchase service if its state is already SUCCEEDED, otherwise just start the service
            if (purchaseService.getState() == Service.State.SUCCEEDED) {
                purchaseService.reset();
                purchaseService.start();
            } else if (purchaseService.getState() == Service.State.READY) {
                purchaseService.start();
            }
            List<Purchase> purchasesOfInvoice = (List<Purchase>) purchaseService.getValue();
            if (purchasesOfInvoice != null) {
                float totalCostOfInvoice = 0;
                for (Purchase purchase : purchasesOfInvoice){
                    totalCostOfInvoice += purchase.getTotalCost();
                }
                labelTotalCost.setText(String.valueOf(totalCostOfInvoice));
            }
        }
    }

    /**
     * Style method
     */
    private void style(Stage stage, Parent root) {
        //Making the stage without borders
        stage.initStyle(StageStyle.UNDECORATED);
        //Setting the stage's coordinates
        stage.setX(80);
        stage.setY(83);
        //Making the window draggable
        root.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                xOffset = stage.getX() - event.getScreenX();
                yOffset = stage.getY() - event.getScreenY();
            }
        });
        root.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                stage.setX(event.getScreenX() + xOffset);
                stage.setY(event.getScreenY() + yOffset);
            }
        });

        //Setting the application's icon
        stage.getIcons().add(new Image(this.getClass().getResourceAsStream("../Resources/Pictures/icons8_Forumbee_96px.png")));
        //Making the stage non-resizable
        stage.setResizable(false);
        //Getting focus for the scene
        root.requestFocus();
        //Showing the stage to the user
        stage.showAndWait();
    }

    /* ------------------------------- FXML Methods ------------------------------- */

    @FXML
    public void searchPurchases() {
        /*--!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!--*/
        FilteredList<Purchase> filteredPurchasesData = new FilteredList<>((ObservableList<Purchase>) purchaseService.getValue(), e -> true);
        txtSearch.textProperty().addListener((observableValue, oldValue, newValue) -> {
            filteredPurchasesData.setPredicate((Predicate<? super Purchase>) purchase -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (purchase.getProductName().toLowerCase().contains(lowerCaseFilter)
                        || purchase.getSupplierName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
        });
        SortedList<Purchase> sortedPurchasesData = new SortedList<>(filteredPurchasesData);
        sortedPurchasesData.comparatorProperty().bind(purchasesTable.comparatorProperty());

        //Need a way to show (or bind) results to my principal and only TableView after that I have already bidden it with the service
    }

    /**
     * Clears all fields
     */
    @FXML
    public void clearFields() {
        label.setText("");
        label.setTextFill(Color.RED);
        if (!datePicker.getEditor().getText().isEmpty()){
            datePicker.getEditor().clear();
        }
    }

    /**
     * Inserting new invoice
     */
    @FXML
    public void insertInvoice() {
        //Int variable to check if insert is successful
        int isSuccessful = -1;
        //Inserting the invoice in the database
        try {
            //Setting Auto Commit Off (Begin Transaction)
            Datasource.getInstance().getConnection().setAutoCommit(false);
            isSuccessful = InvoiceSupplierModel.getInstance().insertInvoice();
        } catch (SQLException exception) {
            System.out.println("Couldn't Insert Invoice: " + exception.getMessage());
            try {
                isSuccessful = -1;
                //Performing Rollback
                System.out.println("Performing rollback");
                Datasource.getInstance().getConnection().rollback();
                //Resetting default commit behavior
                System.out.println("Resetting default commit behavior");
                Datasource.getInstance().getConnection().setAutoCommit(true);
            } catch(SQLException exception2) {
                System.out.println("Oh boy! Things are really bad! " + exception2.getMessage());
            }
        }


        //Testing operation has been executed successfully
        if (isSuccessful != -1) {
            invoiceID = isSuccessful;
            try {
                //Loading the window
                Parent rootAddPurchases = FXMLLoader.load(getClass().getResource("../Resources/fxml/AddPurchaseWindow.fxml"));
                //Setting the scene to the stage
                purchasesStage.setScene(new Scene(rootAddPurchases));
                //Getting focus for the scene
                rootAddPurchases.requestFocus();
                //Passing the stage and the scene as parameters to the style method
                style(purchasesStage, rootAddPurchases);
                //Reinitializing the stage for future use
                purchasesStage = new Stage();
                //Checking if the list of purchases of that invoice isn't empty, else perform a rollback
                invoicesTable.getSelectionModel().select(InvoiceSupplierModel.getInstance().getInvoiceByID(invoiceID));
                setPurchasesTableView();
//                List<Purchase> purchasesOfInvoice = (List<Purchase>) purchaseService.getValue();
//                if (purchasesOfInvoice == null){
//                    System.out.println("Performing a rollback because list of purchases of that invoice is empty");
//                    Datasource.getInstance().getConnection().rollback();
//                    invoiceID = -1;
//                    invoicesTable.getSelectionModel().clearSelection();
//                } else {
                    //Committing changes to the database
                    Datasource.getInstance().getConnection().commit();
                    System.out.println("Committed changes to the database successfully!");
//                }
            } catch (IOException exception3){
                invoiceID = -1;
                invoicesTable.getSelectionModel().clearSelection();
                System.out.println("Couldn't load AddPurchasesWindow: " + exception3.getMessage());
                exception3.printStackTrace();
                //Perform a rollback because catching the error without doing that will commit changes in the finally clause
                try {
                    Datasource.getInstance().getConnection().rollback();
                } catch (SQLException anotherException){
                    System.out.println("Couldn't perform a rollback: " + anotherException.getMessage());
                }
            } catch (SQLException exception4){
                invoiceID = -1;
                invoicesTable.getSelectionModel().clearSelection();
                System.out.println("Couldn't commit changes to the database or perform a rollback: " + exception4.getMessage());
            } finally {
                try {
                    //Resetting default commit behavior
                    System.out.println("Resetting default commit behavior");
                    Datasource.getInstance().getConnection().setAutoCommit(true);
                } catch (SQLException exception5){
                    System.out.println("Couldn't reset autocommit behavior: " + exception5.getMessage());
                }
            }
            //Refreshing the TableViews
            setInvoicesTableView();
            setPurchasesTableView();
            //Requesting focus for the TableView
            invoicesTable.requestFocus();
            if (invoiceID != -1){
                //Selecting the new product in the TableView
                invoicesTable.getSelectionModel().select(InvoiceSupplierModel.getInstance().getInvoiceByID(invoiceID));
                invoicesTable.getFocusModel().focus(invoicesTable.getSelectionModel().getSelectedIndex());
            }
        } else {
            label.setTextFill(Color.RED);
            label.setText("Failed Creating an Invoice!");
        }
    }

    /* ------------------------------- Getters & Setters ------------------------------- */

    public static int getInvoiceID() {
        return invoiceID;
    }

    public static void setInvoiceID(int invoiceID) {
        invoiceID = invoiceID;
    }

    /* ---------------------------------------------------------------------------- */

}
