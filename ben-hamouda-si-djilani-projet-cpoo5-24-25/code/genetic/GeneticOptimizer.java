package genetic;

import clavier.Clavier;
import clavier.Touche;
import evaluation.*;
import java.util.*;

public class GeneticOptimizer {
    private final Clavier clavier;
    private final MovementEvaluator evaluator;
    private final int poolSize;
    private final Random random = new Random();
    private final List<Clavier> pool = new ArrayList<>();
    private final CorpusProcessor processor; // Ajout du processeur

    public GeneticOptimizer(Clavier clavier, MovementEvaluator evaluator, int poolSize, CorpusProcessor processor) {
    this.clavier = clavier;
    this.evaluator = evaluator;
    this.poolSize = poolSize;
    this.processor = processor; // Ajout du processeur
    
    for (int i = 0; i < poolSize; i++) {
        pool.add(randomizeDisposition(clavier));
    }
}


    public void optimize(int iterations) {
    for (int i = 0; i < iterations; i++) {
        Clavier parent1 = selectRandomParent();
        Clavier parent2 = selectRandomParent();
        Clavier enfant = crossover(parent1, parent2);

        mutation(enfant);
        double childScore = evaluateDisposition(enfant);

        //remplace la pire disposition si l'enfant est mieux
        pool.sort(Comparator.comparingDouble(this::evaluateDisposition));
        if (evaluateDisposition(pool.get(pool.size() - 1)) > childScore) {
            pool.set(pool.size() - 1, enfant);
        }

        //affiche la meilleure disposition 
        System.out.println("Iteration " + (i + 1) + ": Meilleure disposition actuelle avec score " +
            evaluateDisposition(pool.get(0)) + " :");
        System.out.println(pool.get(0).toString());

        System.out.println("Optimisation terminée. Meilleure disposition finale :");
        System.out.println(pool.get(0).toString());
    }

    
    }


    private Clavier selectRandomParent() {
        return pool.get(random.nextInt(pool.size()));
    }

    private Clavier randomizeDisposition(Clavier clavier) {
    Clavier randomized = new Clavier();
    List<Touche> allTouches = new ArrayList<>();

    // Extraire toutes les touches valides
    for (int i = 0; i < clavier.getClavier().length; i++) {
        for (int j = 0; j < clavier.getClavier()[i].length; j++) {
            Touche t = clavier.getTouche(i, j);
            if (t != null) {
                allTouches.add(t);
            }
        }
    }

    // Mélanger les touches
    Collections.shuffle(allTouches);

    // Réaffecter les touches aléatoirement
    int index = 0;
    for (int i = 0; i < clavier.getClavier().length; i++) {
        for (int j = 0; j < clavier.getClavier()[i].length; j++) {
            if (index < allTouches.size() && clavier.getTouche(i, j) != null) {
                randomized.getTouche(i, j).setLettre(allTouches.get(index).getLettre());
                index++;
            }
        }
    }

    return randomized;
}


    private void mutation(Clavier clavier) {
        //echange deux touches au pif
        int x1 = random.nextInt(3);
        int y1 = random.nextInt(10);
        int x2 = random.nextInt(3);
        int y2 = random.nextInt(10);

        Touche t1 = clavier.getTouche(x1, y1);
        Touche t2 = clavier.getTouche(x2, y2);

        if (t1 != null && t2 != null) {
            String temp = t1.getLettre();
            t1.setLettre(t2.getLettre());
            t2.setLettre(temp);
        }
    }

    private Clavier crossover(Clavier parent1, Clavier parent2) {
        Clavier child = new Clavier();

        for (int i = 0; i < parent1.getClavier().length; i++) {
            for (int j = 0; j < parent1.getClavier()[i].length; j++) {
                Touche toucheParent1 = parent1.getTouche(i, j);
                Touche toucheParent2 = parent2.getTouche(i, j);

                if (toucheParent1 != null && toucheParent2 != null) {
                    if (random.nextBoolean()) {
                        child.getTouche(i, j).setLettre(toucheParent1.getLettre());
                    } else {
                        child.getTouche(i, j).setLettre(toucheParent2.getLettre());
                    }
                } else {
                    System.out.println("Position invalide ou touche nulle : (" + i + ", " + j + ")");
                }
            }
            }

            return child;
    }



    private double evaluateDisposition(Clavier clavier) {
    evaluator.setClavier(clavier); // Associer le clavier
    evaluator.decompterMouvements(processor); // Utiliser le processeur existant
    return evaluator.calculerScorePondere(processor.getTotalNgrams());
}




   
}
