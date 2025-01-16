import java.util.Random;

public class Puissance4 {

    public final static int VIDE = 0;
	public final static int BLEU = 1;
	public final static int ROUGE = 2;
    private int nbcol = 7;
    private int nbrow = 6;
    private int[][] grille;
    private boolean debut = true;
        
    
    public Puissance4() {
        initJeu();
    }
    private void initJeu() {
        grille = new int[nbcol][nbrow];
    for (int col = 0; col < nbcol ; col++) {
        for (int row = 0; row < nbrow; row++) {
            grille[col][row] = VIDE;
        }
    }
    }
    public boolean joueCoup(int col, int joueur) {
        if ((col < 0) || (col >= nbcol)) {
            return false;
        }

        // Trouve la première place vide dans la colonne
        for (int ligne = 0; ligne < nbrow; ligne++) {
            if (grille[col][ligne] == VIDE) {
                grille[col][ligne] = joueur;
               
                for (int i = grille.length - 1; i >= 0; i--) {
                    int[] is = grille[i];
                    String l = "";
                    for (int j = 0; j < is.length; j++) {
                        String pion = "[" + is[j]+ "]"; 
                        l += pion;
                    }

                    System.out.println(l);
                    
                    
                }
                return true;
            }
        }

        // La colonne est pleine
        return false;
    }

    public boolean cherche4() {
	    // Vérifie les horizontales ( - )
	    for (int ligne = 0; ligne < nbrow; ligne++) {
	    	if (cherche4alignes(0, ligne, 1, 0)) {
	    		return true;
	    	}
	    }

    // Vérifie les verticales ( ¦ )
    for (int col = 0; col < nbcol; col++) {
    	if (cherche4alignes(col, 0, 0, 1)) {
    		return true;
    	}
    }

    // Diagonales (cherche depuis la ligne du bas)
    for (int col = 0; col < nbcol; col++) {
		  // Première diagonale ( / )
		  if (cherche4alignes(col, 0, 1, 1)) {
			  return true;
		  }
		  // Deuxième diagonale ( \ )
		  if (cherche4alignes(col, 0, -1, 1)) {
			  return true;
		  }
    }

    // Diagonales (cherche depuis les colonnes gauches et droites)
    for (int ligne = 0; ligne < nbrow; ligne++) {
	    // Première diagonale ( / )
    	if (cherche4alignes(0, ligne, 1, 1)) {
    		return true;
	    }
	    // Deuxième diagonale ( \ )
	    if (cherche4alignes(nbrow - 1, ligne, -1, 1)) {
	    	return true;
	    }
    }

    // On n'a rien trouvé
    return false;
  }

  /**
   * Cette méthode cherche 4 pions alignés sur une ligne. Cette ligne est définie par
   * le point de départ, ou origine de coordonnées (oCol,oLigne), et par le déplacement
   * delta (dCol,dLigne). En utilisant des valeurs appropriées pour dCol et dLigne
   * on peut vérifier toutes les directions:
   * - horizontale:    dCol = 0, dLigne = 1
   * - vérticale:      dCol = 1, dLigne = 0
   * - 1ère diagonale: dCol = 1, dLigne = 1
   * - 2ème diagonale: dCol = 1, dLigne = -1
   *
   * @param oCol   Colonne d'origine de la recherche
   * @param oLigne Ligne d'origine de la recherche
   * @param dCol   Delta de déplacement sur une colonne
   * @param dLigne Delta de déplacement sur une ligne
   * @return true si on trouve un alignement
   */
	private boolean cherche4alignes(int oCol, int oLigne, int dCol, int dLigne) {
		int couleur = VIDE;
	    int compteur = 0;
	
	    int curCol = oCol;
	    int curRow = oLigne;

	    while ((curCol >= 0) && (curCol < nbcol) && (curRow >= 0) && (curRow < nbrow)) {
	      if (grille[curRow][curCol] != couleur) {
		        // Si la couleur change, on réinitialise le compteur
		        couleur = grille[curRow][curCol];
		        compteur = 1;
	      } else {
		        // Sinon on l'incrémente
		        compteur++;
	      }
	
	      // On sort lorsque le compteur atteint 4
	      if ((couleur != VIDE) && (compteur == 4)) {
	    	  return true;
	      }
	
	      // On passe à l'itération suivante
	      curCol += dCol;
	      curRow += dLigne;
	    }

	    // Aucun alignement n'a été trouvé
	    return false;
	}
	
	
	public int getNearCase(int colTraget, int rowTarget){
		int[] elements = {0, -1, 1};  
		for(int col : elements){
			for (int row : elements) {
				if(colTraget + col >= 0 && colTraget + col < nbcol && rowTarget + row >= 0 && rowTarget + row < nbrow){
		    		if(grille[colTraget + col][rowTarget + row] == VIDE){
		    			return colTraget + col;
		    		}
				}
	    	}
		}
		return -1;
	}
	public boolean estPlein() {
        // On cherche une case vide. S'il n'y en a aucune, le tableau est plein
        for (int col = 0; col < nbcol; col++) {
              for (int ligne = 0; ligne < nbrow; ligne++) {
                    if (grille[col][ligne] == VIDE) {
                        return false;
                    }
              }
        }
        return true;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        for (int row = nbrow - 1; row >= 0; row--) {
            for (int col = 0; col < nbcol; col++) {
                sb.append("[").append(grille[col][row]).append("]");
            }
            sb.append("\n");
        }
        sb.append("\n");
        sb.append(" 0  1  2  3  4  5  6\n");
        
        return sb.toString();
    }

    public int getNombreColonnes() {
        return nbcol;
    }

    public int getNombreLignes() {
        return nbrow;
    }
}


