package Server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {
    ServerSocket serverSocket;
    Socket socket;
    Scanner inputStream;
    PrintWriter out;

    public static void main(String[] args) {
        Server server = new Server();
        try {
            server.connect();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void connect() throws IOException, InterruptedException {

        serverSocket = new ServerSocket(8000);
        System.out.println("Server Listening......");
        while (socket == null) {
            try {
                socket = serverSocket.accept();
                if (socket != null) {
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        inputStream = new Scanner(socket.getInputStream());
        out = new PrintWriter(socket.getOutputStream(), true);
        System.out.println("connection Established");
        InputMonitorThread inputMonitorThread = new InputMonitorThread(socket);
        inputMonitorThread.start();

        while (true) {
           if (inputMonitorThread.getMessage().compareTo("Get") == 0)
                get();

            Thread.sleep(1000);
        }
    }

    public void get() {
        out.println("Data");
    }
}
