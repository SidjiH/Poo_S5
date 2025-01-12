package evaluation;

import java.util.*;


  // comptage des N-grammes dans un corpus.
 
public class CorpusProcessor {

    private final Map<String, Integer> nGramCounts = new HashMap<>();   //le integer c'est la frequence de l'occurence
    private int totalNgrams = 0;


    public void processCorpus(String corpus, int n) {
        System.out.println("Traitement du corpus avec N=" + n);
        for (int i = 0; i <= corpus.length() - n; i++) {
            String nGram = corpus.substring(i, i + n);
            nGramCounts.put(nGram, nGramCounts.getOrDefault(nGram, 0) + 1);
            
            totalNgrams++;
        }
    }

    public Map<String, Integer> getNGramCounts() {
        return nGramCounts;
    }

    public int getTotalNgrams() {
        return totalNgrams;
    }
}
