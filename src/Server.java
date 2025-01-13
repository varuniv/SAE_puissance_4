import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class Server {
    private static final int PORT = 8000;  // Port du serveur
    private static final ExecutorService pool = Executors.newFixedThreadPool(2);  // Limiter à 2 clients

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Serveur en attente de connexion...");
            
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connecté : " + clientSocket.getRemoteSocketAddress());
                
                // Gérer la connexion du client dans un thread
                pool.submit(new Service(clientSocket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class Service implements Runnable {
    private final Socket clientSocket;
    private String nom ;

    public Service(Socket socket) {
        this.clientSocket = socket;
    }

    @Override
    public void run() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true)) {

            String message;
            writer.println("Bienvenue sur le serveur !");

            // Lire les messages envoyés par le client
            

            while ((message = reader.readLine()) != null) {

                if (this.nom == null) {
                    this.nom = message;
                    continue;
                }

                System.out.println("Message du client: " + message);

                // Répondre au client
                writer.println("Serveur : " + message);

                if (message.equalsIgnoreCase("fin")) {
                    break;
                }
            }

            System.out.println("Client déconnecté : " + clientSocket.getRemoteSocketAddress());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}