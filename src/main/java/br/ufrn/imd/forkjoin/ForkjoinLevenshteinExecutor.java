package br.ufrn.imd.forkjoin;

import java.util.concurrent.RecursiveTask;

public class ForkjoinLevenshteinExecutor extends RecursiveTask<Integer> {
    private final int i;
    private final int j;
    private final char[] word1;
    private final char[] word2;

    public ForkjoinLevenshteinExecutor(int i, int j, String word1, String word2) {
        this.i = i;
        this.j = j;

        //Transforma as strings em array de chars para facilitar as operações do algorítmo
        this.word1 = word1.toCharArray();
        this.word2 = word2.toCharArray();
    }

    public ForkjoinLevenshteinExecutor(int i, int j, char[] word1, char[] word2) {
        this.i = i;
        this.j = j;
        this.word1 = word1;
        this.word2 = word2;
    }

    @Override
    public Integer compute() {
        if (Math.min(i, j) == 0) {
            return Math.max(i, j);
        }

        ForkjoinLevenshteinExecutor executorCond1 = new ForkjoinLevenshteinExecutor(i - 1, j, word1, word2);
        ForkjoinLevenshteinExecutor executorCond2 = new ForkjoinLevenshteinExecutor(i, j - 1, word1, word2);
        ForkjoinLevenshteinExecutor executorCond3 = new ForkjoinLevenshteinExecutor(i - 1, j - 1, word1, word2);

        executorCond1.fork();
        executorCond2.fork();
        int cond3 = executorCond3.compute();

        int cond1 = executorCond1.join();
        int cond2 = executorCond2.join();

        cond1++;
        cond2++;

        if (word1[i - 1] != word2[j - 1]) {
            cond3++;
        }

        return Math.min(cond1, Math.min(cond2, cond3));
    }
}
