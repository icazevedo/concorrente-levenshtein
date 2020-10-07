package br.ufrn.imd.concorrente;

public class LevenshteinExecutor implements Runnable {
    private final Integer i;
    private final Integer j;
    private final LevenshteinData data;

    public int value;

    public LevenshteinExecutor(Integer i, Integer j, LevenshteinData data) {
        this.i = i;
        this.j = j;
        this.data = data;
    }

    @Override
    public void run() {
        if (Math.min(i, j) == 0) {
            value = Math.max(i, j);
            return;
        }

        LevenshteinExecutor executorCond1 = new LevenshteinExecutor(i - 1, j, data);
        LevenshteinExecutor executorCond2 = new LevenshteinExecutor(i, j - 1, data);
        LevenshteinExecutor executorCond3 = new LevenshteinExecutor(i - 1, j - 1, data);

        Thread threadCond1 = new Thread(executorCond1);
        Thread threadCond2 = new Thread(executorCond2);
        Thread threadCond3 = new Thread(executorCond3);


        try {
        threadCond1.start();
        threadCond2.start();
            threadCond1.join();
            threadCond2.join();
        threadCond3.start();
            threadCond3.join();
        } catch (InterruptedException e) {
            value = -1;
            return;
        }

        int cond1 = executorCond1.value;
        int cond2 = executorCond2.value;
        int cond3 = executorCond3.value;

        if (cond1 == -1 || cond2 == -1 || cond3 == -1) {
           value = -1;
           return;
        }

        cond1++;
        cond2++;

        if (data.getPalavra1()[i - 1] != data.getPalavra2()[j - 1]) {
            cond3++;
        }

        value = Math.min(cond1, Math.min(cond2, cond3));
    }
}
