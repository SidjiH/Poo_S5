import java.util.Map;

public interface EvaluateurInter {

    double evalclavier(Map<String, Integer> nGramFrequencies, Map<String, Double> movementWeights, int corpusSize);
}