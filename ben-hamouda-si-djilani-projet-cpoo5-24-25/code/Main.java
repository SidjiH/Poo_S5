import clavier.Clavier;
import evaluation.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


public class Main {
    public static void main(String[] args) {
        String corpus = "";

        // Lire le corpus depuis un fichier texte
        try {
            corpus = Files.readString(Paths.get("corpus.txt"));
        } catch (IOException e) {
            System.err.println("Erreur lors de la lecture du fichier corpus.txt : " + e.getMessage());
            return;
        }

        // Supprimer les espaces et lignes vides éventuels
        corpus = corpus.replaceAll("\\s+", "");
        Clavier clavier = new Clavier();
        CorpusProcessor processor = new CorpusProcessor();
        processor.processCorpus(corpus, 1);
        processor.processCorpus(corpus, 2);
        processor.processCorpus(corpus, 3);
        System.out.println("Total N-grams traités : " + processor.getTotalNgrams());

        MovementEvaluator evaluator = new MovementEvaluator(clavier);
       evaluator.decompterMouvements(processor);

        //System.out.println(clavier.toString());

        /*evaluator.obtenirScoresDeMouvement().forEach((mouvement, score) -> {
         System.out.println("mouv : " + mouvement + ", score : " + score);
        });*/

        System.out.println(); 
        //calcul score final pondéré
        double weightedScore = evaluator.calculerScorePondere(processor.getTotalNgrams());
        System.out.println("score final pondere " + weightedScore);

        //Objet qui stocke les rés
        EvaluationResult result = new EvaluationResult(evaluator.obtenirScoresDeMouvement(), weightedScore);


        //afficher le résultat
        System.out.println(result);

        //GeneticOptimizer optimizer = new GeneticOptimizer(clavier, evaluator, 10, processor);
        //optimizer.optimize(10);
       // System.out.println("Total N-grams traités : " + processor.getTotalNgrams());

    }
}

