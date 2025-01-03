package evaluation;
import clavier.*;
import java.util.*;

public class MovementEvaluator {

    private final Clavier clavier;
    private final Map<String, Integer> movementScores = new HashMap<>();

    public MovementEvaluator(Clavier clavier) {
        this.clavier = clavier;
    }

   public void evaluateNGram(String nGram, int count) {
    List<Touche> touches = new ArrayList<>();

    for (char c : nGram.toCharArray()) {
        Touche touche = findToucheByLetter(String.valueOf(c));
        if (touche != null) {
            touches.add(touche);
        }
    }

    if (!touches.isEmpty()) {
        int movementScore = calculateMovementScore(touches);
        movementScores.put(
            nGram,
            movementScores.getOrDefault(nGram, 0) + movementScore * count
        );

        //juste du debug
        System.out.println("Bigramme: " + nGram + ", Score: " + movementScore + ", Touches: " + touches);
    }
}


     //lettre -> touche
    private Touche findToucheByLetter(String lettre) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 10; j++) {
                Touche t = clavier.getTouche(i, j);
                if (t.getLettre().equalsIgnoreCase(lettre)) {
                    return t;
                }
            }
        }
        return null;
    }

    private int calculateTwoKeyMovementScore(Touche t1, Touche t2) {
    int score = 0;

    if (isSameFinger(t1, t2)) {
        score -= 10;
        System.out.println("SFB détecté entre " + t1.getLettre() + " et " + t2.getLettre());
    } else if (isScissorMovement(t1, t2)) {
        score -= 15;
        System.out.println("Ciseaux détecté entre " + t1.getLettre() + " et " + t2.getLettre());
    } else if (isLateralStretch(t1, t2)) {
        score -= 20;
        System.out.println("LSB détecté entre " + t1.getLettre() + " et " + t2.getLettre());
    } else if (isAlternatingHands(t1, t2)) {
        score += 10;
        System.out.println("Alternance de mains détectée entre " + t1.getLettre() + " et " + t2.getLettre());
    } else if (isPreferredRoll(t1, t2)) {
        score += 15;
        System.out.println("Roulement préféré détecté entre " + t1.getLettre() + " et " + t2.getLettre());
    }

    return score;
}


    private int calculateThreeKeyMovementScore(List<Touche> touches) {
        int score = 0;
        Touche t1 = touches.get(0);
        Touche t2 = touches.get(1);
        Touche t3 = touches.get(2);

        if (isRedirection(t1, t2, t3)) {
            score -= 20; //pénalité redirections
            if (!involvesIndexFinger(t1, t2, t3)) {
                score -= 10; // pena supp pour mauvaises redirections
            }
        } else if (isSameFingerSkipgram(t1, t2, t3)) {
            score -= 15; //pénalité pour SKS
        }

        return score;
    }

    //calcul du score mouvement
    private int calculateMovementScore(List<Touche> touches) {
    int score = 0;

    if (touches.size() == 1) {
        score += touches.get(0).getScore();
        System.out.println("Mouvement simple : " + touches.get(0).getLettre() + ", Score: " + score);
    } else if (touches.size() == 2) {
        int twoKeyScore = calculateTwoKeyMovementScore(touches.get(0), touches.get(1));
        score += twoKeyScore;
        System.out.println("Mouvement bigramme : " + touches.get(0).getLettre() + touches.get(1).getLettre() + ", Score: " + twoKeyScore);
    } else if (touches.size() == 3) {
        int threeKeyScore = calculateThreeKeyMovementScore(touches);
        score += threeKeyScore;
        System.out.println("Mouvement trigramme : " + touches + ", Score: " + threeKeyScore);
    }

    return score;
}


    private boolean isScissorMovement(Touche t1, Touche t2) {
        return t1.getPos().x != t2.getPos().x && Math.abs(t1.getPos().x - t2.getPos().x) == 2;
    }

    private boolean isLateralStretch(Touche t1, Touche t2) {
        return Math.abs(t1.getPos().y - t2.getPos().y) > 1;
    }

    

     //si alternance entre les mains parce que que c'est pas ouf avec la meme main  
    private boolean isAlternatingHands(Touche t1, Touche t2) {
        return (t1.getPos().y <= 4 && t2.getPos().y > 4) || (t1.getPos().y > 4 && t2.getPos().y <= 4);  //mg:0-4 md:5-9
    }

    
     //si deux touches utilisent le même doigt (pour l'instant je vais laisser sur la meme colonne apres je vais agrandir la marge)
    private boolean isSameFinger(Touche t1, Touche t2) {
        return t1.getPos().y == t2.getPos().y; 
    }

    public Map<String, Integer> getMovementScores() {
        return movementScores;
    }

    private boolean isPreferredRoll(Touche t1, Touche t2) {
    //roulements horizontaux
    if (t1.getPos().x == t2.getPos().x) { // meme rangée
        return t1.getPos().y < t2.getPos().y; //extérieur -> intérieur
    }
    //Roulement vertical déjà géré
    return t1.getPos().y > t2.getPos().y && t1.getPos().y <= 4;
}


    private boolean isRedirection(Touche t1, Touche t2, Touche t3) {
        return t1.getPos().x == t3.getPos().x && t2.getPos().x != t1.getPos().x;
    }

    private boolean involvesIndexFinger(Touche t1, Touche t2, Touche t3) {
        return t1.getPos().y == 4 || t2.getPos().y == 4 || t3.getPos().y == 4;
    }

    private boolean isSameFingerSkipgram(Touche t1, Touche t2, Touche t3) {
        return isSameFinger(t1, t3) && !isSameFinger(t1, t2);
    }
}