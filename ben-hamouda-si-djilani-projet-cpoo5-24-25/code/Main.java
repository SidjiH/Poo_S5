import Analyses.*;
import Clavier.*;

import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        //corpus
        String corpus = "HELLO WORLD";

        Map<String, String> charToKeyMapping = new HashMap<>();
        charToKeyMapping.put("h", "H");
        charToKeyMapping.put("e", "E");
        charToKeyMapping.put("l", "L");
        charToKeyMapping.put("o", "O");
        charToKeyMapping.put("w", "W");
        charToKeyMapping.put("r", "R");
        charToKeyMapping.put("d", "D");

        Clavier keyboardMapper = new Clavier(charToKeyMapping);
        Map<String, Double> movementWeights = new HashMap<>();
        movementWeights.put("H", 1.0);
        movementWeights.put("E", 0.8);
        movementWeights.put("L", 0.5);
        movementWeights.put("O", 0.9);
        movementWeights.put("LL", 1.0);
        movementWeights.put("O ", 0.8);
        movementWeights.put("LO", 1.4);
        movementWeights.put("OR", 1.3);
        movementWeights.put("EL", 1.2);
        movementWeights.put(" W", 0.7);
        movementWeights.put("WO", 1.6);
        movementWeights.put("LD", 1.7);
        movementWeights.put("RL", 1.1);
        movementWeights.put("HE", 1.5);

        Analyzer corpusAnalyzer = new Analyzer();
        Evaluateur keyboardEvaluator = new Evaluateur();
        EvaluationFin evaluationSystem = new EvaluationFin(corpusAnalyzer, keyboardMapper, keyboardEvaluator);

        double score = evaluationSystem.evaluate(corpus, 2, movementWeights);
        System.out.println("Score de la disposition : " + score);
    }
}
