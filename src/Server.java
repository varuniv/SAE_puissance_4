import java.io.*;
import java.net.*;
import java.util.Map.Entry;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.*;

public class Server {
    private static final int PORT = 7000;
    private static final ExecutorService pool = Executors.newCachedThreadPool(); 
    private static final Map<Socket, String> joueurs = new HashMap<>(); //Liste des joueurs avec Socket
    private static final Map<String, String> invites = new HashMap<>(); //Liste de toute les invitations en cours
    private static final Map<String, String> parties = new HashMap<>(); //Liste des parties en cours
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

    public static String name(Socket socket, String nom) throws IOException{
        if(joueurs.containsKey(socket)){
            return "ERR Vous êtes déjà identifié";
        }
        if(joueurs.containsValue(nom) || nom.isBlank()){
            return "ERR Nom de joueur invalide ou déjà pris";
        }
        joueurs.put(socket, nom);
        return "Joueur ajouté";
    }

    public static String invite(Socket joueur1Soc, String joueur2Name) throws IOException{
        if(joueurs.containsValue(joueur2Name) || joueur2Name.isBlank()){
            return "ERR Aucun joueur ne porte ce nom";
        }
        Socket joueur2Soc = getKeyByValue(joueurs, joueur2Name);
        String joueur1Name = joueurs.get(joueur1Soc);
        sendToPlayer(joueur2Soc, "Vous avez reçu une invitation de " + joueur1Name);
        invites.put(joueur1Name, joueur2Name);

        
        return "Demande envoyé";
    }

    public static String decline(Socket joueur1Soc, String joueur2Name) throws IOException{
        if(joueurs.containsValue(joueur2Name) || joueur2Name.isBlank()){
            return "ERR Aucun joueur ne porte ce nom";
        }
        Socket joueur2Soc = getKeyByValue(joueurs, joueur2Name);
        String joueur1Name = joueurs.get(joueur1Soc);
        sendToPlayer(joueur2Soc, "Votre invitation à " + joueur1Name + " a été refusée");
        invites.remove(joueur1Name);
        return "Invitation refusée";
    }

    //JoueurAcc = Joueur acceptant
    //JoueurInv = Joueur invitant
    public static String accept(Socket joueurAccSoc, String joueurInvName ) throws IOException{
        String joueurAccName = joueurs.get(joueurAccSoc);
        Socket joueurInvSoc = getKeyByValue(joueurs, joueurInvName);
        if(invites.containsKey(joueurInvName) && invites.get(joueurInvName) == joueurAccName){
            parties.put(joueurInvName, joueurAccName);
            invites.remove(joueurInvName);
            sendToPlayer(joueurInvSoc, "Votre invitation à " +joueurAccName+" a été acceptée");
            return "Invitation accepté avec succès";
        }
        return "ERR Vous n'avez aucune invitation de " + joueurInvName ;
    }

    public static String sendToPlayer(Socket joueurSocket, String message) throws IOException{
        OutputStream os = joueurSocket.getOutputStream();
        OutputStreamWriter osw = new OutputStreamWriter(os);
        BufferedWriter bw = new BufferedWriter(osw);
        bw.write(message);
        bw.flush();
        return "Message envoyé au client";
    }

    public static Socket getKeyByValue(Map<Socket, String> joueurs2, String joueur1Soc) {
        for (Entry<Socket, String> entry : joueurs2.entrySet()) {
            if (Objects.equals(joueur1Soc, entry.getValue())) {
            return entry.getKey();
        }
    }
    return null;
    }
}


