import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;

public class Controleur {
    private String joueurAttente ;
    private String joueurLeader;
    public Controleur(){
        
    }

    public void receiveInfoFromServer() {
        try (ServerSocket serverSocket = new ServerSocket(12345)) { // Assurez-vous que le port correspond à celui utilisé par Server.java
            while (true) {
                Socket socket = serverSocket.accept();
                InputStream inputStream = socket.getInputStream();
                ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
                String nom = (String) objectInputStream.readObject();
                String message = (String) objectInputStream.readObject();
                String type = (String) objectInputStream.readObject();
                objectInputStream.close();
                socket.close();
                handleMessage(nom, message, type);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void sendInfoToController(String nom, String message){
        try {
            Socket socket = new Socket("localhost", 12345); // Assurez-vous que le port correspond à celui utilisé par Server.java
            OutputStream outputStream = socket.getOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(nom);
            objectOutputStream.writeObject(message);
            objectOutputStream.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleMessage(String nom, String message, String type) {
        switch (type) {
            case "commande":
                handleCommand(nom, message);
                break;
            case "nom":
                handleNom(nom);
                break;
            default:
                System.out.println("Type de message inconnu: " + type);
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
            } else {
                sendInfoToController(nom, "En attente d'un autre joueur");
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
