import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

class Serveur {
    public static void main(String[] arg) {
		List<Service> ListeJoueurs = new ArrayList<>();
        int portEcoute = 5000;
        ServerSocket standardiste;
        Socket socket;

        try {
            standardiste = new ServerSocket(portEcoute);
            while (true) {
                socket = standardiste.accept();
                new Service(socket);
				ListeJoueurs.add(new Service(socket));
            }
        } catch (IOException exc) {
            System.out.println("probleme de connexion");
            exc.printStackTrace();
        }
    }
}
