import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
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
    public static Socket getKeyByValue(Map<Socket, String> joueurs2, Object joueur1Soc) {
            for (Entry<Socket, String> entry : joueurs2.entrySet()) {
                if (Objects.equals(joueur1Soc, entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }
    public void run() {

        try {
            while (enCours) {
                // Récupérer la socket du joueur actuel (leader)
                Socket joueurLeaderSocket = getKeyByValue(joueurs, joueurLeader);
    
                if (joueurLeaderSocket == null) {
                    System.out.println("Erreur: Impossible d'identifier le joueur leader.");
                    break;
                }
    
                // Envoyer un message au joueur leader pour jouer
                Server.sendToPlayer(joueurLeaderSocket, "C'est votre tour. Entrez une colonne (0-6) :");
    
                // Lire le choix du joueur leader
                InputStream is = joueurLeaderSocket.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                int colonneChoisie;
                try {
                    colonneChoisie = Integer.parseInt(br.readLine());
                } catch (NumberFormatException | IOException e) {
                    Server.sendToPlayer(joueurLeaderSocket, "Entrée invalide, réessayez.");
                    continue; // Recommencez le tour si l'entrée est invalide
                }
    
                // Valider le coup et jouer
                boolean coupValide = p4.joueCoup(colonneChoisie, joueurLeader.equals(joueurs.values().toArray()[0]) ? Puissance4.BLEU : Puissance4.ROUGE);
                if (!coupValide) {
                    Server.sendToPlayer(joueurLeaderSocket, "Coup invalide, réessayez.");
                    continue; // Recommencez le tour si le coup est invalide
                }
    
                // Vérifier si quelqu'un a gagné
                if (p4.cherche4()) {
                    Server.sendToPlayer(joueurLeaderSocket, "Félicitations, vous avez gagné !");
                    Server.sendToPlayer(getKeyByValue(joueurs, joueurs.values().toArray()[joueurs.size() - 1]), "Vous avez perdu. Le jeu est terminé.");
                    enCours = false;
                    break;
                }
    
                // Vérifier si la grille est pleine
                if (p4.estPlein()) {
                    for (Socket joueur : joueurs.keySet()) {
                        Server.sendToPlayer(joueur, "Match nul, la grille est pleine. Le jeu est terminé.");
                    }
                    enCours = false;
                    break;
                }
    
                // Changer le leader pour le tour suivant
                joueurLeader = joueurs.values().toArray()[0].equals(joueurLeader) 
                               ? (String) joueurs.values().toArray()[1]
                               : (String) joueurs.values().toArray()[0];
            }
        } catch (IOException e) {
            System.err.println("Erreur dans le jeu : " + e.getMessage());
        }
    }
    //     try {
    //         // Flux de communication avec les deux joueurs
    //         ObjectOutputStream outputJoueur1 = new ObjectOutputStream(joueurs.keySet().toArray(new Socket[0])[0].getOutputStream());
    //         ObjectOutputStream outputJoueur2 = new ObjectOutputStream(joueurs.keySet().toArray(new Socket[0])[1].getOutputStream());

    //         // Variables pour la gestion du tour de chaque joueur
    //         boolean tourJoueur1 = true; // Joueur 1 commence
    //         String message;
            
    //         while (enCours) {
    //             // Si c'est le tour du joueur 1
    //             if (tourJoueur1) {
    //                 outputJoueur1.writeObject("C'est votre tour de jouer !");
    //                 outputJoueur2.writeObject("L'autre joueur joue...");

    //                 // Lire le coup du joueur 1
    //                 InputStream inputJoueur1 = joueurs.keySet().toArray(new Socket[0])[0].getInputStream();
    //                 BufferedReader readerJoueur1 = new BufferedReader(new InputStreamReader(inputJoueur1));
    //                 message = readerJoueur1.readLine();

    //                 // Jouer le coup
    //                 int col = Integer.parseInt(message); // Le message contient la colonne à jouer
    //                 if (p4.joueCoup(col, Puissance4.BLEU)) {
    //                     // Vérifier si le joueur a gagné
    //                     if (p4.cherche4()) {
    //                         outputJoueur1.writeObject("Félicitations, vous avez gagné !");
    //                         outputJoueur2.writeObject("Désolé, vous avez perdu.");
    //                         enCours = false; // Fin de la partie
    //                         break;
    //                     }
    //                     tourJoueur1 = false; // Passer au joueur suivant
    //                 } else {
    //                     outputJoueur1.writeObject("Coup invalide, réessayez.");
    //                 }
    //             }
    //             // Si c'est le tour du joueur 2
    //             else {
    //                 outputJoueur2.writeObject("C'est votre tour de jouer !");
    //                 outputJoueur1.writeObject("L'autre joueur joue...");

    //                 // Lire le coup du joueur 2
    //                 InputStream inputJoueur2 = joueurs.keySet().toArray(new Socket[0])[1].getInputStream();
    //                 BufferedReader readerJoueur2 = new BufferedReader(new InputStreamReader(inputJoueur2));
    //                 message = readerJoueur2.readLine();

    //                 // Jouer le coup
    //                 int col = Integer.parseInt(message); // Le message contient la colonne à jouer
    //                 if (p4.joueCoup(col, Puissance4.ROUGE)) {
    //                     // Vérifier si le joueur a gagné
    //                     if (p4.cherche4()) {
    //                         outputJoueur2.writeObject("Félicitations, vous avez gagné !");
    //                         outputJoueur1.writeObject("Désolé, vous avez perdu.");
    //                         enCours = false; // Fin de la partie
    //                         break;
    //                     }
    //                     tourJoueur1 = true; // Passer au joueur suivant
    //                 } else {
    //                     outputJoueur2.writeObject("Coup invalide, réessayez.");
    //                 }
    //             }
    //         }
    //     } catch (IOException e) {
    //         e.printStackTrace();
    //     }
    // }



}     
    


