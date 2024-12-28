import java.util.HashMap;
import java.util.Map;


public class EvaluationFin {
    private final Analyzer corpusAnalyzer;
    private final Clavier keyboardMapper;
    private final Evaluateur keyboardEvaluator;

    public EvaluationFin(Analyzer corpusAnalyzer, Clavier keyboardMapper, Evaluateur keyboardEvaluator) {
        this.corpusAnalyzer = corpusAnalyzer;
        this.keyboardMapper = keyboardMapper;
        this.keyboardEvaluator = keyboardEvaluator;
    }

    public double evaluate(String corpus, int n, Map<String, Double> movementWeights) {
        Map<String, Integer> nGramFrequencies = corpusAnalyzer.analyzeNGrams(corpus, n);
        Map<String, Integer> mappedNGramFrequencies = new HashMap<>();

        for (Map.Entry<String, Integer> entry : nGramFrequencies.entrySet()) {
            String nGram = entry.getKey();
            int frequency = entry.getValue();
            String mappedNGram = keyboardMapper.mapNGramToKeys(nGram); 
            System.out.println("Original NGram: " + nGram + ", Mapped NGram: " + mappedNGram);

            mappedNGramFrequencies.put(mappedNGram, mappedNGramFrequencies.getOrDefault(mappedNGram, 0) + frequency);
        }

        int corpusSize = corpus.length();
        return keyboardEvaluator.evalclavier(nGramFrequencies, movementWeights, corpusSize);
    }
}