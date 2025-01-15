import java.io.BufferedReader;
import java.io.IOException;

public class Receive extends Thread {
    private BufferedReader in;

    public Receive(BufferedReader in) {
        this.in = in;
    }

    @Override
    public void run() {
        try {
            String message;
            while ((message = in.readLine()) != null) {
                System.out.println("Serveur : " + message);
            }
        } catch (IOException e) {
            System.err.println("Erreur lors de la récéption du message : " + e.getMessage());
        }
    }
}