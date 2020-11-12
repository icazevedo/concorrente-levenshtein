package br.ufrn.imd.parallelstream;

import java.util.concurrent.Callable;

public class WordRunner implements Callable<Integer> {
    private final String word;

    public WordRunner(String word) {
        this.word = word;
    }

    @Override
    public Integer call() {
        //Executa o algoritmo iterativo de levenshtein nas duas palavras fornecidas
        //LevenshteinExecutor está como runnable apenas para facilitar a leitura
        //e por que prototipei com threads nessa parte do código
        ParallelLevenshteinExecutor levenshteinExecutor = new ParallelLevenshteinExecutor(Main.chosenWord, word);
        return levenshteinExecutor.run();
    }

    public static Integer getSmallerDistance(Integer minDistance, Integer newDistance) {
        return newDistance < minDistance ? newDistance : minDistance;
    }
}
