package evaluation;
import clavier.*;
import java.util.*;

public class MovementEvaluator {

    private final Clavier clavier;
    private final Map<String, Integer> scoresDeMouvement = new HashMap<>();

    public MovementEvaluator(Clavier clavier) {
        this.clavier = clavier;
    }

    public void decompterMouvements(CorpusProcessor processor) {
    processor.getNGramCounts().forEach((nGram, count) -> {
        List<Touche> touches = convertirNGramEnTouches(nGram); //convertir en touches
        if (touches.size() > 3) {
            System.out.println("Séquence ignorée (trop longue) : " + nGram);
            return;
        }

        //calculer le score pour les séquences valides
        int score = calculerScoreDeMouvement(touches);
        scoresDeMouvement.put(nGram, scoresDeMouvement.getOrDefault(nGram, 0) + score * count);
    });
}


    private Touche trouverToucheParLettre(String lettre) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 10; j++) {
                Touche t = clavier.getTouche(i, j);
                if (t.getLettre().equalsIgnoreCase(lettre)) {
                    return t;
                }
            }
        }
        return null;
    }

    private int calculerScoreMouvementDeuxTouches(Touche t1, Touche t2) {
        int score = 0;

        if (estMemeDoigt(t1, t2)) {
            score -= 10;
            System.out.println("SFB détecté entre " + t1.getLettre() + " et " + t2.getLettre());
        } else if (estMouvementCiseaux(t1, t2)) {
            score -= 15;
            System.out.println("Ciseaux détecté entre " + t1.getLettre() + " et " + t2.getLettre());
        } else if (estExtensionLaterale(t1, t2)) {
            score -= 20;
            System.out.println("LSB détecté entre " + t1.getLettre() + " et " + t2.getLettre());
        } else if (estAlternanceMains(t1, t2)) {
            score += 10;
            System.out.println("Alternance de mains détectée entre " + t1.getLettre() + " et " + t2.getLettre());
        } else if (estRoulementPrefere(t1, t2)) {
            score += 15;
            System.out.println("Roulement préféré détecté entre " + t1.getLettre() + " et " + t2.getLettre());
        }

        return score;
    }

    private int calculerScoreMouvementTroisTouches(List<Touche> touches) {
        int score = 0;
        Touche t1 = touches.get(0);
        Touche t2 = touches.get(1);
        Touche t3 = touches.get(2);

        if (estRedirection(t1, t2, t3)) {
            score -= 20;
            if (!impliqueDoigtIndex(t1, t2, t3)) {
                score -= 10;
            }
        } else if (estSkipgramMemeDoigt(t1, t2, t3)) {
            score -= 15;
        }

        return score;
    }

    private int calculerScoreDeMouvement(List<Touche> touches) {
        int score = 0;

        if (touches.size() == 1) {
            score += touches.get(0).getScore();
            System.out.println("Mouvement simple : " + touches.get(0).getLettre() + ", Score: " + score);
        } else if (touches.size() == 2) {
            int scoreDeuxTouches = calculerScoreMouvementDeuxTouches(touches.get(0), touches.get(1));
            score += scoreDeuxTouches;
            System.out.println("Mouvement bigramme : " + touches.get(0).getLettre() + touches.get(1).getLettre() + ", Score: " + scoreDeuxTouches);
        } else if (touches.size() == 3) {
            int scoreTroisTouches = calculerScoreMouvementTroisTouches(touches);
            score += scoreTroisTouches;
            System.out.println("Mouvement trigramme : " + touches + ", Score: " + scoreTroisTouches);
        }

        return score;
    }

    public double calculerScorePondere(int totalNgrams) {
    Map<String, Integer> poidsMouvements = Map.of(
        "SFB", -10,
        "LSB", -20,
        "Ciseaux", -15,
        "Alternance", 10,
        "Roulement", 15
    );

    int scorePondere = scoresDeMouvement.entrySet().stream()
        .mapToInt(entry -> {
            String typeMouvement = entry.getKey();
            int score = entry.getValue();
            return score * poidsMouvements.getOrDefault(typeMouvement, 0);
        }).sum();

    return (double) scorePondere / totalNgrams;
}



    private boolean estMouvementCiseaux(Touche t1, Touche t2) {
    //changeemnt rangee avec meme main
    return t1.getPos().x == t2.getPos().x && Math.abs(t1.getPos().y - t2.getPos().y) == 2;
}

    private boolean estExtensionLaterale(Touche t1, Touche t2) {
        return Math.abs(t1.getPos().x - t2.getPos().x) > 1;
    }

    private boolean estAlternanceMains(Touche t1, Touche t2) {
    // mains differentes
    return (t1.getPos().x <= 4 && t2.getPos().x > 4) || (t1.getPos().x > 4 && t2.getPos().x <= 4);
}

    private boolean estMemeDoigt(Touche t1, Touche t2) {
        return t1.getPos().x == t2.getPos().x;
    }

    private boolean estRoulementPrefere(Touche t1, Touche t2) {
    if (t1.getPos().x == t2.getPos().x) {
        return t1.getPos().y < t2.getPos().y;  //si la première touche est plus extérieure
    }
    return t1.getPos().x > t2.getPos().x && t1.getPos().x <= 4;
}


    private boolean estRedirection(Touche t1, Touche t2, Touche t3) {
    //touches sur la meme main
    boolean memeMain = (t1.getPos().x <= 4 && t2.getPos().x <= 4 && t3.getPos().x <= 4) ||
                       (t1.getPos().x > 4 && t2.getPos().x > 4 && t3.getPos().x > 4);
    //changement de direction sur colonne
    return memeMain && t1.getPos().x != t2.getPos().x && t2.getPos().x != t3.getPos().x;
}

    private boolean impliqueDoigtIndex(Touche t1, Touche t2, Touche t3) {
        return t1.getPos().x == 4 || t2.getPos().x == 4 || t3.getPos().x == 4;
    }

    private boolean estSkipgramMemeDoigt(Touche t1, Touche t2, Touche t3) {
        return estMemeDoigt(t1, t3) && !estMemeDoigt(t1, t2);
    }

    public Map<String, Integer> obtenirScoresDeMouvement() {
        return scoresDeMouvement;
    }


    private List<Touche> convertirNGramEnTouches(String nGram) {
    List<Touche> touches = new ArrayList<>();
    for (char c : nGram.toCharArray()) {
        switch (c) {
            case 'û' -> {
                touches.add(trouverToucheParLettre("^"));
                touches.add(trouverToucheParLettre("u"));
            }
            case 'ù' -> {
                touches.add(trouverToucheParLettre("`"));
                touches.add(trouverToucheParLettre("u"));
            }
            case 'î' -> {
                touches.add(trouverToucheParLettre("^"));
                touches.add(trouverToucheParLettre("i"));
            }
            case 'â' -> {
                touches.add(trouverToucheParLettre("^"));
                touches.add(trouverToucheParLettre("a"));
            }
            case 'ô' -> {
                touches.add(trouverToucheParLettre("^"));
                touches.add(trouverToucheParLettre("o"));
            }
            case 'é' -> {
                touches.add(trouverToucheParLettre("'"));
                touches.add(trouverToucheParLettre("e"));
            }
            case '¨' -> {
                touches.add(trouverToucheParLettre("Shift"));
                touches.add(trouverToucheParLettre("^"));
                touches.add(trouverToucheParLettre("e"));
            }
            case 'œ' -> {
                touches.add(trouverToucheParLettre("o"));
                touches.add(trouverToucheParLettre("e"));
}
            case 'è' -> {
                touches.add(trouverToucheParLettre("`"));
                touches.add(trouverToucheParLettre("e"));
            }
            case 'ê' -> {
                touches.add(trouverToucheParLettre("^"));
                touches.add(trouverToucheParLettre("e"));
            }
            case 'à' -> {
                touches.add(trouverToucheParLettre("`"));
                touches.add(trouverToucheParLettre("a"));
            }
            case 'ç' -> {
                touches.add(trouverToucheParLettre(","));
                touches.add(trouverToucheParLettre("c"));
            }
            case '.' -> {
                touches.add(trouverToucheParLettre("Shift"));
                touches.add(trouverToucheParLettre(";"));
            }
            case '%' -> {
                touches.add(trouverToucheParLettre("Shift"));
                touches.add(trouverToucheParLettre("ù"));
            }
            case 'µ' -> {
                touches.add(trouverToucheParLettre("Shift"));
                touches.add(trouverToucheParLettre("*"));
            }
            case '/' -> {
                touches.add(trouverToucheParLettre("Shift"));
                touches.add(trouverToucheParLettre(":"));
            }
            case '?' -> {
                touches.add(trouverToucheParLettre("Shift"));
                touches.add(trouverToucheParLettre(","));
            }
            
            
            default -> {
                Touche touche = trouverToucheParLettre(String.valueOf(c));
                if (touche != null) {
                    touches.add(touche);
                } else {
                    System.out.println("Caractère non reconnu : " + c);
                }
            }
        }
    }
    return touches;
}


}
