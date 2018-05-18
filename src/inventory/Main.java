package inventory;

import inventory.Model.Datasource;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {

    //Variables of setting window's coordinates
    private static double xOffset = 0;
    private static double yOffset = 0;

    @Override
    public void start(Stage primaryStage) throws Exception {
        //Loading the FXML file to a scene
        Parent root = FXMLLoader.load(getClass().getResource("Resources/fxml/LoginForm.fxml"));
        //Making the stage (window) without borders
        primaryStage.initStyle(StageStyle.UNDECORATED);

        //Making the window draggable
        root.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                xOffset = primaryStage.getX() - event.getScreenX();
                yOffset = primaryStage.getY() - event.getScreenY();
            }
        });
        root.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                primaryStage.setX(event.getScreenX() + xOffset);
                primaryStage.setY(event.getScreenY() + yOffset);
            }
        });

        //Setting title of the window
        primaryStage.setTitle("Inventory Management - University of Saïda");
        //Setting the application's icon
        primaryStage.getIcons().add(new Image(this.getClass().getResourceAsStream("Resources/Pictures/icons8_Forumbee_96px.png")));
        //Setting the scene to the stage
        primaryStage.setScene(new Scene(root));
        //Making the stage non-resizable
        primaryStage.setResizable(false);
        //Getting focus for the scene
        root.requestFocus();
        //Showing the stage to the user
        primaryStage.show();
    }

    @Override
    public void init() throws Exception {
        super.init();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        //Closing connection with the database
        Datasource.getInstance().close();
    }


    public static void main(String[] args) {
        //Opening connection with the database, if fails exiting the application
        if (!Datasource.getInstance().open()) {
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Couldn't Open!");
                alert.setHeaderText("Database Connection Error!");
                alert.setContentText("There was a problem during the opening of the database of the application! \n"
                        + "Please contact developer.. \n" + "Marouane Zoubir Guettatfi");
                alert.showAndWait();
                Platform.exit();
            });
        } else {
            launch(args);
        }
    }
}
