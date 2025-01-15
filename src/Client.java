import java.io.*;
import java.net.*;

public class Client {
    private static final String SERVER_ADDRESS = "localhost";  // Adresse du serveur
    private static final int SERVER_PORT = 7000;  // Port du serveur

    public static void main(String[] args) {
        
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
             BufferedReader serverInput = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter serverOutput = new PrintWriter(socket.getOutputStream(), true)) {

            System.out.println("Connecté au serveur à " + SERVER_ADDRESS + ":" + SERVER_PORT);

            Send send = new Send(serverOutput, userInput);
            Receive receive = new Receive(serverInput);
            send.start();
            receive.start();
            while(send.isAlive() && receive.isAlive()) {
                // Attendre que les threads se terminent
            }
            System.out.println("Déconnecté du serveur");


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}