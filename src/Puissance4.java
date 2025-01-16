/**
 * Classe Puissance4 représentant une grille et la logique du jeu Puissance 4.
 * Cette classe permet de gérer les mouvements des joueurs, vérifier les conditions de victoire
 * ou d'égalité, et afficher l'état de la grille.
 */
public class Puissance4 {
    /** Grille du jeu représentée par un tableau à deux dimensions. */
    private final char[][] board;

    /** Nombre de lignes dans la grille. */
    private final int rows = 6;

    /** Nombre de colonnes dans la grille. */
    private final int cols = 7;

    /** Joueur actuel ('R' pour Rouge, 'Y' pour Jaune). */
    private char currentPlayer;

    /**
     * Constructeur de la classe Puissance4.
     * Initialise la grille vide et définit le joueur Rouge comme joueur initial.
     */
    public Puissance4() {
        board = new char[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                board[i][j] = '.'; // Case vide
            }
        }
        currentPlayer = 'R'; // Le joueur rouge commence
    }

    /**
     * Permet à un joueur de jouer un coup dans une colonne donnée.
     *
     * @param col Numéro de la colonne (0-indexée) où le joueur souhaite jouer.
     * @return true si le coup est valide et joué avec succès, false sinon.
     */
    public boolean makeMove(int col) {
        if (col < 0 || col >= cols || board[0][col] != '.') {
            return false; // Mouvement invalide
        }

        for (int i = rows - 1; i >= 0; i--) {
            if (board[i][col] == '.') {
                board[i][col] = currentPlayer;
                currentPlayer = (currentPlayer == 'R') ? 'Y' : 'R'; // Changement de joueur
                return true;
            }
        }
        return false;
    }

    /**
     * Vérifie si un joueur a gagné la partie.
     *
     * @return true si un joueur a aligné 4 jetons, false sinon.
     */
    public boolean checkWin() {
        // Vérification des lignes, colonnes et diagonales
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (board[i][j] != '.' &&
                    (checkDirection(i, j, 1, 0) || // Horizontale
                     checkDirection(i, j, 0, 1) || // Verticale
                     checkDirection(i, j, 1, 1) || // Diagonale droite
                     checkDirection(i, j, 1, -1))) { // Diagonale gauche
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Vérifie si 4 jetons sont alignés dans une direction donnée.
     *
     * @param row Ligne de départ.
     * @param col Colonne de départ.
     * @param deltaRow Incrément pour la ligne.
     * @param deltaCol Incrément pour la colonne.
     * @return true si 4 jetons sont alignés, false sinon.
     */
    private boolean checkDirection(int row, int col, int deltaRow, int deltaCol) {
        char start = board[row][col];
        int count = 0;

        for (int i = 0; i < 4; i++) {
            int r = row + i * deltaRow;
            int c = col + i * deltaCol;

            if (r < 0 || r >= rows || c < 0 || c >= cols || board[r][c] != start) {
                return false;
            }
            count++;
        }
        return count == 4;
    }

    /**
     * Vérifie si la grille est pleine (aucune case vide).
     *
     * @return true si la grille est pleine, false sinon.
     */
    public boolean isFull() {
        for (int j = 0; j < cols; j++) {
            if (board[0][j] == '.') {
                return false;
            }
        }
        return true;
    }

    /**
     * Affiche la grille actuelle dans la console.
     */
    public void printBoard() {
        for (char[] row : board) {
            for (char cell : row) {
                System.out.print(cell + " ");
            }
            System.out.println();
        }
    }

    /**
     * Retourne le joueur actuel.
     *
     * @return Le caractère représentant le joueur actuel ('R' ou 'Y').
     */
    public char getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Retourne une représentation sous forme de chaîne de caractères de la grille.
     *
     * @return Représentation textuelle de la grille.
     */
    public String getBoardString() {
        StringBuilder sb = new StringBuilder();
        for (char[] row : board) {
            for (char cell : row) {
                sb.append(cell).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    /**
     * Vérifie si la partie est terminée (victoire ou égalité).
     *
     * @return true si la partie est terminée, false sinon.
     */
    public boolean isGameOver() {
        return checkWin() || isFull();
    }
}
