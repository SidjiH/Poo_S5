import java.util.Map;
import java.util.HashMap;

public interface ClavierInter {
    Map<String, String> getCharacterToKeyMapping();

    String mapNGramToKeys(String nGram);
}