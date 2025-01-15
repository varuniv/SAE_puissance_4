import java.io.*;

public class Send extends Thread {
    private PrintWriter out;
    private BufferedReader userInput;

    public Send(PrintWriter out, BufferedReader userInput) {
        this.out = out;
        this.userInput = userInput;
    }

    @Override
    public void run() {
        try {
            String commande;
            while (true) {
                System.out.print("Vous:");
                commande = userInput.readLine();
                out.println(commande);
            }
        } catch (IOException e) {
            System.err.println("Erreur de l'envoi du message : " + e.getMessage());
        }
    }
}