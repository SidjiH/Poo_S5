package clavier;

public class Touche {
    final private Position pos;
    final private int score;
    private String lettre;
    private String ALTGR = " ";
    private String SHIFT = " ";
    private String SHIFT_ALT = " ";

    public Touche(Position pos, int score, String lettre){
        this.pos = pos;
        this.score = score;
        this.lettre = lettre;
    }

    public void setALTGR(String ALT) {
        this.ALTGR = ALTGR;
    }
    public void setSHIFT(String SHIFT) {
        this.SHIFT = SHIFT;
    }
    public void setSHIFT_ALT(String SHIFT_ALT) {
        this.SHIFT_ALT = SHIFT_ALT;
    }

    public Position getPos() {
        return pos;
    }
    public int getScore() {
        return score;
    }
    public String getLettre() {
        return lettre;
    }
    public void setLettre(String lettre) {
        this.lettre = lettre;
    }

    @Override
    public String toString() {
        return lettre;
    }
}
