package lk.ijse.chatRoom.client;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import lk.ijse.chatRoom.controller.ChatFormController;
import lombok.Getter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;

public class Client implements Runnable, Serializable {

    private final Socket socket;
    private final DataInputStream inputStream;
    private final DataOutputStream outputStream;
    private ChatFormController chatFormController;

    @Getter
    private final String name;

    @Getter
    private ImageView imageView;


    public Client(String name, ImageView imageView) throws IOException {
        this.name = name;
        if (imageView == null) {

        } else {
            this.imageView = imageView;
        }

        //  Open a remote socket with server address and port
        socket = new Socket("localhost", 1200);

        //  Use input stream for reading data from the server
        inputStream = new DataInputStream(socket.getInputStream());
        //  Use output stream for writing data to the server
        outputStream = new DataOutputStream(socket.getOutputStream());

        //  Send the client's input
        outputStream.writeUTF(name);
        outputStream.flush();

        try {
            loadScene();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }


    @Override
    public void run() {
        String message = "";
        while (!message.equals("exit")) {
            try {
                //  Receive the client's input
                message = inputStream.readUTF();
                if (message.equals("*image*")) {
                    receiveImage();
                } else {
                    chatFormController.writeMessage(message);
                }

            } catch (IOException e) {
                try {
                    socket.close();
                } catch (IOException ignored) {

                }
            }
        }
    }

    public void sendMessage(String msg) throws IOException {
        //  Send the client's input
        outputStream.writeUTF(msg);
        outputStream.flush();
    }

    public void sendImage(byte[] bytes) throws IOException {
        outputStream.writeUTF("*image*");
        outputStream.writeInt(bytes.length);
        outputStream.write(bytes);
        outputStream.flush();
    }

    private void loadScene() throws IOException {

        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/chat_form.fxml"));
        Parent parent = loader.load();

        chatFormController = loader.getController();
        chatFormController.setClient(this);
        chatFormController.lblClientName.setText(name);
        chatFormController.showProPic.setImage(imageView.getImage());

        stage.setResizable(false);
        stage.setScene(new Scene(parent));
        stage.setTitle(name + "'s Chat");
        stage.show();

        stage.setOnCloseRequest(event -> {
            try {
                inputStream.close();
                outputStream.close();
                socket.close();
            } catch (IOException e) {
                System.out.println(e);
            }
        });
    }

    private void receiveImage() throws IOException {

        //  Receive the client's input
        String utf = inputStream.readUTF();
        int size = inputStream.readInt();
        byte[] bytes = new byte[size];

        inputStream.readFully(bytes);
        System.out.println(name + "- Image received: from " + utf);
        chatFormController.setImage(bytes, utf);

    }
}
