package Clavier;

import java.util.Map;

public class Evaluateur {
    public double evalclavier(Map<String, Integer> frequences, Map<String, Double> movementWeights, int corpusSize) {
        double score = 0.0;

        for (Map.Entry<String, Integer> entry : frequences.entrySet()) {
            String nGram = entry.getKey();
            int frequency = entry.getValue();
            double weight = movementWeights.getOrDefault(nGram, 1.0);

            score += frequency * weight;
        }

        return score / corpusSize;
    }
}