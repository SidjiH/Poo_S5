import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        //corpus
        String corpus = "hello world";

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

        Analyzer corpusAnalyzer = new Analyzer();
        Evaluateur keyboardEvaluator = new Evaluateur();
        EvaluationFin evaluationSystem = new EvaluationFin(corpusAnalyzer, keyboardMapper, keyboardEvaluator);

        double score = evaluationSystem.evaluate(corpus, 2, movementWeights);
        System.out.println("Score de la disposition : " + score);
    }
}
