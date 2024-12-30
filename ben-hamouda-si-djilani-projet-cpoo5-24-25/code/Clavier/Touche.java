package Clavier;

public class Touche {
    final private Position pos;
    final private int score;
    private String lettre;

    public Touche(Position pos, int score, String lettre){
        this.pos = pos;
        this.score = score;
        this.lettre = lettre;
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
}
