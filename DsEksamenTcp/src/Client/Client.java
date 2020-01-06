package Client;

import Server.InputMonitorThread;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {
    private Socket socket = null;
    private String messageToBeSent;

    public static void main(String[] args) {
        Client client = new Client();
        try {
            client.connect();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public void connect() throws  UnknownHostException{
        String message;
        String nonLocalHostAdress = "erdetfredag.dk";
        InetAddress address = InetAddress.getLocalHost();
        InetAddress addresstest = InetAddress.getByName(nonLocalHostAdress);
        PrintWriter out;
        while (socket == null) {
            try {

                socket = new Socket(address.getHostName(), 8000);
                //Input/output for client
                new Scanner(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                System.out.println("Client Address : " + address);

                out.print("GET / HTTP/1.1\r\n");
                out.print("Host: " + address + "\r\n\r\n");
                out.flush(); // actually send data to server

                //Thread to monitor the input from the server. Thread is needed, since there is both input and output that needs monitoring
                InputMonitorThread st = new InputMonitorThread(socket);
                st.start();
                while (true) {
                    //If message isn't null, print the message and reset it to null.
                    if (messageToBeSent != null) {
                        out.println(messageToBeSent);
                        messageToBeSent = null;
                    }
                }
            } catch (ConnectException e) {
                System.out.println("failed to connect to server retrying...");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.err.print("IO Exception");
            }
        }
    }
}


