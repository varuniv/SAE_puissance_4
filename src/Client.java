import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
    public static void main(String[] args) {
        int portEcouteServeur = 5000;
        BufferedReader entreeUtilisateur = null;
        BufferedReader entreeServeur = null;
        PrintStream sortie = null;
        Socket socket = null;

        try {
            socket = new Socket("127.0.0.1", portEcouteServeur);
            entreeUtilisateur = new BufferedReader(new InputStreamReader(System.in));
            entreeServeur = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            sortie = new PrintStream(socket.getOutputStream());

            System.out.println("Connexion au serveur sur le port " + portEcouteServeur);
            System.out.println("(quit pour quitter)");

            String message;
            while (true) {
                System.out.print("msg : ");
                message = entreeUtilisateur.readLine();
                sortie.println(message);

                if ("quit".equalsIgnoreCase(message)) {
                    System.out.println("Fermeture de la connexion...");
                    break;
                }
            }

            String reponse = entreeServeur.readLine();
            System.out.println("Serveur : " + reponse);

        } catch (UnknownHostException e) {
            System.out.println("Adresse du serveur inconnue.");
        } catch (IOException e) {
            System.out.println("Erreur de lecture");
        } finally {
            try {
                if (entreeUtilisateur != null) entreeUtilisateur.close();
                if (entreeServeur != null) entreeServeur.close();
                if (sortie != null) sortie.close();
                if (socket != null) socket.close();
            } catch (IOException e) {
                System.out.println("Erreur lors de la fermeture des entrees et des sockets");
            }
        }
    }
}