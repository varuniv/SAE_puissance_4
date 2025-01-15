import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.io.BufferedReader;
import java.io.IOException;

public class Jeu implements Runnable {
    private String joueurLeader;
    private Map<Socket, String> joueurs = new HashMap<>();
    private Puissance4 p4;
    private boolean enCours;
    public Jeu (Socket j1s, String j1n, Socket j2s, String j2n, Puissance4 p4){
        this.p4 = p4 ;
        this.joueurs.put(j1s, j1n);
        this.joueurs.put(j2s, j2n);
        this.enCours = true ;
        this.joueurLeader = j1n ;
    }

    public void run() {
        try {
            // Flux de communication avec les deux joueurs
            ObjectOutputStream outputJoueur1 = new ObjectOutputStream(joueurs.keySet().toArray(new Socket[0])[0].getOutputStream());
            ObjectOutputStream outputJoueur2 = new ObjectOutputStream(joueurs.keySet().toArray(new Socket[0])[1].getOutputStream());

            // Variables pour la gestion du tour de chaque joueur
            boolean tourJoueur1 = true; // Joueur 1 commence
            String message;
            
            while (enCours) {
                // Si c'est le tour du joueur 1
                if (tourJoueur1) {
                    outputJoueur1.writeObject("C'est votre tour de jouer !");
                    outputJoueur2.writeObject("L'autre joueur joue...");

                    // Lire le coup du joueur 1
                    InputStream inputJoueur1 = joueurs.keySet().toArray(new Socket[0])[0].getInputStream();
                    BufferedReader readerJoueur1 = new BufferedReader(new InputStreamReader(inputJoueur1));
                    message = readerJoueur1.readLine();

                    // Jouer le coup
                    int col = Integer.parseInt(message); // Le message contient la colonne à jouer
                    if (p4.joueCoup(col, Puissance4.BLEU)) {
                        // Vérifier si le joueur a gagné
                        if (p4.cherche4()) {
                            outputJoueur1.writeObject("Félicitations, vous avez gagné !");
                            outputJoueur2.writeObject("Désolé, vous avez perdu.");
                            enCours = false; // Fin de la partie
                            break;
                        }
                        tourJoueur1 = false; // Passer au joueur suivant
                    } else {
                        outputJoueur1.writeObject("Coup invalide, réessayez.");
                    }
                }
                // Si c'est le tour du joueur 2
                else {
                    outputJoueur2.writeObject("C'est votre tour de jouer !");
                    outputJoueur1.writeObject("L'autre joueur joue...");

                    // Lire le coup du joueur 2
                    InputStream inputJoueur2 = joueurs.keySet().toArray(new Socket[0])[1].getInputStream();
                    BufferedReader readerJoueur2 = new BufferedReader(new InputStreamReader(inputJoueur2));
                    message = readerJoueur2.readLine();

                    // Jouer le coup
                    int col = Integer.parseInt(message); // Le message contient la colonne à jouer
                    if (p4.joueCoup(col, Puissance4.ROUGE)) {
                        // Vérifier si le joueur a gagné
                        if (p4.cherche4()) {
                            outputJoueur2.writeObject("Félicitations, vous avez gagné !");
                            outputJoueur1.writeObject("Désolé, vous avez perdu.");
                            enCours = false; // Fin de la partie
                            break;
                        }
                        tourJoueur1 = true; // Passer au joueur suivant
                    } else {
                        outputJoueur2.writeObject("Coup invalide, réessayez.");
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}     
    


