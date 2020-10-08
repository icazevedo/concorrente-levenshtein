package br.ufrn.imd.concorrente;

public class WordRunner implements Runnable {
    private final String word;

    public WordRunner(String word) {
        this.word = word;
    }

    @Override
    public void run() {
        LevenshteinExecutor levenshteinExecutor = new LevenshteinExecutor(Main.chosenWord, word);
        int distance = levenshteinExecutor.run();

        int smallerDistanceRead = Main.smallerDistance.get();
        if (distance < smallerDistanceRead) {
            Main.smallerDistance.compareAndSet(smallerDistanceRead, distance);
            Main.smallerDistanceWord = word;
            System.out.println("Found new better word: " + word + " with distance " + distance);
        }
    }
}
