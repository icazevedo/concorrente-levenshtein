package br.ufrn.imd.sequencial;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.stream.Stream;

public class Main {
    public static String chosenWord = "catnip";
    public static int smallerDistance = Integer.MAX_VALUE;
    public static String smallerDistanceWord = "";

    public static void readFile(Path path) {
        try(Scanner sc = new Scanner(path);) {

            while(sc.hasNext()) {
                String word = sc.next();
                int distance = LevenshteinExecutor.lev(chosenWord.length(), word.length(), new LevenshteinData(chosenWord, word));
                System.out.println(new StringBuilder().append(word).append(": ").append(distance));

                if(distance < smallerDistance) {
                    smallerDistance = distance;
                    smallerDistanceWord = word;
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
                    .limit(10)
                    .forEach(Main::readFile);
        }

        long endTime = System.nanoTime();
        long duration = (endTime - startTime);
        System.out.println(duration);

        System.out.println("Menor distância: " + smallerDistance + "\nPalavra de menor distância: " + smallerDistanceWord);
    }
}
