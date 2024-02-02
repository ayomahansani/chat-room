package lk.ijse.chatRoom.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lk.ijse.chatRoom.client.Client;
import lombok.Setter;

import java.io.*;
import java.nio.file.Files;


public class ChatFormController {

    @FXML
    public AnchorPane chatForm;

    @FXML
    public ScrollPane scrollpane;

    @FXML
    public VBox vBox;

    @FXML
    public AnchorPane header;

    @FXML
    public Label lblClientName;

    @FXML
    public Button profileBtn;

    @FXML
    public ImageView showProPic;

    @FXML
    public AnchorPane textArea;

    @FXML
    public Button sendButton;

    @FXML
    public Button emojiIcon;

    @FXML
    public Button cameraButton;

    @FXML
    public TextField txtMsg;

    @FXML
    public AnchorPane emojiPane;

    private Client client;
    private File file;

    public void initialize(){
        lblClientName.setText(LoginFormController.name);
        emojiPane.setVisible(false);
    }

    public void setClient(Client client) throws IOException {
        this.client = client;

        String msg =" joined the chat room";
        appendText(msg);

        client.sendMessage(msg);
    }

    void appendText(String message) {

        if (message.startsWith(" joined")) {
            HBox hBox = new HBox();
            hBox.setStyle("-fx-alignment: center;-fx-fill-height: true;-fx-min-height: 50;-fx-pref-width: 520;-fx-max-width: 520;-fx-padding: 10");

            Label messageLbl = new Label(message);
            messageLbl.setStyle("-fx-background-color: rgb(128,128,128);-fx-background-radius:15;-fx-font-size: 14;-fx-font-weight: normal;-fx-text-fill: white;-fx-wrap-text: true;-fx-alignment: center-left;-fx-content-display: left;-fx-padding: 10;-fx-max-width: 350;");

            hBox.getChildren().add(messageLbl);
            vBox.getChildren().add(hBox);

        } else {
            HBox hBox = new HBox();
            hBox.setStyle("-fx-alignment: center-right;-fx-fill-height: true;-fx-min-height: 50;-fx-pref-width: 520;-fx-max-width: 520;-fx-padding: 10");

            Label messageLbl = new Label(message);
            messageLbl.setStyle("-fx-background-color:  #e88997;-fx-background-radius:15;-fx-font-size: 14;-fx-font-weight: normal;-fx-text-fill: white;-fx-wrap-text: true;-fx-alignment: center-left;-fx-content-display: left;-fx-padding: 10;-fx-max-width: 350;");

            hBox.getChildren().add(messageLbl);
            vBox.getChildren().add(hBox);

        }
    }

    @FXML
    void cameraButtonOnAction(ActionEvent event) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image File");

        FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg","*.gif");
        fileChooser.getExtensionFilters().add(imageFilter);

        File selectedFile = fileChooser.showOpenDialog(new Stage());

