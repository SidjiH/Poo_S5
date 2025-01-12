package evaluation;
import clavier.*;
import java.util.*;

public class MovementEvaluator {

    private  Clavier clavier;
    private final Map<String, Integer> scoresDeMouvement = new HashMap<>();

    public MovementEvaluator(Clavier clavier) {
        this.clavier = clavier;
    }


    public void decompterMouvements(CorpusProcessor processor) {
        System.out.println("Début du décompte des mouvements.");
        processor.getNGramCounts().forEach((nGram, count) -> {
        System.out.println("N-gram : " + nGram + ", Count : " + count);
        // Logique de traitement
        });
        processor.getNGramCounts().forEach((nGram, count) -> {
        List<Touche> touches = convertirNGramEnTouches(nGram);
        if (touches.size() > 3) {
            System.out.println("Séquence ignorée (trop longue) : " + nGram);
            return;
        }

        // Calculer le score et associer au type de mouvement
        int score = calculerScoreDeMouvement(touches);

        String typeMouvement;
        if (touches.size() == 2 && estMemeDoigt(touches.get(0), touches.get(1))) {
            typeMouvement = "SFB";
        } else if (touches.size() == 2 && estExtensionLaterale(touches.get(0), touches.get(1))) {
            typeMouvement = "LSB";
        } else if (touches.size() == 2 && estAlternanceMains(touches.get(0), touches.get(1))) {
            typeMouvement = "Alternance";
        } else if (touches.size() == 3 && estRedirection(touches.get(0), touches.get(1), touches.get(2))) {
            typeMouvement = "Ciseaux";
        } else if (touches.size() == 2 && estRoulementPrefere(touches.get(0), touches.get(1))) {
            typeMouvement = "Roulement";
        } else {
            typeMouvement = "Autre"; // Type par défaut
        }

        scoresDeMouvement.put(typeMouvement, scoresDeMouvement.getOrDefault(typeMouvement, 0) + score * count);
        });
}



    private Touche trouverToucheParLettre(String lettre) {
    for (int i = 0; i < clavier.getClavier().length; i++) {
        for (int j = 0; j < clavier.getClavier()[i].length; j++) {
            Touche t = clavier.getTouche(i, j);
            if (t != null && t.getLettre().equalsIgnoreCase(lettre)) {
                return t;
            }
        }
    }
    System.out.println("Touche non trouvée pour : " + lettre);
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
        System.out.println("Redirection détectée entre " + t1.getLettre() + ", " + t2.getLettre() + " et " + t3.getLettre());
        if (!impliqueDoigtIndex(t1, t2, t3)) {
            score -= 10;
            System.out.println("Redirection sans index détectée entre " + t1.getLettre() + ", " + t2.getLettre() + " et " + t3.getLettre());
        }
    } else if (estSkipgramMemeDoigt(t1, t2, t3)) {
        score -= 15;
        System.out.println("Skipgram détecté entre " + t1.getLettre() + ", " + t2.getLettre() + " et " + t3.getLettre());
    } else {
        System.out.println("Aucun mouvement spécifique détecté pour " + t1.getLettre() + ", " + t2.getLettre() + ", " + t3.getLettre());
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
            String trigramme = touches.get(0).getLettre() + ", " + touches.get(1).getLettre() + ", " + touches.get(2).getLettre();
            score += scoreTroisTouches;
            System.out.println("Mouvement trigramme : [" + trigramme + "], Score: " + scoreTroisTouches);        }

