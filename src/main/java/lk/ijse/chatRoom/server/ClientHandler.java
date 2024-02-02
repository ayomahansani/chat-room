package lk.ijse.chatRoom.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClientHandler implements Runnable{
    public static final List<ClientHandler> clientHandlerList = new ArrayList<>();
    private final Socket socket;
    private final DataInputStream inputStream;
    private final DataOutputStream outputStream;
    private final String clientName;

    public ClientHandler(Socket socket) throws IOException {
        this.socket = socket;

        //  Use input stream for reading data from the server
        inputStream = new DataInputStream(socket.getInputStream());
        //  Use output stream for writing data to the server
        outputStream = new DataOutputStream(socket.getOutputStream());

        //  Receive the client's input
        clientName = inputStream.readUTF();
        clientHandlerList.add(this);
    }

    @Override
    public void run() {

        l1: while (socket.isConnected()) {
            try {

                String utf = inputStream.readUTF();
                if (utf.equals("*image*")) {
                    receiveImage();
                } else {
                    for (ClientHandler handler : clientHandlerList) {
                        if (!handler.clientName.equals(clientName)) {
                            handler.sendMessage(clientName, utf);
                        }
                    }
                }
            } catch (IOException e) {

                clientHandlerList.remove(this);
                break;
            }
        }
    }

    public void sendMessage(String sender, String msg) throws IOException {
        //  Send the client's input
        outputStream.writeUTF(sender + ": " + msg);
        outputStream.flush();
    }

    private void receiveImage() throws IOException {
        int size = inputStream.readInt();
        byte[] bytes = new byte[size];
        inputStream.readFully(bytes);

        for (ClientHandler handler : clientHandlerList) {
            if (!handler.clientName.equals(clientName)) {
                handler.sendImage(clientName, bytes);
            }
        }
    }

    private void sendImage(String sender, byte[] bytes) throws IOException {
        outputStream.writeUTF("*image*");
        outputStream.writeUTF(sender);
        outputStream.writeInt(bytes.length);
        outputStream.write(bytes);
        outputStream.flush();
    }
}
