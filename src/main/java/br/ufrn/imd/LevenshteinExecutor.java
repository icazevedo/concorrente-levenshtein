package br.ufrn.imd;

public class LevenshteinExecutor {
    private final char[] word1;
    private final char[] word2;

    public LevenshteinExecutor(String word1, String word2) {
        //Transforma as strings em array de chars para facilitar as operações do algorítmo
        this.word1 = word1.toCharArray();
        this.word2 = word2.toCharArray();
    }

    //Implementação iterativa do algoritmo de levenshtein
    public int run() {
        int[][] distance = new int[word1.length+1][word2.length+1];

        for (int x = 1; x <= word1.length; x++) {
            distance[x][0] = x;
        }

        for (int y = 1; y <= word2.length; y++) {
            distance[0][y] = y;
        }

        for (int x = 1; x <= word1.length; ++x) {
            for (int y = 1; y <= word2.length; ++y) {
                int substitutionCost = 0;

                if (word1[x-1] != word2[y-1]) {
                    substitutionCost = 1;
                }

                distance[x][y] = Math.min(
                        distance[x - 1][y] + 1,
                        Math.min(
                                distance[x][y - 1] + 1,
                                distance[x - 1][y - 1] + substitutionCost
                        )
                );
            }
        }

        return distance[word1.length][word2.length];
    }
}
