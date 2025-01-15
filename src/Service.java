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
                    String[] inputs = message.split(" ");
                    String commande = inputs[0];
                    String arg = inputs[1];

                    switch(commande){
                        case "name": Server.name(arg, clientSocket);
                        case "invite":
                    }

                }




        } catch (IOException e) {
                    e.printStackTrace();
                }

    } 
}