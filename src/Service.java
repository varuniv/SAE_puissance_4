import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class Service implements Runnable {
    private final Socket clientSocket;
    private String nom ;

    public Service(Socket socket) {
        this.clientSocket = socket;
    }

    @Override
    public void run() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true)) {
                while(true){
                    String message = reader.readLine(); 
                    if(message.isBlank() || message.isEmpty()){
                        writer.println("La commande est vide");
                        continue;
                    }
                    try {
                        String[] inputs = message.split(" ");
                        String commande = inputs[0];
                        String arg = inputs[1];
                        switch(commande){
                            case "name": 
                                writer.println(Server.name(clientSocket, arg));
                                break;
                            case "invite": 
                                writer.println(Server.invite(clientSocket, arg)); 
                                break;
                            case "accept": 
                                writer.println(Server.accept(clientSocket, arg)); 
                                break;
                            case "decline": 
                                writer.println(Server.decline(clientSocket, arg)); 
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