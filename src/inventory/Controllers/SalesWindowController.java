package inventory.Controllers;

import com.jfoenix.controls.JFXTextField;
import inventory.Entity.Sale;
import inventory.Model.Datasource;
import inventory.Model.InvoiceClientModel;
import inventory.Model.InvoiceSupplierModel;
import inventory.Model.SaleModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.SQLException;
import java.util.function.Predicate;

public class SalesWindowController {

    /* ------------------------------- FXML Objects ------------------------------- */

    @FXML
    private Label label;

    @FXML
    private JFXTextField txtSearch;

    @FXML
    private TableView<Sale> salesTable;

    @FXML
    private TableColumn<Sale, Integer> saleIDColumn;

    @FXML
    private TableColumn<Sale, String> brandNameColumn;

    @FXML
    private TableColumn<Sale, String> productNameColumn;

    @FXML
    private TableColumn<Sale, Float> productSellingPriceColumn;

    @FXML
    private TableColumn<Sale, Integer> saleQuantityColumn;

    @FXML
    private TableColumn<Sale, Float> saleTotalCostColumn;

    /* ------------------------------- Other Objects & Variables ------------------------------- */

    private Service saleService;

    private static int invoiceID;

    private double xOffset = 0;
    private double yOffset = 0;

    private Stage addSaleStage = new Stage();

    /* ------------------------------- Initialize Method ------------------------------- */

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    public void initialize() {
        //Initializing sale service for the TableView
        saleService = new Service<ObservableList<Sale>>() {
            @Override
            protected Task<ObservableList<Sale>> createTask() {
                return new Task<ObservableList<Sale>>() {
                    @Override
                    protected ObservableList<Sale> call() throws Exception {
                        return FXCollections.observableArrayList(SaleModel.getInstance().querySales());
                    }
                };
            }
        };

        //Setting up Cell Value Factory with Value Properties in the salesTable
        saleIDColumn.setCellValueFactory(cellData -> cellData.getValue().getSaleIDProperty().asObject());
        brandNameColumn.setCellValueFactory(cellData -> cellData.getValue().getBrandNameProperty());
        productNameColumn.setCellValueFactory(cellData -> cellData.getValue().getProductNameProperty());
        productSellingPriceColumn.setCellValueFactory(cellData -> cellData.getValue().getSellingPriceProperty().asObject());
        saleQuantityColumn.setCellValueFactory(cellData -> cellData.getValue().getQuantityProperty().asObject());
        saleTotalCostColumn.setCellValueFactory(cellData -> cellData.getValue().getTotalCostProperty().asObject());

        //Binding data of the TableView and the observable list coming from the database
        salesTable.itemsProperty().bind(saleService.valueProperty());
        //Setting up data in the TableViews
        setSalesTableView();
    }
    /* ------------------------------- Other Methods ------------------------------- */

    /**
     * Sets up the Invoices TableView
     */
    public void setSalesTableView() {
        //Restarting the sale service if its state is already SUCCEEDED, otherwise just start the service
        if (saleService.getState() == Service.State.SUCCEEDED) {
            saleService.reset();
            saleService.start();
        } else if (saleService.getState() == Service.State.READY) {
            saleService.start();
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
    public void searchSales() {
        /*--!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!--*/
        FilteredList<Sale> filteredSalesData = new FilteredList<>((ObservableList<Sale>) saleService.getValue(), e -> true);
        txtSearch.textProperty().addListener((observableValue, oldValue, newValue) -> {
            filteredSalesData.setPredicate((Predicate<? super Sale>) sale -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (sale.getProductName().toLowerCase().contains(lowerCaseFilter)
                        || sale.getBrandName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
        });
        SortedList<Sale> sortedSalesData = new SortedList<>(filteredSalesData);
        sortedSalesData.comparatorProperty().bind(salesTable.comparatorProperty());

        //Need a way to show (or bind) results to my principal and only TableView after that I have already bidden it with the service
    }

    /**
     * Clears all fields
     */
    @FXML
    public void clearFields() {
        label.setText("");
        label.setTextFill(Color.RED);
    }

    /**
     * Inserting new invoice
     */
    @FXML
    public void insertSale() {
        //Int variable to check if insert is successful
        int isSuccessful = -1;
        //Inserting the sale in the database
        try {
            //Setting Auto Commit Off (Begin Transaction)
            Datasource.getInstance().getConnection().setAutoCommit(false);
            isSuccessful = InvoiceClientModel.getInstance().insertInvoice();
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
                Parent rootAddSale = FXMLLoader.load(getClass().getResource("../Resources/fxml/AddSaleWindow.fxml"));
                //Setting the scene to the stage
                addSaleStage.setScene(new Scene(rootAddSale));
                //Getting focus for the scene
                rootAddSale.requestFocus();
                //Passing the stage and the scene as parameters to the style method
                style(addSaleStage, rootAddSale);
                //Reinitializing the stage for future use
                addSaleStage = new Stage();
                //Committing changes to the database
                Datasource.getInstance().getConnection().commit();
                System.out.println("Committed changes to the database successfully!");
            } catch (IOException exception3){
                invoiceID = -1;
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
            setSalesTableView();
            salesTable.refresh();
            salesTable.requestFocus();
            salesTable.getSelectionModel().selectFirst();
            salesTable.getFocusModel().focus(salesTable.getSelectionModel().getSelectedIndex());
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
