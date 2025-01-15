import java.io.BufferedReader;
import java.io.IOException;

public class Receive extends Thread {
    private BufferedReader in;
    private static final String RED = "\u001B[31m";
    private static final String RESET = "\u001B[0m";

    public Receive(BufferedReader in) {
        this.in = in;
    }

    @Override
    public void run() {
        try {
            String message;
            while ((message = in.readLine()) != null) {
                System.out.println(RED + message + RESET);
            }
        } catch (IOException e) {
            System.err.println("Erreur lors de la récéption du message : " + e.getMessage());
        }
    }
}