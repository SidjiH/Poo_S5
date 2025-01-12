package main;
import clavier.*;
import evaluation.*;
import genetic.*;


public class Main {
    public static void main(String[] args) {
        String corpus = "azertyazertyéù";
        Clavier clavier = new Clavier();
        CorpusProcessor processor = new CorpusProcessor();
        processor.processCorpus(corpus, 1);
        processor.processCorpus(corpus, 2);
        processor.processCorpus(corpus, 3);

        MovementEvaluator evaluator = new MovementEvaluator(clavier);
        evaluator.decompterMouvements(processor);

        //calcul score final pondéré
        double weightedScore = evaluator.calculerScorePondere(processor.getTotalNgrams());

        //Objet qui stocke les rés
        EvaluationResult result = new EvaluationResult(evaluator.obtenirScoresDeMouvement(), weightedScore);


        //afficher le résultat
        System.out.println(result);

        GeneticOptimizer optimizer = new GeneticOptimizer(clavier, evaluator, 10);
        optimizer.optimize(100);
    }
}