        return score;
    }

    public double calculerScorePondere(int totalNgrams) {
        if (totalNgrams == 0) {
        System.out.println("Erreur : totalNgrams est égal à 0. Impossible de calculer un score pondéré.");
        return 0.0;
    }
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
    return (t1.getPos().x <= 5 && t2.getPos().x > 5) || (t1.getPos().x > 5 && t2.getPos().x <= 5);
}

    private boolean estMemeDoigt(Touche t1, Touche t2) {
        return t1.getPos().x == t2.getPos().x;
    }

    private boolean estRoulementPrefere(Touche t1, Touche t2) {
    if (t1.getPos().x == t2.getPos().x) {
        return t1.getPos().y < t2.getPos().y;  //si la première touche est plus extérieure
    }
    return t1.getPos().x > t2.getPos().x && t1.getPos().x <= 5;
}


    private boolean estRedirection(Touche t1, Touche t2, Touche t3) {
        if (t1 == null || t2 == null || t3 == null) {
        return false; // Pas de redirection possible si une des touches est null
    }
    //touches sur la meme main
    boolean memeMain = (t1.getPos().x <= 5 && t2.getPos().x <= 5 && t3.getPos().x <= 5) ||
                       (t1.getPos().x > 5 && t2.getPos().x > 5 && t3.getPos().x > 5);
    //changement de direction sur colonne
    return memeMain && t1.getPos().x != t2.getPos().x && t2.getPos().x != t3.getPos().x;
}

    private boolean impliqueDoigtIndex(Touche t1, Touche t2, Touche t3) {
        return t1.getPos().x == 5 || t2.getPos().x == 5 || t3.getPos().x == 5;
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
            case 'ë' -> {
                touches.add(trouverToucheParLettre("Shift"));
                touches.add(trouverToucheParLettre("^"));
                touches.add(trouverToucheParLettre("e"));
            }
            case '¨' -> {
                touches.add(trouverToucheParLettre("Shift"));
                touches.add(trouverToucheParLettre("^"));
            }
            case 'œ' -> {
                touches.add(trouverToucheParLettre("o"));
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
            case '@' -> {
                touches.add(trouverToucheParLettre("altgr"));
                touches.add(trouverToucheParLettre("à"));
            }
            case '1' -> {
                touches.add(trouverToucheParLettre("Shift"));
                touches.add(trouverToucheParLettre("&"));
            }
            case '2' -> {
                touches.add(trouverToucheParLettre("Shift"));
                touches.add(trouverToucheParLettre("é"));
            }
            case '3' -> {
                touches.add(trouverToucheParLettre("Shift"));
                touches.add(trouverToucheParLettre(String.valueOf('\"')));
            }
            case '4' -> {
                touches.add(trouverToucheParLettre("Shift"));
                touches.add(trouverToucheParLettre("'"));
            }
            case '5' -> {
                touches.add(trouverToucheParLettre("Shift"));
                touches.add(trouverToucheParLettre("("));
            }
            case '6' -> {
                touches.add(trouverToucheParLettre("Shift"));
                touches.add(trouverToucheParLettre("-"));
            }
            case '7' -> {
                touches.add(trouverToucheParLettre("Shift"));
                touches.add(trouverToucheParLettre("è"));
            }
            case '8' -> {
                touches.add(trouverToucheParLettre("Shift"));
                touches.add(trouverToucheParLettre("_"));
            }
            case '9' -> {
                touches.add(trouverToucheParLettre("Shift"));
                touches.add(trouverToucheParLettre("ç"));
            }
            case '0' -> {
                touches.add(trouverToucheParLettre("Shift"));
                touches.add(trouverToucheParLettre("à"));
            }
            case '°' -> {
                touches.add(trouverToucheParLettre("Shift"));
                touches.add(trouverToucheParLettre("="));
            }

            case '#' -> {
                touches.add(trouverToucheParLettre("altgr"));
                touches.add(trouverToucheParLettre("3"));
            }
            case '~' -> {
                touches.add(trouverToucheParLettre("altgr"));
                touches.add(trouverToucheParLettre("2"));
            }
            case '{' -> {
                touches.add(trouverToucheParLettre("altgr"));
                touches.add(trouverToucheParLettre("'"));
            }
            case '}' -> {
                touches.add(trouverToucheParLettre("altgr"));
                touches.add(trouverToucheParLettre("="));
            }
            case '[' -> {
                touches.add(trouverToucheParLettre("altgr"));
                touches.add(trouverToucheParLettre("("));
            }
            case ']' -> {
                touches.add(trouverToucheParLettre("altgr"));
                touches.add(trouverToucheParLettre(")"));
            }
            case '\\' -> {
                touches.add(trouverToucheParLettre("altgr"));
                touches.add(trouverToucheParLettre("_"));
            }
            case '|' -> {
                touches.add(trouverToucheParLettre("altgr"));
                touches.add(trouverToucheParLettre("-"));
            }
            case '`' -> {
                touches.add(trouverToucheParLettre("altgr"));
                touches.add(trouverToucheParLettre("è"));
            }
            case '\n' -> {
                touches.add(trouverToucheParLettre("entrée"));
            }
            case ' ' -> {
                touches.add(trouverToucheParLettre("espace"));
            }
            case '£' -> {
                touches.add(trouverToucheParLettre("shift"));
                touches.add(trouverToucheParLettre("$"));
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

    public void setClavier(Clavier clavier) {
        this.clavier = clavier;
    }


}
