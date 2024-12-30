package Clavier;

import java.util.ArrayList;

public class Clavier {
    final private int acces_score[][] = {{3,2,2,2,3,3,2,2,2,3},{1,1,1,0,3,3,0,1,1,1},{3,3,2,3,4,3,2,3,3,2}};
    private Touche un = new Touche(new Position(0,0),3,"A");
    private Touche deux = new Touche(new Position(0,1),2,"Z");
    private Touche trois = new Touche(new Position(0,2),2,"E");
    private Touche quatre = new Touche(new Position(0,3),2,"R");
    private Touche cinq = new Touche(new Position(0,4),3,"T");
    private Touche six = new Touche(new Position(0,5),3,"Y");
    private Touche sept = new Touche(new Position(0,6),2,"U");
    private Touche huit = new Touche(new Position(0,7),2,"I");
    private Touche neuf = new Touche(new Position(0,8),2,"O");
    private Touche dix = new Touche(new Position(0,9),3,"P");

    private Touche onze = new Touche(new Position(1,0),1,"Q");
    private Touche douze = new Touche(new Position(1,1),1,"S");
    private Touche treize = new Touche(new Position(1,2),1,"D");
    private Touche quatorze = new Touche(new Position(1,3),0,"F");
    private Touche quinze = new Touche(new Position(1,4),3,"G");
    private Touche seize = new Touche(new Position(1,5),3,"H");
    private Touche dixsept = new Touche(new Position(1,6),0,"J");
    private Touche dixhuit = new Touche(new Position(1,7),1,"K");
    private Touche dixneuf = new Touche(new Position(1,8),1,"L");
    private Touche vingt = new Touche(new Position(1,9),1,"M");

    private Touche vingtun = new Touche(new Position(2,0),3,"W");
    private Touche vingtdeux = new Touche(new Position(2,1),3,"X");
    private Touche vingttrois = new Touche(new Position(2,2),2,"C");
    private Touche vingtquatre = new Touche(new Position(2,3),3,"V");
    private Touche vingtquinze = new Touche(new Position(2,4),4,"B");
    private Touche vingtsix = new Touche(new Position(2,5),3,"N");
    private Touche vingtsept = new Touche(new Position(2,6),2,",");
    private Touche vingthuit = new Touche(new Position(2,7),3,";");
    private Touche vingtneuf = new Touche(new Position(2,8),3,":");
    private Touche trente = new Touche(new Position(2,9),2,"!");

    private Touche clav[][] = {
            {un, deux, trois, quatre, cinq, six, sept, huit, neuf, dix},
            {onze, douze, treize, quatorze, quinze, seize, dixsept, dixhuit, dixneuf, vingt},
            {vingtun, vingtdeux, vingttrois, vingtquatre, vingtquinze, vingtsix, vingtsept, vingthuit, vingtneuf, trente}
    };

    public Clavier(){
    }
    public Touche getTouche(int x, int y){
        return clav[x][y];
    }
    public int getScore(int x, int y){
        return acces_score[x][y];
    }
    public ArrayList<Touche> getLigne(int x){
        ArrayList<Touche> ligne = new ArrayList<Touche>();
        for(int i=0; i<10; i++){
            ligne.add(clav[x][i]);
        }
        return ligne;
    }
    public ArrayList<Touche> getColonne(int y){
        ArrayList<Touche> colonne = new ArrayList<Touche>();
        for(int i=0; i<3; i++){
            colonne.add(clav[i][y]);
        }
        return colonne;
    }

    @Override
    public String toString() {
        String res = "";
        for(int i=0; i<3; i++){
            for(int j=0; j<10; j++){
                res += clav[i][j].getLettre() + " ";
            }
            res += "\n";
        }
        return res;
    }
}
