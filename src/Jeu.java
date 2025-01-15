import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
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
        while(true){

        }
    }

    public boolean enCours(){
        return this.enCours;
    }

}
