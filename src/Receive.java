import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class Receive extends Thread {
    private BufferedReader in;
    private PrintWriter out;
    private BufferedReader userInput;

    public Receive(BufferedReader in, PrintWriter out, BufferedReader userInput) {
        this.in = in;
        this.out = out;
        this.userInput = userInput;
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