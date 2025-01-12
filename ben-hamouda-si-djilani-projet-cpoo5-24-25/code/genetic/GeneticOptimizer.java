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

    public GeneticOptimizer(Clavier clavier, MovementEvaluator evaluator, int poolSize) {
        this.clavier = clavier;
        this.evaluator = evaluator;
        this.poolSize = poolSize;

        
        for (int i = 0; i < poolSize; i++) {
            pool.add(randomizeDisposition(clavier));       //init le pool avec des dispositions de départ
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
    }

    //disposition finale
    System.out.println("Optimisation terminée. Meilleure disposition finale :");
    System.out.println(pool.get(0).toString());
}


    private Clavier selectRandomParent() {
        return pool.get(random.nextInt(pool.size()));
    }

    private Clavier randomizeDisposition(Clavier clavier) {
        Clavier randomized = new Clavier();
        List<Touche> allTouches = new ArrayList<>();

        
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 10; j++) {
                Touche t = clavier.getTouche(i, j);       //on extrait touches
                if (t != null) {
                    allTouches.add(t);
                }
            }
        }

        
        Collections.shuffle(allTouches);  //on mélange les touches

        //réaffecte les touches aléatoirement
        int index = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 10; j++) {
                if (index < allTouches.size()) {
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

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 10; j++) {
                if (random.nextBoolean()) {
                    child.getTouche(i, j).setLettre(parent1.getTouche(i, j).getLettre());
                } else {
                    child.getTouche(i, j).setLettre(parent2.getTouche(i, j).getLettre());
                }
            }
        }

        return child;
    }

    private double evaluateDisposition(Clavier clavier) {
    evaluator.setClavier(clavier); //assoc le clavier à l'évaluateur
    CorpusProcessor processor = new CorpusProcessor();
    evaluator.decompterMouvements(processor); 
    return evaluator.calculerScorePondere(processor.getTotalNgrams());
}



   
}
