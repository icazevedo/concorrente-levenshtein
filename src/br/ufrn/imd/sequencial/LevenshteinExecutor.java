package br.ufrn.imd.sequencial;

public class LevenshteinExecutor {
    public static int lev(int i, int j, LevenshteinData data) {
        if(Math.min(i, j) == 0) {
            return Math.max(i, j);
        }

        int con1 = lev(i - 1 , j, data) + 1;
        int con2 = lev(i , j - 1, data) + 1;
        int con3 = lev(i - 1 , j - 1, data);

        if (data.getPalavra1()[i-1] != data.getPalavra2()[j-1]) {
           con3++;
        }

        return Math.min(con1, Math.min(con2, con3));
    }
}
