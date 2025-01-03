import clavier.*;
import evaluation.*;


public class Main {
    public static void main(String[] args) {
        String corpus = "azertyazerty";
        Clavier clavier = new Clavier();

        //traitement du corpus
        CorpusProcessor processor = new CorpusProcessor();
        processor.processCorpus(corpus, 2); 

        //évaluation des mouvements
        MovementEvaluator evaluator = new MovementEvaluator(clavier);
        processor.getNGramCounts().forEach(evaluator::evaluateNGram);

        //calcul final
        int totalScore = evaluator.getMovementScores().values().stream().mapToInt(Integer::intValue).sum();
        double normalizedScore = (double) totalScore / processor.getTotalNgrams();

        //res
        EvaluationResult result = new EvaluationResult(evaluator.getMovementScores(), normalizedScore);
        System.out.println(result);
    }
}