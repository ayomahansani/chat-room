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
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lk.ijse.chatRoom.dto.UserDTO;
import lk.ijse.chatRoom.model.UserModel;

import java.io.*;
import java.sql.SQLException;

public class SignupFormController {

    @FXML
    public AnchorPane signupForm;

    @FXML
    public TextField txtPassword;

    @FXML
    public TextField txtUserName;

    @FXML
    public JFXButton loginBtn;

    @FXML
    public ImageView profilePic;

    @FXML
    public Button btnImageChooser;

    @FXML
    public Button signupButton;

    private File file;

    @FXML
    void signupButtonOnAction(ActionEvent event) {

        if (!(txtUserName.getText().isEmpty() || txtPassword.getText().isEmpty())) {

            try {

                String username = txtUserName.getText();
                String password = txtPassword.getText();

                    boolean isExists = UserModel.existsUser(username);

                    if (!isExists) {
                        boolean isSaved;

                        if (file != null) {
                            InputStream inputStream = new FileInputStream(file);
                            isSaved = UserModel.saveUser(new UserDTO(username, password, inputStream));
                        } else {
                            isSaved = UserModel.saveUser(new UserDTO(username, password, null));
                        }
                        if (isSaved) {
                            new Alert(Alert.AlertType.INFORMATION, "Signup Successfully!", ButtonType.OK).show();
                            loginBtnOnAction(event);
                        }
                    } else {
                        new Alert(Alert.AlertType.WARNING, "Username already exists", ButtonType.OK).show();
                    }

            }catch(FileNotFoundException | SQLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        } else {
            new Alert(Alert.AlertType.WARNING, "All fields are required", ButtonType.OK).show();
        }
    }

    @FXML
    void loginBtnOnAction(ActionEvent event) throws IOException {
        Parent rootNode = FXMLLoader.load(this.getClass().getResource("/view/login_form.fxml"));
        Scene scene = new Scene(rootNode);
        signupForm.getChildren().clear();

        Stage primaryStage = (Stage) signupForm.getScene().getWindow();
        primaryStage.setScene(scene);
        primaryStage.setTitle("Login Form");
    }

    @FXML
    void btnImageChooserOnAction(ActionEvent event) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select the image");

        FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.jpeg", "*.png", "*.gif", "*.bmp");
        fileChooser.getExtensionFilters().add(imageFilter);

        file = fileChooser.showOpenDialog(txtUserName.getScene().getWindow());

        if (file != null) {
            try {
                FileInputStream fileInputStream = new FileInputStream(file);
                profilePic.setImage(new Image(fileInputStream));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void txtUserNameOnAction(ActionEvent event) {
        txtPassword.requestFocus();
    }

    @FXML
    void txtPasswordOnAction(ActionEvent event) {
        signupButtonOnAction(event);
    }

}
