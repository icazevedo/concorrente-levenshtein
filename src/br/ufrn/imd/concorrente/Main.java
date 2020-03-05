package br.ufrn.imd.concorrente;

public class Main {

    public static void main(String[] args) {
        String gato = "gato";
        String sapato = "sapato";

        System.out.println(LevenshteinExecutor.lev(gato.length(), sapato.length(), new LevenshteinData(gato, sapato)));
    }
}
