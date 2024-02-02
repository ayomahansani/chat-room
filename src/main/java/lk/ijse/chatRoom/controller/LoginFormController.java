package lk.ijse.chatRoom.controller;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.scene.layout.AnchorPane;
import lk.ijse.chatRoom.client.Client;
import lk.ijse.chatRoom.dto.UserDTO;
import lk.ijse.chatRoom.server.Server;
import lk.ijse.chatRoom.model.UserModel;

import java.io.IOException;
import java.sql.SQLException;

public class LoginFormController {

    @FXML
    public AnchorPane loginPage;

    @FXML
    public Button forgetPasswordButton;

    @FXML
    public Button loginButton;

    @FXML
    public JFXButton registerBtn;

    @FXML
    public TextField txtPassword;

    @FXML
    public TextField txtUserName;

    @FXML
    public ImageView showProPic;

    public static String name;

    public void initialize() throws IOException{
        txtUserName.requestFocus();
        startServer();
    }

    private void startServer() throws IOException {
        Server server = Server.getServerSocket();
        Thread thread = new Thread(server);
        thread.start();
    }

    @FXML
    void loginButtonOnAction(ActionEvent event) throws IOException {

        if (!txtUserName.getText().isEmpty() || !txtPassword.getText().isEmpty()) {

            String username = txtUserName.getText();
            String password = txtPassword.getText();

            boolean isExists = false;

            try {
                isExists = UserModel.existsUser(username);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            if (isExists) {
                try {
                    UserDTO userDTO = UserModel.getUserDetails(username);

                    if (!userDTO.getPassword().equals(password)) {
                        new Alert(Alert.AlertType.WARNING, "Invalid username or password", ButtonType.OK).show();

                    } else {

                        if (userDTO.getImage() != null){
                            showProPic = new ImageView(new Image(userDTO.getImage()));
                        } else {
                            showProPic = new ImageView(new Image("/assets/icons/username_pink.png"));
                        }

                        loadClient();
                        txtUserName.clear();
                        txtPassword.clear();
                    }

                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }

            }else{
                new Alert(Alert.AlertType.WARNING, "Invalid username or password!", ButtonType.OK).show();
            }
        }else{
            new Alert(Alert.AlertType.WARNING, "Please fill all fields!", ButtonType.OK).show();
        }
    }

    private void loadClient() throws IOException {
        Client client = new Client(txtUserName.getText(), showProPic);
        Thread thread = new Thread(client);
        thread.start();
    }


    @FXML
    void registerButtonOnAction(ActionEvent event) throws IOException {
        Parent rootNode = FXMLLoader.load(this.getClass().getResource("/view/signup_form.fxml"));
        Scene scene = new Scene(rootNode);
        loginPage.getChildren().clear();

        Stage primaryStage = (Stage) loginPage.getScene().getWindow();
        primaryStage.setScene(scene);
        primaryStage.setTitle("Signup Form");
    }

    @FXML
    void forgetPasswordOnAction(ActionEvent event) throws IOException {
        Parent anchorPane = FXMLLoader.load(getClass().getResource("/view/reset_password_form.fxml"));
        Scene scene = new Scene(anchorPane);

        Stage stage = new Stage();
        stage.setTitle("Reset Password Form");
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    @FXML
    void txtUserNameOnAction(ActionEvent event) {
        txtPassword.requestFocus();
    }

    @FXML
    void txtPasswordOnAction(ActionEvent event) throws IOException {
        loginButtonOnAction(event);
    }


}
