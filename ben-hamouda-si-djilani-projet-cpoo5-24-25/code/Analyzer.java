import java.util.HashMap;
import java.util.Map;

public class Analyzer implements AnalyzerInter {
    @Override
    public Map<String, Integer> analyzeNGrams(String text, int n) {
        Map<String, Integer> frequences = new HashMap<>();

        if (text == null || text.isEmpty() || n <= 0 || n > text.length()) {
            return frequences;
        }

        for (int i = 0; i <= text.length() - n; i++) {
            String nGram = text.substring(i, i + n);
            frequences.put(nGram, frequences.getOrDefault(nGram, 0) + 1);
        }

        return frequences;
    }
}
