package evaluation;
import clavier.*;
import java.util.*;

public class EvaluationResult {

    private final Map<String, Integer> movementScores;
    private final double finalScore;

    public EvaluationResult(Map<String, Integer> movementScores, double finalScore) {
        this.movementScores = movementScores;
        this.finalScore = finalScore;
    }

    public Map<String, Integer> getMovementScores() {
        return movementScores;
    }

    public double getFinalScore() {
        return finalScore;
    }

    @Override
    public String toString() {
        return "{" +
                "score du mouvement =" + movementScores +
                ", score final=" + finalScore +
                '}';
    }
}


