package br.ufrn.imd.concorrente;

import br.ufrn.imd.LevenshteinExecutor;

import java.util.concurrent.atomic.AtomicInteger;

public class WordRunner implements Runnable {
    private final String word;

    public WordRunner(String word) {
        this.word = word;
    }

    @Override
    public void run() {
        //Executa o algoritmo iterativo de levenshtein nas duas palavras fornecidas
        //LevenshteinExecutor está como runnable apenas para facilitar a leitura
        //e por que prototipei com threads nessa parte do código
        LevenshteinExecutor levenshteinExecutor = new LevenshteinExecutor(Main.chosenWord, word);
        checkAndUpdateDistance(Main.smallerDistance, levenshteinExecutor.run());
    }

    public static void checkAndUpdateDistance(AtomicInteger atomicDistance, int newDistance) {
        //Atualiza valor da menor distancia com o menor dos 2: a nova distância ou o valor atual
        atomicDistance.getAndUpdate(smallerDistance -> Math.min(smallerDistance, newDistance));
    }
}
