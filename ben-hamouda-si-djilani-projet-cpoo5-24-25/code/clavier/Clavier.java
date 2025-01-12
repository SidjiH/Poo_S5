package clavier;

import java.util.ArrayList;

public class Clavier {
    final private int acces_score[][] = {{3,2,2,2,1,0,0,1,1,2,3,3},{3,2,2,2,3,3,2,2,2,3,3,3},{1,1,1,0,3,3,0,1,1,1,1,1,2},{3,3,2,3,4,3,2,3,3,2,2},{3,2,2,2,3}};

    private Touche a = new Touche(new Position(0,0),3,"&");
    private Touche b = new Touche(new Position(0,1),2,"é");
    private Touche c = new Touche(new Position(0,2),2,String.valueOf('\"'));
    private Touche d = new Touche(new Position(0,3),2,"'");
    private Touche e= new Touche(new Position(0,4),1,"(");
    private Touche f= new Touche(new Position(0,5),0,"-");
    private Touche g = new Touche(new Position(0,6),0,"è");
    private Touche h = new Touche(new Position(0,7),1,"_");
    private Touche i = new Touche(new Position(0,8),1,"ç");
    private Touche j = new Touche(new Position(0,9),2,"à");
    private Touche k = new Touche(new Position(0,10),3,")");
    private Touche l = new Touche(new Position(0,11),3,"=");


    private Touche un = new Touche(new Position(1,0),3,"A");
    private Touche deux = new Touche(new Position(1,1),2,"Z");
    private Touche trois = new Touche(new Position(1,2),2,"E");
    private Touche quatre = new Touche(new Position(1,3),2,"R");
    private Touche cinq = new Touche(new Position(1,4),3,"T");
    private Touche six = new Touche(new Position(1,5),3,"Y");
    private Touche sept = new Touche(new Position(1,6),2,"U");
    private Touche huit = new Touche(new Position(1,7),2,"I");
    private Touche neuf = new Touche(new Position(1,8),2,"O");
    private Touche dix = new Touche(new Position(1,10),3,"P");
    private Touche dix1 = new Touche(new Position(1,11),3,"^");
    private Touche dix2 = new Touche(new Position(1,12),3,"$");
    


    private Touche onze = new Touche(new Position(2,0),1,"Q");
    private Touche douze = new Touche(new Position(2,1),1,"S");
    private Touche treize = new Touche(new Position(2,2),1,"D");
    private Touche quatorze = new Touche(new Position(2,3),0,"F");
    private Touche quinze = new Touche(new Position(2,4),3,"G");
    private Touche seize = new Touche(new Position(2,5),3,"H");
    private Touche dixsept = new Touche(new Position(2,6),0,"J");
    private Touche dixhuit = new Touche(new Position(2,7),1,"K");
    private Touche dixneuf = new Touche(new Position(2,8),1,"L");
    private Touche vingt = new Touche(new Position(2,9),1,"M");
    private Touche vingt1 = new Touche(new Position(2,10),1,"ù");
    private Touche vingt2 = new Touche(new Position(2,11),1,"*");
    private Touche vingt3 = new Touche(new Position(2,12),2,"Entrée");

    private Touche vingtun = new Touche(new Position(3,0),3,"W");
    private Touche vingtdeux = new Touche(new Position(3,1),3,"X");
    private Touche vingttrois = new Touche(new Position(3,2),2,"C");
    private Touche vingtquatre = new Touche(new Position(3,3),3,"V");
    private Touche vingtquinze = new Touche(new Position(3,4),4,"B");
    private Touche vingtsix = new Touche(new Position(3,5),3,"N");
    private Touche vingtsept = new Touche(new Position(3,6),2,",");
    private Touche vingthuit = new Touche(new Position(3,7),3,";");
    private Touche vingtneuf = new Touche(new Position(3,8),3,":");
    private Touche trente = new Touche(new Position(3,9),2,"!");
    private Touche trente1 = new Touche(new Position(3,9),2,"Shift");

    private Touche trenteun = new Touche(new Position(4,0),3,"ctrl");
    private Touche trentedeux = new Touche(new Position(4,1),2,"Fn");
    private Touche trentetrois = new Touche(new Position(4,2),2,"alt");
    private Touche trentequatre = new Touche(new Position(4,3),2,"espace");
    private Touche trentecinq = new Touche(new Position(4,4),3,"altgr");
    
   

    private Touche clav[][] = {
            {a,b,c,d,e,f,g,h,i,j,k,l},
            {un, deux, trois, quatre, cinq, six, sept, huit, neuf, dix, dix1, dix2},
            {onze, douze, treize, quatorze, quinze, seize, dixsept, dixhuit, dixneuf, vingt, vingt1, vingt2,vingt3},
            {vingtun, vingtdeux, vingttrois, vingtquatre, vingtquinze, vingtsix, vingtsept, vingthuit, vingtneuf, trente, trente1},
            {trenteun,trentedeux,trentetrois,trentequatre,trentecinq}
    };

    public Clavier(){
    }
    public Touche getTouche(int x, int y) {
    if (x < 0 || x >= clav.length || y < 0 || y >= clav[x].length) {
        System.out.println("Position invalide : (" + x + ", " + y + ")");
        return null;
    }
    return clav[x][y];
}


    public Touche[][] getClavier() {
    return clav;
    }

    public int getScore(int x, int y){
        return acces_score[x][y];
    }
    public ArrayList<Touche> getLigne(int x){
        ArrayList<Touche> ligne = new ArrayList<Touche>();
        for(int i=0; i<14; i++){
            ligne.add(clav[x][i]);
        }
        return ligne;
    }
    public ArrayList<Touche> getColonne(int y){
        ArrayList<Touche> colonne = new ArrayList<Touche>();
        for(int i=0; i<6; i++){
            colonne.add(clav[i][y]);
        }
        return colonne;
    }

    @Override
    
    public String toString() {
        String res = "";
        for (int i = 0; i < clav.length; i++) {
            for (int j = 0; j < clav[i].length; j++) {
                if (clav[i][j] != null) {
                    res += clav[i][j].getLettre() + " ";
                } else {
                    res += "  "; // Ajouter un espace vide pour les cases nulles
                }
            }
            res += "\n";
        }
        return res;
    }

}
