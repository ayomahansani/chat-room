package lk.ijse.chatRoom.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server implements Runnable{

    private static Server server;
    private final ServerSocket serverSocket;


    private Server() throws IOException {
        serverSocket = new ServerSocket(1200);
        System.out.println("Server Started");
    }

    public static Server getServerSocket() throws IOException {
        return (server == null) ? server = new Server() : server;
    }

    @Override
    public void run() {
        while (!serverSocket.isClosed()) {

            System.out.println("Listening.......");

            try {
                Socket accepted = serverSocket.accept();

                System.out.println("Accepted.......");

                ClientHandler clientHandler = new ClientHandler(accepted);
                Thread thread = new Thread(clientHandler);
                thread.start();

            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
