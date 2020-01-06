package Server;

import java.io.IOException;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.function.Consumer;

public class InputMonitorThread extends Thread {
    private Socket socket;
    private String message = "";
    private Consumer<String> onSignalRecived;
    Scanner in;

    public InputMonitorThread(Socket socket, Consumer<String> onSignalRecived) {
        this.socket = socket;
        this.onSignalRecived = onSignalRecived;
    }

    public InputMonitorThread(Socket socket) {
        this.socket = socket;
        this.onSignalRecived = null;
    }

    @Override
    public synchronized void start() {
        try {
            in = new Scanner(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.start();
    }

    public void run() {
        while (true) {
            try {
                message = in.nextLine();
                System.out.println(message);
                if (onSignalRecived != null)
                    onSignalRecived.accept(message);

            } catch (NoSuchElementException e) {

                break;
            }
        }
    }

    public String getMessage() {
        return message;

    }
}