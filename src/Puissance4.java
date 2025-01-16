public class Puissance4 {
    private final char[][] board;
    private final int rows = 6;
    private final int cols = 7;
    private char currentPlayer;

    public Puissance4() {
        board = new char[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                board[i][j] = '.'; // Case vide
            }
        }
        currentPlayer = 'R'; // Le joueur rouge commence
    }

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

    public boolean isFull() {
        for (int j = 0; j < cols; j++) {
            if (board[0][j] == '.') {
                return false;
            }
        }
        return true;
    }

    public void printBoard() {
        for (char[] row : board) {
            for (char cell : row) {
                System.out.print(cell + " ");
            }
            System.out.println();
        }
    }

    public char getCurrentPlayer() {
        return currentPlayer;
    }

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
}
