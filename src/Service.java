import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Service implements Runnable {
    private final Socket clientSocket;

    public Service(Socket socket) {
        this.clientSocket = socket;
    }

    @Override
    public void run() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true)) {
                writer.println("Entrer votre nom avec la commande 'name' suivi de votre nom \n");
                writer.println("Pour avoir la liste des commandes entrer help \n");
                while(true){
                    String message = reader.readLine(); 
                    if(message.isBlank() || message.isEmpty()){
                        writer.println("La commande est vide");
                        continue;
                    }
                    try {
                        String[] inputs = message.split(" ");
                        String commande = inputs[0];
                        String arg = inputs.length > 1 ? inputs[1] : "";
                        switch(commande){
                            case "name": 
                                if (!arg.isEmpty()) {
                                    writer.println(Server.name(clientSocket, arg));
                                } else {
                                    writer.println("Argument manquant pour la commande 'name'");
                                }
                                break;
                            case "invite": 
                                if (!arg.isEmpty()) {
                                    writer.println(Server.invite(clientSocket, arg)); 
                                } else {
                                    writer.println("Argument manquant pour la commande 'invite'");
                                }
                                break;
                            case "accept": 
                                if (!arg.isEmpty()) {
                                    writer.println(Server.accept(clientSocket, arg)); 
                                } else {
                                    writer.println("Argument manquant pour la commande 'accept'");
                                }
                                break;
                            case "decline": 
                                if (!arg.isEmpty()) {
                                    writer.println(Server.decline(clientSocket, arg)); 
                                } else {
                                    writer.println("Argument manquant pour la commande 'decline'");
                                }
                                break;
                            case "play": 
                                writer.println(Server.play(clientSocket)); 
                                break;
                            case "players":
                                writer.println(Server.players(clientSocket));
                                break;
                            case "help":
                                String lesCommandes = "Voici la liste des commandes :\n";
                                lesCommandes += "-name [votreNom] : vous entregistre en tant que jouer \n";
                                lesCommandes += "-invite [nomJoueur] : inviter un joueur connecté \n";
                                lesCommandes += "-accept [nomInvité] : accepter l'invitation d'un joueur \n";
                                lesCommandes += "-decline [nomInvité] : refuse un invitation de jeu \n";
                                lesCommandes += "-play : lancer la partie \n";
                                lesCommandes += "-player : affiche les joueurs disponible \n";
                                writer.println(lesCommandes);
                                break;
                            default: 
                                writer.println("Commande invalide");
                        }
                    } catch (Exception e) {
                        writer.println("Erreur lors du traitement de la commande: " + e.getMessage());
                    }


                }




        } catch (IOException e) {
                    e.printStackTrace();
                }

    } 
}