package inventory.Controllers;


import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import inventory.Model.EmployeeModel;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

import java.io.IOException;
import java.sql.SQLException;

public class LoginFormController {

    /* ------------------------------- FXML Objects ------------------------------- */

    @FXML
    private JFXTextField txtUsername;

    @FXML
    private JFXPasswordField txtPassword;

    @FXML
    private Label ValidationLabel;

    private static String username;
    private String password;

    /* ------------------------------- FXML Methods ------------------------------- */

    //Login Button Action
    @FXML
    public void loginAction(ActionEvent event) throws IOException {

        username = txtUsername.getText();
        password = txtPassword.getText();

        if (txtUsername.getText().trim().isEmpty() || txtPassword.getText().trim().isEmpty()) {

            //When Username field or Password field is empty
            ValidationLabel.setText("Fill Username And Password");
        } else {

            //Action when login informations are wrong
            if (!EmployeeModel.getInstance().checkLogin(username, password)){
                ValidationLabel.setText("Invalid Username or Password !!");
            } else {

                //Showing a Notification Slider
                TrayNotification tray = new TrayNotification();
                tray.setNotificationType(NotificationType.SUCCESS);
                tray.setTitle("Login Succeeded");
                tray.setMessage("Welcome, " + EmployeeModel.getInstance().getEmployeeByUsername(username).getEmployeeName());
                tray.setAnimationType(AnimationType.SLIDE);
                tray.showAndDismiss(Duration.millis(1500));
                //Hide the login window
                ((Node) event.getSource()).getScene().getWindow().hide();
                //Loading the main window
                Parent rootMain = FXMLLoader.load(getClass().getResource("../Resources/fxml/MainWindow.fxml"));
                //Creating a new stage
                Stage mainStage = new Stage();
                //Setting the scene to the stage
                mainStage.setScene(new Scene(rootMain));
                //Setting the stage's coordinates
                mainStage.setX(250);
                mainStage.setY(0);
                //Making the stage non-resizable
                mainStage.setResizable(false);
                //Making the stage without borders
                mainStage.initStyle(StageStyle.UNDECORATED);
                //Setting the application's icon
                mainStage.getIcons().add(new Image(this.getClass().getResourceAsStream("../Resources/Pictures/icons8_Forumbee_96px.png")));
                //Showing the stage to the user
                mainStage.show();
            }
        }
    }

    //Exit Button Action
    @FXML
    public void exitAction(ActionEvent event) {

        //Setting an confirmation pop-up message and showing it to the user
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to exit!!",
                ButtonType.YES, ButtonType.NO);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.YES) {
            Platform.exit();
        }
    }

    /* ------------------------------- Other Methods ------------------------------- */

    public static String getUsername() {
        return username;
    }

}
