import java.io.*;
import java.net.*;

public class Client {
    private static final String SERVER_ADDRESS = "localhost";  // Adresse du serveur
    private static final int SERVER_PORT = 8000;  // Port du serveur

    public static void main(String[] args) {
        
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
             BufferedReader serverInput = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter serverOutput = new PrintWriter(socket.getOutputStream(), true)) {

            System.out.println("Connecté au serveur à " + SERVER_ADDRESS + ":" + SERVER_PORT);

            //Vérification de la connexion
            System.out.println("Serveur: " + serverInput.readLine());

            System.out.println("Entrez votre nom: ");
            String Nom = userInput.readLine();
            serverOutput.println(Nom);

            String message;
            while (true) {
                // Demander à l'utilisateur d'entrer un message
                System.out.print("Vous: ");
                message = userInput.readLine();

                // Envoyer le message au serveur
                serverOutput.println(message);

                // Lire la réponse du serveur
                String response = serverInput.readLine();
                System.out.println("Serveur: " + response);

                if (message.equalsIgnoreCase("bye")) {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}