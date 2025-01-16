import java.io.*;
import java.net.*;

public class Jeu extends Thread {
    private final Socket player1;
    private final Socket player2;
    private final BufferedReader input1;
    private final PrintWriter output1;
    private final BufferedReader input2;
    private final PrintWriter output2;

    public Jeu(Socket player1, Socket player2) throws IOException {
        this.player1 = player1;
        this.player2 = player2;
        this.input1 = new BufferedReader(new InputStreamReader(player1.getInputStream()));
        this.output1 = new PrintWriter(player1.getOutputStream(), true);
        this.input2 = new BufferedReader(new InputStreamReader(player2.getInputStream()));
        this.output2 = new PrintWriter(player2.getOutputStream(), true);
    }

    @Override
    public void run() {
        try {
            Puissance4 game = new Puissance4();
            boolean running = true;
            Socket currentSocket = player1;
            PrintWriter currentOutput = output1;
            BufferedReader currentInput = input1;

            output1.println("Vous êtes le joueur Rouge (R). Attendez votre tour.");
            output2.println("Vous êtes le joueur Jaune (Y). Attendez votre tour.");

            while (running) {
                currentOutput.println("C'est votre tour. Entrez une colonne (0-6):");
                currentOutput.println(game.getBoardString());

                String move = currentInput.readLine();
                try {
                    int col = Integer.parseInt(move);
                    if (!game.makeMove(col)) {
                        currentOutput.println("Mouvement invalide. Essayez à nouveau.");
                        continue;
                    }

                    // Switch players
                    if (currentSocket == player1) {
                        currentSocket = player2;
                        currentOutput = output2;
                        currentInput = input2;
                    } else {
                        currentSocket = player1;
                        currentOutput = output1;
                        currentInput = input1;
                    }

                    // Display the updated board to both players
                    output1.println(game.getBoardString());
                    output2.println(game.getBoardString());

                    // Check if the game is over
                    if (game.isGameOver()) {
                        running = false;
                        output1.println("Le jeu est terminé.");
                        output2.println("Le jeu est terminé.");
                    }
                } catch (NumberFormatException e) {
                    currentOutput.println("Entrée invalide. Entrez un nombre entre 0 et 6.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