        if (selectedFile != null) {
            try {
                byte[] bytes = Files.readAllBytes(selectedFile.toPath());

                HBox hBox = new HBox();
                hBox.setStyle("-fx-fill-height: true; -fx-min-height: 50; -fx-pref-width: 520; -fx-max-width: 520; -fx-padding: 10; -fx-alignment: center-right;");


                ImageView imageView = new ImageView(new Image(new FileInputStream(selectedFile)));
                imageView.setStyle("-fx-padding: 10px;");
                imageView.setFitHeight(150);
                imageView.setFitWidth(150);

                hBox.getChildren().addAll(imageView);
                vBox.getChildren().add(hBox);

                client.sendImage(bytes);

            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    @FXML
    void sendButtonOnAction(ActionEvent event) {
        try {

            String text = txtMsg.getText();

            if (text != null) {
                appendText(text);
                client.sendMessage(text);
                txtMsg.clear();

            } else {
                ButtonType ok = new ButtonType("Ok");
                ButtonType cancel = new ButtonType("Cancel");

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Empty message. Is it ok?", ok, cancel);
                alert.showAndWait();
                ButtonType result = alert.getResult();

                if (result.equals(ok)) {
                    client.sendMessage(null);
                }
                txtMsg.clear();
            }

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void writeMessage(String message) {

        HBox hBox = new HBox();
        hBox.setStyle("-fx-alignment: center-left;-fx-fill-height: true;-fx-min-height: 50;-fx-pref-width: 520;-fx-max-width: 520;-fx-padding: 10");

        Label messageLbl = new Label(message);
        messageLbl.setStyle("-fx-background-color:   #836474;-fx-background-radius:15;-fx-font-size: 14;-fx-font-weight: normal;-fx-text-fill: white;-fx-wrap-text: true;-fx-alignment: center-left;-fx-content-display: left;-fx-padding: 10;-fx-max-width: 350;");

        hBox.getChildren().add(messageLbl);
        Platform.runLater(() -> vBox.getChildren().add(hBox));

    }

    public void setImage(byte[] bytes, String sender) {

        HBox hBox = new HBox();

        Label messageLbl = new Label(sender);
        messageLbl.setStyle("-fx-background-color:   #836474;-fx-background-radius:15;-fx-font-size: 14;-fx-font-weight: normal;-fx-text-fill: white;-fx-wrap-text: true;-fx-alignment: center;-fx-content-display: left;-fx-padding: 10;-fx-max-width: 350;");


        hBox.setStyle("-fx-fill-height: true; -fx-min-height: 50; -fx-pref-width: 520; -fx-max-width: 520; -fx-padding: 10; " + (sender.equals(client.getName()) ? "-fx-alignment: center-right;" : "-fx-alignment: center-left;"));

        Platform.runLater(() -> {
            ImageView imageView = new ImageView(new Image(new ByteArrayInputStream(bytes)));
            imageView.setStyle("-fx-padding: 10px;");
            imageView.setFitHeight(150);
            imageView.setFitWidth(150);

            hBox.getChildren().addAll(messageLbl, imageView);
            vBox.getChildren().add(hBox);
        });
    }

    @FXML
    void emojiButtonOnAction(ActionEvent event) {
        emojiPane.setVisible(true);
    }

    @FXML
    void profileBtnOnAction(ActionEvent event) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Edit the profile pic");
        FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.jpeg", "*.png", "*.gif", "*.bmp");
        fileChooser.getExtensionFilters().add(imageFilter);

        file = fileChooser.showOpenDialog(lblClientName.getScene().getWindow());

        if (file != null) {
            try {
                FileInputStream fileInputStream = new FileInputStream(file);
                showProPic.setImage(new Image(fileInputStream));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void faceWithTearsOfJoyOnAction(MouseEvent event) {
        txtMsg.appendText("\uD83D\uDE02");
        emojiPane.setVisible(false);
    }

    @FXML
    void grinningFaceEmojiOnAction(MouseEvent event) {
        txtMsg.appendText("\uD83D\uDE00");
        emojiPane.setVisible(false);
    }

    @FXML
    void grinningFaceWithSweatOnAction(MouseEvent event) {
        txtMsg.appendText("\uD83D\uDE05");
        emojiPane.setVisible(false);
    }

    @FXML
    void grinningSquintingOnAction(MouseEvent event) {
        txtMsg.appendText("\uD83D\uDE06");
        emojiPane.setVisible(false);
    }

    @FXML
    void smilingFaceWithHeartEyesOnAction(MouseEvent event) {
        txtMsg.appendText("\uD83D\uDE0D");
        emojiPane.setVisible(false);
    }

    @FXML
    void smilingFaceWithHornsOnAction(MouseEvent event) {
        txtMsg.appendText("\uD83D\uDE08");
        emojiPane.setVisible(false);
    }

    @FXML
    void smilingFaceWithOpenHandsOnAction(MouseEvent event) {
        txtMsg.appendText("\uD83E\uDD17");
        emojiPane.setVisible(false);
    }

    @FXML
    void sunglassesFaceOnAction(MouseEvent event) {
        txtMsg.appendText("\uD83D\uDE0E");
        emojiPane.setVisible(false);
    }

    @FXML
    public void thumbsUpOnAction(MouseEvent mouseEvent) {
        txtMsg.appendText("\uD83D\uDC4D");
        emojiPane.setVisible(false);
    }

    @FXML
    void txtMessageOnAction(ActionEvent event) {
        sendButtonOnAction(event);
    }

}
