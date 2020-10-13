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

                int smallerDistanceRead = smallerDistance.get();
                if (distance < smallerDistanceRead) {
                    smallerDistance.compareAndSet(smallerDistanceRead, distance);
                    smallerDistanceWord = word;
                    System.out.println("Found new better word: " + word + " with distance " + distance);
                }
            }
        } catch (IOException exception) {
            throw new RuntimeException("Erro ao ler arquivo", exception);
        }
    }

    public static void main(String[] args) {
        long startTime = System.nanoTime();

        try (Stream<Path> paths = Files.walk(Paths.get(args[0]))) {
            paths
                    .filter(Files::isRegularFile)
                    .forEach(Main::readFile);
        } catch (IOException e) {
            throw new RuntimeException("Too bad, has been an error!", e);
        }

        long endTime = System.nanoTime();
        long duration = (endTime - startTime) / 1000000000;
        System.out.println(duration + " seconds");

        System.out.println("Menor distância: " + smallerDistance + "\nPalavra de menor distância: " + smallerDistanceWord);
    }
}
