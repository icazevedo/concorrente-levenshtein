package br.ufrn.imd.sequencial;

public class LevenshteinData {
    private char[] palavra1;
    private char[] palavra2;

    public LevenshteinData(String palavra1, String palavra2) {
        this.palavra1 = palavra1.toCharArray();
        this.palavra2 = palavra2.toCharArray();
    }

    public char[] getPalavra1() {
        return palavra1;
    }

    public void setPalavra1(char[] palavra1) {
        this.palavra1 = palavra1;
    }

    public char[] getPalavra2() {
        return palavra2;
    }

    public void setPalavra2(char[] palavra2) {
        this.palavra2 = palavra2;
    }
}

