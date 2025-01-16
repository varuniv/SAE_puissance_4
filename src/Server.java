import java.io.*;
import java.net.*;
import java.util.Map.Entry;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.*;

/**
 * Classe Server représentant un serveur multijoueur pour la gestion
 * de connexions, d'invitations et de parties en réseau.
 */
public class Server {

    /** Port sur lequel le serveur écoute les connexions. */
    private static final int PORT = 7000;

    /** Pool de threads pour gérer les clients connectés. */
    private static final ExecutorService pool = Executors.newCachedThreadPool();

    /** Liste des joueurs connectés avec leur socket. */
    private static final Map<Socket, String> joueurs = new HashMap<>();

    /** Liste des invitations en cours entre les joueurs. */
    private static final Map<String, String> invites = new HashMap<>();

    /** Liste des parties en cours entre les joueurs. */
    private static final Map<String, String> parties = new HashMap<>();

    /**
     * Point d'entrée principal du serveur.
     * Attend et accepte les connexions des clients, créant un thread pour chaque client.
     */
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

    /**
     * Associe un nom d'utilisateur à un socket client.
     *
     * @param socket Le socket du client.
     * @param nom Le nom du joueur.
     * @return Un message indiquant le succès ou une erreur.
     * @throws IOException Si une erreur d'entrée/sortie survient.
     */
    public static String name(Socket socket, String nom) throws IOException {
        if (joueurs.containsKey(socket)) {
            return "ERR Vous êtes déjà identifié";
        }
        if (joueurs.containsValue(nom) || nom.isBlank()) {
            return "ERR Nom de joueur invalide ou déjà pris";
        }
        joueurs.put(socket, nom);
        return "Joueur ajouté";
    }

    /**
     * Envoie une invitation d'un joueur à un autre.
     *
     * @param joueur1Soc Le socket du joueur qui invite.
     * @param joueur2Name Le nom du joueur invité.
     * @return Un message indiquant le succès ou une erreur.
     * @throws IOException Si une erreur d'entrée/sortie survient.
     */
    public static String invite(Socket joueur1Soc, String joueur2Name) throws IOException {
        if (!joueurs.containsValue(joueur2Name) || joueur2Name.isBlank()) {
            return "ERR Aucun joueur ne porte ce nom";
        }
        Socket joueur2Soc = getKeyByValue(joueurs, joueur2Name);
        String joueur1Name = joueurs.get(joueur1Soc);
        sendToPlayer(joueur2Soc, "Vous avez reçu une invitation de " + joueur1Name);
        invites.put(joueur1Name, joueur2Name);
        return "Demande envoyée";
    }

    /**
     * Permet à un joueur de refuser une invitation.
     *
     * @param joueur1Soc Le socket du joueur qui refuse.
     * @param joueur2Name Le nom du joueur invitant.
     * @return Un message indiquant le succès ou une erreur.
     * @throws IOException Si une erreur d'entrée/sortie survient.
     */
    public static String decline(Socket joueur1Soc, String joueur2Name) throws IOException {
        if (!joueurs.containsValue(joueur2Name) || joueur2Name.isBlank()) {
            return "ERR Aucun joueur ne porte ce nom";
        }
        Socket joueur2Soc = getKeyByValue(joueurs, joueur2Name);
        String joueur1Name = joueurs.get(joueur1Soc);
        sendToPlayer(joueur2Soc, "Votre invitation à " + joueur1Name + " a été refusée");
        invites.remove(joueur1Name);
        return "Invitation refusée";
    }

    /**
     * Envoie la liste des joueurs connectés à un client.
     *
     * @param joueurSoc Le socket du client demandant la liste.
     * @return Un message indiquant le succès.
     * @throws IOException Si une erreur d'entrée/sortie survient.
     */
    public static String players(Socket joueurSoc) throws IOException {
        for (String key : joueurs.values()) {
            sendToPlayer(joueurSoc, key);
        }
        return "Liste des joueurs envoyée";
    }

    /**
     * Accepte une invitation pour démarrer une partie.
     *
     * @param joueurAccSoc Le socket du joueur acceptant.
     * @param joueurInvName Le nom du joueur invitant.
     * @return Un message indiquant le succès ou une erreur.
     * @throws IOException Si une erreur d'entrée/sortie survient.
     */
    public static String accept(Socket joueurAccSoc, String joueurInvName) throws IOException {
        String joueurAccName = joueurs.get(joueurAccSoc);
        Socket joueurInvSoc = getKeyByValue(joueurs, joueurInvName);
        if (invites.containsKey(joueurInvName) && invites.get(joueurInvName).equals(joueurAccName)) {
            parties.put(joueurInvName, joueurAccName);
            invites.remove(joueurInvName);
            sendToPlayer(joueurInvSoc, "Votre invitation à " + joueurAccName + " a été acceptée");
            return "Invitation acceptée avec succès";
        }
        return "ERR Vous n'avez aucune invitation de " + joueurInvName;
    }

    /**
     * Démarre une partie entre deux joueurs.
     *
     * @param joueur1Soc Le socket du premier joueur.
     * @return Un message indiquant que la partie est terminée ou une erreur.
     * @throws InterruptedException Si une interruption survient.
     * @throws IOException Si une erreur d'entrée/sortie survient.
     */
    public static String play(Socket joueur1Soc) throws InterruptedException, IOException {
        String joueur1Nom = joueurs.get(joueur1Soc);
        String joueur2Nom = parties.get(joueur1Nom);

        if (joueur2Nom == null) {
            return "ERR Vous n'avez aucune invitation en attente.";
        }

        Socket joueur2Soc = getKeyByValue(joueurs, joueur2Nom);

        if (joueur2Soc == null) {
            return "ERR Le joueur " + joueur2Nom + " n'est pas connecté.";
        }

        Jeu jeu = new Jeu(joueur1Soc, joueur2Soc);
        jeu.start();
        jeu.join();
        parties.remove(joueur1Nom);

        return "La partie est terminée.";
    }

    /**
     * Envoie un message à un joueur via son socket.
     *
     * @param joueurSocket Le socket du joueur.
     * @param message Le message à envoyer.
     * @return Un message indiquant que l'envoi a été effectué.
     * @throws IOException Si une erreur d'entrée/sortie survient.
     */
    public static String sendToPlayer(Socket joueurSocket, String message) throws IOException {
        try {
            OutputStream os = joueurSocket.getOutputStream();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));
            bw.write(message);
            bw.newLine();
            bw.flush();
        } catch (IOException e) {
            System.err.println("Erreur dans l'envoi du message : " + e.getMessage());
            throw e;
        }
        return "Message sent";
    }

    /**
     * Récupère le socket associé à un nom de joueur.
     *
     * @param joueurs2 La map contenant les sockets et noms des joueurs.
     * @param joueur1Soc Le nom du joueur recherché.
     * @return Le socket correspondant, ou null si introuvable.
     */
    public static Socket getKeyByValue(Map<Socket, String> joueurs2, String joueur1Soc) {
        for (Entry<Socket, String> entry : joueurs2.entrySet()) {
            if (Objects.equals(joueur1Soc, entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }
}
