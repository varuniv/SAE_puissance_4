import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.StringTokenizer;

class Service extends Thread {
    private Socket socket;
    private BufferedReader entree;
    private PrintStream sortie;

    Service(Socket socket) {
        this.socket = socket;
        try {
            entree = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            sortie = new PrintStream(socket.getOutputStream());
            this.start();
        } catch (IOException exc) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public void run() {
        String texte;

        try {
            while (true) {
                texte = entree.readLine();
                sortie.println("Message reçu : " + texte);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (sortie != null) sortie.close();
                if (entree != null) entree.close();
                if (socket != null) socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}