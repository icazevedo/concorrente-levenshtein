package br.ufrn.imd.sequencial;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import br.ufrn.imd.concorrente.LevenshteinExecutor;

public class Main {
    public static String chosenWord = "officially";
    public static AtomicInteger smallerDistance = new AtomicInteger(Integer.MAX_VALUE);
    public static String smallerDistanceWord = "";

    public static void readFile(Path path) {
        try(Scanner sc = new Scanner(path);) {

            while(sc.hasNext()) {
                String word = sc.next();
                int distance = new LevenshteinExecutor(chosenWord, word).run();

                int smallerDistanceRead = br.ufrn.imd.concorrente.Main.smallerDistance.get();
                if (distance < smallerDistanceRead) {
                    br.ufrn.imd.concorrente.Main.smallerDistance.compareAndSet(smallerDistanceRead, distance);
                    br.ufrn.imd.concorrente.Main.smallerDistanceWord = word;
                    System.out.println("Found new better word: " + word + " with distance " + distance);
                }
            }
        } catch (IOException exception) {
            throw new RuntimeException("Erro ao ler arquivo", exception);
        }
    }

    public static void main(String[] args) throws IOException {
        long startTime = System.nanoTime();

        try (Stream<Path> paths = Files.walk(Paths.get("/Users/vtex/faculdade/concorrente/concorrente-levenshtein/dataset"))) {
            paths
                    .filter(Files::isRegularFile)
                    .forEach(Main::readFile);
        }

        long endTime = System.nanoTime();
        long duration = (endTime - startTime) / 1000000000;
        System.out.println(duration + " seconds");

        System.out.println("Menor distância: " + smallerDistance + "\nPalavra de menor distância: " + smallerDistanceWord);
    }
}
