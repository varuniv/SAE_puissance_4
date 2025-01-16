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
    private String joueurSuivant;
    private Map<Socket, String> joueurs = new HashMap<>();
    private Puissance4 p4;
    private boolean enCours;

    public Jeu (Socket j1s, String j1n, Socket j2s, String j2n, Puissance4 p4){
        this.p4 = p4;
        this.joueurs.put(j1s, j1n);
        this.joueurs.put(j2s, j2n);
        this.enCours = true;
        this.joueurLeader = j1n;
        this.joueurSuivant = j2n;
        }
    
        private String getGrilleRepresentation() {
            return p4.toString(); 
        }

    public void run() {
        try {
            while (enCours) {
                Socket joueurLeaderSocket = getKeyByValue(joueurs, joueurLeader);

                if (joueurLeaderSocket == null) {
                    System.out.println("ERR Impossible d'identifier le joueur leader.");
                    System.out.println("Joueur leader: " + joueurLeader);
                    System.out.println("Joueurs: " + joueurs);
                    break;
                }

                Server.sendToPlayer(joueurLeaderSocket, "C'est votre tour. Entrez une colonne (0-6) :");

                InputStream is = joueurLeaderSocket.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                int colonneChoisie;
                try {
                    colonneChoisie = Integer.parseInt(br.readLine());
                } catch (NumberFormatException | IOException e) {
                    Server.sendToPlayer(joueurLeaderSocket, "ERR Entrée invalide, réessayez.");
                    continue; 
                }

                if (colonneChoisie < 0 || colonneChoisie >= p4.getNombreColonnes()) {
                    Server.sendToPlayer(joueurLeaderSocket, "Colonne invalide, réessayez.");
                    continue; 
                }

                boolean coupValide = p4.joueCoup(colonneChoisie, joueurLeader.equals(joueurs.values().toArray()[0]) ? Puissance4.BLEU : Puissance4.ROUGE);
                if (!coupValide) {
                    Server.sendToPlayer(joueurLeaderSocket, "Coup invalide, réessayez.");
                    continue;
                }

                String grille = getGrilleRepresentation();
                for (Socket joueurSocket : joueurs.keySet()) {
                    Server.sendToPlayer(joueurSocket, "Grille actuelle:\n" + grille);
                }

                String temp = joueurLeader;
                joueurLeader = joueurSuivant;
                joueurSuivant = temp;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Socket getKeyByValue(Map<Socket, String> map, String value) {
        for (Entry<Socket, String> entry : map.entrySet()) {
            if (Objects.equals(value, entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }
}
