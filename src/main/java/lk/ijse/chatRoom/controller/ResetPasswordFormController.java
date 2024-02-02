package lk.ijse.chatRoom.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.chatRoom.model.UserModel;

import java.io.IOException;
import java.sql.SQLException;

public class ResetPasswordFormController {

    @FXML
    private AnchorPane resetPasswordForm;

    @FXML
    private TextField txtPassword;

    @FXML
    private TextField txtUserName;

    @FXML
    private Button btnReset;

    @FXML
    void btnResetPasswordOnAction(ActionEvent event) {

        String username = txtUserName.getText();

        try {
            boolean isExist = UserModel.existsUser(username);

            if(isExist){
                String password = txtPassword.getText();
                boolean isUpdated = UserModel.updatePassword(username, password);

                if(isUpdated){

                    new Alert(Alert.AlertType.CONFIRMATION, "Password Reset successful!!").show();

                }else{
                    new Alert(Alert.AlertType.INFORMATION, "Password Not Updated").show();
                }

            }else{
                new Alert(Alert.AlertType.INFORMATION, "Username Not Found").show();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void txtUserNameOnAction(ActionEvent event) {
        txtPassword.requestFocus();
    }

    @FXML
    void txtPasswordOnAction(ActionEvent event) {
        btnResetPasswordOnAction(event);
    }

}
