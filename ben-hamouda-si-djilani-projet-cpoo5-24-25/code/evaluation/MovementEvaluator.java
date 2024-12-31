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

        // char du ngramme en touche
        for (char c : nGram.toCharArray()) {
            Touche touche = findToucheByLetter(String.valueOf(c));
            if (touche != null) {
                touches.add(touche);
            }
        }
        //score associé 
        if (!touches.isEmpty()) {
            int movementScore = calculateMovementScore(touches);
            movementScores.put(
                nGram, 
                movementScores.getOrDefault(nGram, 0) + movementScore * count // Accumulation
            );
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

    //calcul du score mouvement
    private int calculateMovementScore(List<Touche> touches) {
        int score = 0;

        for (int i = 0; i < touches.size() - 1; i++) {
            Touche current = touches.get(i);
            Touche next = touches.get(i + 1);

            //scores des touches
            score += current.getScore();

            
            if (isAlternatingHands(current, next)) {   //bonus ou une pénalité si avec deux mains
                score += 10; 
            } else if (isSameFinger(current, next)) {
                score -= 10; 
            }
        }

        // Ajouter le score de la dernière touche
        if (!touches.isEmpty()) {
            score += touches.get(touches.size() - 1).getScore();
        }

        return score;
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
}