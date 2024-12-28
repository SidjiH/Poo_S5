import java.util.Map;

public interface AnalyzerInter {
    
    Map<String, Integer> analyzeNGrams(String text, int n);
}
