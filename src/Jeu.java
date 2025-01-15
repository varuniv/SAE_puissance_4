import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;

public class Jeu implements Runnable {
    private String joueurAttente ;
    private String joueurLeader;
    public Jeu (){
        
    }

    public void run() {
        while(true){

        }
    }

    private void handleCommand(String nom, String message) {
        // Traitez la commande
        if(message.equals("fin")){
            //mettre fin à la partie et au serveur

        }else if(message.equals("jouer")){
            if(allReady()){
                //envoyer commande au puissance 4
                //afficher aux joueurs le puissance 4
            } 
        }else if(isJoueurLeader(nom)){
            //envoyer commande au puissance 4
            //afficher aux joueurs le nouveau puissance 4
        }
    }

    private void handleNom(String nom) {
        // Traitez le message
        if (this.joueurLeader == null){
            this.joueurLeader = nom;
        }else if(this.joueurAttente == null){
            this.joueurAttente = nom;
        }
    }

    private void switchJoueurLead(){
        String temp = joueurAttente;
        joueurAttente = joueurLeader;
        joueurLeader = temp;
    }

    private boolean isJoueurLeader(String nom){
        return joueurLeader.equals(nom);
    }

    private boolean allReady(){
        return joueurLeader != null && joueurAttente != null;
    }
}
