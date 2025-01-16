import java.io.*;

/**
 * Classe Send utilisée pour envoyer des messages à un serveur
 * via un flux de sortie (PrintWriter) dans un thread séparé.
 */
public class Send extends Thread {

    /** Flux de sortie pour envoyer les messages au serveur. */
    private PrintWriter out;

    /** Flux pour lire les entrées utilisateur depuis la console. */
    private BufferedReader userInput;

    /**
     * Constructeur de la classe Send.
     *
     * @param out Flux de sortie pour envoyer les messages au serveur.
     * @param userInput Flux pour lire les entrées utilisateur.
     */
    public Send(PrintWriter out, BufferedReader userInput) {
        this.out = out;
        this.userInput = userInput;
    }

    /**
     * Méthode principale du thread. Lit les commandes de l'utilisateur
     * et les envoie au serveur. Se termine lorsque l'utilisateur entre "quit".
     */
    @Override
    public void run() {
        try {
            String commande;
            while (true) {
                // Lecture de la commande utilisateur
                commande = userInput.readLine();
                
                // Arrêt si l'utilisateur entre "quit"
                if ("quit".equals(commande)) {
                    break;
                }
                
                // Envoi de la commande au serveur
                out.println(commande);
            }
        } catch (IOException e) {
            System.err.println("Erreur lors de l'envoi du message : " + e.getMessage());
        }
    }
}
