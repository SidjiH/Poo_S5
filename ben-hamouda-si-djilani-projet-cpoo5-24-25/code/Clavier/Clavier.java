package Clavier;

import java.util.Map;

public class Clavier{
    private final Map<String, String> charmap;

    public Clavier(Map<String, String> charmap) {
        this.charmap = charmap;
    }

    public Map<String, String> getCharacterToKeyMapping() {
        return charmap;
    }

    public String mapNGramToKeys(String nGram) {
        StringBuilder keySequence = new StringBuilder();
        for (char c : nGram.toCharArray()) {
            keySequence.append(charmap.getOrDefault(String.valueOf(c), ""));
        }
        System.out.println("NGram: " + nGram + " -> Mapped: " + keySequence);
        return keySequence.toString();
    }

}