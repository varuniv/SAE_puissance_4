package SAE_puissance_4;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;

class Client {
    public static void main(String[] args) {
        int portEcouteServeur = 10302;
        BufferedReader entreeUtilisateur = null;
        BufferedReader entreeServeur = null;
        PrintStream sortie = null;
        Socket socket = null;

        try {
            // Connexion au serveur local sur le port 10302
            socket = new Socket("127.0.0.1", portEcouteServeur);
            
            // Initialisation des flux d'entrée et de sortie
            entreeUtilisateur = new BufferedReader(new InputStreamReader(System.in));
            entreeServeur = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            sortie = new PrintStream(socket.getOutputStream());

            System.out.println("Connexion établie avec le serveur sur le port " + portEcouteServeur);
            System.out.println("Tapez vos messages (ou 'quit' pour quitter) :");

            String message;
            while (true) {
                // Lecture du message utilisateur
                System.out.print("Vous : ");
                message = entreeUtilisateur.readLine();

                // Envoi du message au serveur
                sortie.println(message);

                // Si l'utilisateur tape 'quit', on quitte la boucle
                if ("quit".equalsIgnoreCase(message)) {
                    System.out.println("Fermeture de la connexion...");
                    break;
                }

                // Lecture de la réponse du serveur
                String reponse = entreeServeur.readLine();
                System.out.println("Serveur : " + reponse);
            }
        } catch (UnknownHostException e) {
            System.out.println("Erreur : Adresse du serveur inconnue.");
        } catch (IOException e) {
            System.out.println("Erreur : Problème d'entrée-sortie.");
        } finally {
            try {
                if (entreeUtilisateur != null) entreeUtilisateur.close();
                if (entreeServeur != null) entreeServeur.close();
                if (sortie != null) sortie.close();
                if (socket != null) socket.close();
            } catch (IOException e) {
                System.out.println("Erreur lors de la fermeture des ressources : " + e.getMessage());
            }
        }
    }
}