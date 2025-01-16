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
                game.printBoard();

                String move = currentInput.readLine();
                try {
                    int col = Integer.parseInt(move);
                    if (!game.makeMove(col)) {
                        currentOutput.println("Mouvement invalide. Essayez à nouveau.");
                        continue;
                    }

                    if (game.checkWin()) {
                        game.printBoard();
                        currentOutput.println("Félicitations, vous avez gagné !");
                        (currentSocket == player1 ? output2 : output1).println("Désolé, vous avez perdu.");
                        break;
                    }

                    if (game.isFull()) {
                        game.printBoard();
                        output1.println("Match nul !");
                        output2.println("Match nul !");
                        break;
                    }

                    // Changer de joueur
                    game.printBoard();
                    output1.println(game.getBoardString());
                    output2.println(game.getBoardString());
                    currentSocket = (currentSocket == player1) ? player2 : player1;
                    currentOutput = (currentOutput == output1) ? output2 : output1;
                    currentInput = (currentInput == input1) ? input2 : input1;

                } catch (NumberFormatException e) {
                    currentOutput.println("Entrée invalide. Entrez un numéro de colonne entre 0 et 6.");
                }
            }

            // Fin de la partie
            player1.close();
            player2.close();
        } catch (IOException e) {
            System.err.println("Erreur pendant la session de jeu : " + e.getMessage());
        }
    }
}
