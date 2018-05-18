package inventory.Controllers;

import inventory.Model.EmployeeModel;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class MainWindowController {

    /* ------------------------------- Style Variables ------------------------------- */

    private double xOffset = 0;
    private double yOffset = 0;

    /* ------------------------------- Stages Ordering ------------------------------- */
    //True when stage is hidden, false when stage is appearing

    boolean productsBoolean = true;
    boolean salesBoolean = true;
    boolean purchasesBoolean = true;
    boolean suppliersBoolean = true;

    /* ------------------------------- Stage Objects ------------------------------- */

    Stage productsStage = new Stage();
    Stage purchasesStage = new Stage();
    Stage saleStage = new Stage();
    Stage suppliersStage = new Stage();

    /* ------------------------------- FXML Objects ------------------------------- */

    @FXML
    private Label nameLabel;

    /* ------------------------------- Initialize Method ------------------------------- */

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    public void initialize(){
        //Setting the label text to the employee's name
        nameLabel.setText(EmployeeModel.getInstance().getEmployeeByUsername(LoginFormController.getUsername()).getEmployeeName());
    }

    /* ------------------------------- Style Method ------------------------------- */

    private void style(Stage stage, Parent root) {
        //Making the stage without borders
        stage.initStyle(StageStyle.UNDECORATED);
        //Setting the stage's coordinates
        stage.setX(80);
        stage.setY(80);
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
        stage.show();
    }

    //Custom style method for the purchase window
    private void stylePurchase(Stage stage, Parent root) {
        //Making the stage without borders
        stage.initStyle(StageStyle.UNDECORATED);
        //Setting the stage's coordinates
        stage.setX(55);
        stage.setY(80);
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
        stage.show();
    }

    /* ------------------------------- FXML Methods ------------------------------- */

    //Products Button Action
    @FXML
    public void buttonProductsAction(ActionEvent event) {
        //Check if other stages are hidden or not
        if (!purchasesBoolean){
            //When stage not hidden, hide it
            purchasesStage.hide();
            //Reinitializing stage for future use
            purchasesStage = new Stage();
            //Set its boolean to true
            purchasesBoolean = true;
        } else if (!salesBoolean){
            //When stage not hidden, hide it
            saleStage.hide();
            //Reinitializing stage for future use
            saleStage = new Stage();
            //Set its boolean to true
            salesBoolean = true;
        } else if (!suppliersBoolean){
            //When stage not hidden, hide it
            suppliersStage.hide();
            //Reinitializing stage for future use
            suppliersStage = new Stage();
            //Set its boolean to true
            suppliersBoolean = true;
        }

        //Check if the stage is hidden or not
        if (productsBoolean){
            //When stage is hidden
            productsBoolean = false;
            try {
                //Loading the window
                Parent rootProducts = FXMLLoader.load(getClass().getResource("../Resources/fxml/ProductsWindow.fxml"));
                //Setting the scene to the stage
                productsStage.setScene(new Scene(rootProducts));
                //Getting focus for the scene
                rootProducts.requestFocus();
                //Passing the stage and the scene as parameters to the style method
                style(productsStage, rootProducts);

            } catch (IOException exception){
                System.out.println("Couldn't load ProductsWindow: " + exception.getMessage());
                exception.printStackTrace();
            }
        } else {
            //When stage is appearing
            productsStage.hide();
            //Reinitializing the stage for future use
            productsStage = new Stage();
            productsBoolean = true;
        }
    }

    //Purchases Button Action
    @FXML
    public void buttonPurchasesAction(ActionEvent event) {
        //Check if other stages are hidden or not
        if (!productsBoolean){
            //When stage not hidden, hide it
            productsStage.hide();
            //Reinitializing stage for future use
            productsStage = new Stage();
            //Set its boolean to true
            productsBoolean = true;
        } else if (!salesBoolean){
            //When stage not hidden, hide it
            saleStage.hide();
            //Reinitializing stage for future use
            saleStage = new Stage();
            //Set its boolean to true
            salesBoolean = true;
        } else if (!suppliersBoolean){
            //When stage not hidden, hide it
            suppliersStage.hide();
            //Reinitializing stage for future use
            suppliersStage = new Stage();
            //Set its boolean to true
            suppliersBoolean = true;
        }

        //Check if the stage is hidden or not
        if (purchasesBoolean){
            //When stage is hidden
            purchasesBoolean = false;
            try {
                //Loading the window
                Parent rootPurchases = FXMLLoader.load(getClass().getResource("../Resources/fxml/PurchaseInvoiceWindow.fxml"));
                //Setting the scene to the stage
                purchasesStage.setScene(new Scene(rootPurchases));
                //Getting focus for the scene
                rootPurchases.requestFocus();
                //Passing the stage and the scene as parameters to the style method
                stylePurchase(purchasesStage, rootPurchases);

            } catch (IOException exception){
                System.out.println("Couldn't load PurchaseInvoiceWindow: " + exception.getMessage());
                exception.printStackTrace();
            }
        } else {
            //When stage is appearing
            purchasesStage.hide();
            //Reinitializing the stage for future use
            purchasesStage = new Stage();
            purchasesBoolean = true;
        }
    }

    //Sales Button Action
    @FXML
    public void buttonSalesAction(ActionEvent event) {
        //Check if other stages are hidden or not
        if (!productsBoolean){
            //When stage not hidden, hide it
            productsStage.hide();
            //Reinitializing stage for future use
            productsStage = new Stage();
            //Set its boolean to true
            productsBoolean = true;
        } else if (!purchasesBoolean){
            //When stage not hidden, hide it
            purchasesStage.hide();
            //Reinitializing stage for future use
            purchasesStage = new Stage();
            //Set its boolean to true
            purchasesBoolean = true;
        } else if (!suppliersBoolean){
            //When stage not hidden, hide it
            suppliersStage.hide();
            //Reinitializing stage for future use
            suppliersStage = new Stage();
            //Set its boolean to true
            suppliersBoolean = true;
        }

        //Check if the stage is hidden or not
        if (salesBoolean){
            //When stage is hidden
            salesBoolean = false;
            try {
                //Loading the window
                Parent rootSales = FXMLLoader.load(getClass().getResource("../Resources/fxml/SalesWindow.fxml"));
                //Setting the scene to the stage
                saleStage.setScene(new Scene(rootSales));
                //Getting focus for the scene
                rootSales.requestFocus();
                //Passing the stage and the scene as parameters to the style method
                style(saleStage, rootSales);

            } catch (IOException exception){
                System.out.println("Couldn't load SalesWindow: " + exception.getMessage());
                exception.printStackTrace();
            }
        } else {
            //When stage is appearing
            saleStage.hide();
            //Reinitializing the stage for future use
            saleStage = new Stage();
            salesBoolean = true;
        }
    }

    //Supplier Button Action
    @FXML
    public void buttonSuppliersAction(ActionEvent event) {
        //Check if other stages are hidden or not
        if (!productsBoolean){
            //When stage not hidden, hide it
            productsStage.hide();
            //Reinitializing stage for future use
            productsStage = new Stage();
            //Set its boolean to true
            productsBoolean = true;
        } else if (!purchasesBoolean){
            //When stage not hidden, hide it
            purchasesStage.hide();
            //Reinitializing stage for future use
            purchasesStage = new Stage();
            //Set its boolean to true
            purchasesBoolean = true;
        } else if (!salesBoolean){
            //When stage not hidden, hide it
            saleStage.hide();
            //Reinitializing stage for future use
            saleStage = new Stage();
            //Set its boolean to true
            salesBoolean = true;
        }

        //Check if the stage is hidden or not
        if (suppliersBoolean){
            //When stage is hidden
            suppliersBoolean = false;
            try {
                //Loading the window
                Parent rootSuppliers = FXMLLoader.load(getClass().getResource("../Resources/fxml/SuppliersWindow.fxml"));
                //Setting the scene to the stage
                suppliersStage.setScene(new Scene(rootSuppliers));
                //Getting focus for the scene
                rootSuppliers.requestFocus();
                //Passing the stage and the scene as parameters to the style method
                style(suppliersStage, rootSuppliers);

            } catch (IOException exception){
                System.out.println("Couldn't load SuppliersWindow: " + exception.getMessage());
                exception.printStackTrace();
            }
        } else {
            //When stage is appearing
            suppliersStage.hide();
            //Reinitializing the stage for future use
            suppliersStage = new Stage();
            suppliersBoolean = true;
        }
    }

    //Exit Button Action
    @FXML
    public void exitButtonAction(ActionEvent event){
        //Setting an confirmation pop-up message and showing it to the user
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to exit!!",
                ButtonType.YES, ButtonType.NO);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.YES) {
            Platform.exit();
        }
    }


    /* ---------------------------------------------------------------------------- */
}
