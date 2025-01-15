import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

public class Server {
    private static final int PORT = 7000;
    private static final ExecutorService pool = Executors.newCachedThreadPool(); 
    private static final Map<String, Socket> joueurs = new HashMap<>();
    //private static final Map<String, Integer> scores = new HashMap<>();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Serveur en attente de connexion...");
            
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connecté : " + clientSocket.getRemoteSocketAddress());
                pool.submit(new Service(clientSocket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String name(String nom, Socket socket){
        if(joueurs.containsKey(nom) || nom.isBlank()){
            return "ERR Nom de joueur invalide ou déjà pris";
        }
        joueurs.put(nom, socket);
        return "Joueur ajouté";
    }

    public static String invite(String joueur1, String joueur2){
        if(joueurs.containsKey(joueur2) || joueur2.isBlank()){
            return "ERR Aucun adversaire ne porte ce nom";
        }
        Socket joueur2Socket = joueurs.get(joueur2);
        //PrintWriter
        
        return "Demande envoyé";
    }
}

