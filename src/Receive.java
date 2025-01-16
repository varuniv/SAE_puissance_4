import java.io.BufferedReader;
import java.io.IOException;

/**
 * Classe Receive utilisée pour recevoir des messages depuis un serveur
 * via un flux d'entrée (BufferedReader) dans un thread séparé.
 */
public class Receive extends Thread {

    /** Flux d'entrée pour lire les messages reçus du serveur. */
    private BufferedReader in;

    /** Code couleur pour afficher les messages reçus en rouge dans la console. */
    private static final String RED = "\u001B[31m";

    /** Code pour réinitialiser la couleur dans la console. */
    private static final String RESET = "\u001B[0m";

    /**
     * Constructeur de la classe Receive.
     *
     * @param in Flux d'entrée utilisé pour recevoir les messages.
     */
    public Receive(BufferedReader in) {
        this.in = in;
    }

    /**
     * Méthode principale du thread. Lit et affiche les messages reçus
     * du serveur en utilisant le flux d'entrée.
     */
    @Override
    public void run() {
        try {
            String message;
            // Lecture continue des messages envoyés par le serveur
            while ((message = in.readLine()) != null) {
                // Affichage des messages reçus en rouge
                System.out.println(RED + message + RESET);
            }
        } catch (IOException e) {
            System.err.println("Erreur lors de la réception du message : " + e.getMessage());
        }
    }
}
