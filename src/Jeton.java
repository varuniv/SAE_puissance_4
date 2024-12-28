public class Jeton {
    private String couleur;

    public Jeton(String couleur) {
        this.couleur = couleur;
    }

    public boolean is(String couleur) {
        return this.couleur.equals(couleur);
    }

    public String getCouleur(){
        return this.couleur;
    }
}
