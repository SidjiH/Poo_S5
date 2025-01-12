# Ben hamouda-Si djilani-projet-cpoo5-24-25

Ce projet implémente un système pour analyser un corpus de texte et évaluer les mouvements sur un clavier selon des critères spécifiques.

##Structure du projet

##Fichiers principaux

Main.java : Le point d'entrée du programme.

Clavier.java : Définit le clavier utilisé pour l'analyse des mouvements.

CorpusProcessor.java : Traite le corpus en générant des N-grams.

MovementEvaluator.java : Évalue les mouvements sur le clavier.

GeneticOptimizer.java : (pas fini) Optimise la disposition des touches sur le clavier.

EvaluationResult.java : Stocke et affiche les résultats de l'évaluation.

Touche.java, Position.java : Classes utilitaires pour modéliser les touches et leurs positions.

Fichier de corpus

corpus.txt : Contient le texte à analyser. Chaque ligne ou paragraphe est traité comme un corpus continu sans espaces superflus.


##Instructions pour exécuter le programme


###Compiler le projet :

javac Main.java

###Exécuter le programme :

java Main

###Résultats :

Le programme lira le fichier corpus.txt, traitera le texte pour générer des N-grams, et affichera les scores des mouvements.

Les résultats finaux incluent un score pondéré basé sur les mouvements analysés.
