package br.ufrn.imd.concorrente;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
    public static String chosenWord = "catnip";
    public static int smallerDistance = Integer.MAX_VALUE;
    public static String smallerDistanceWord = "";

    public static void waitFor(Collection<? extends Thread> c) throws InterruptedException {
        for(Thread t : c) t.join();
    }

//    public static Thread readFile(Path path) {
    public static void readFile(Path path) {
        FileRunner fileRunner = new FileRunner(path);
//        Thread fileRunnerThread = new Thread(fileRunner);
//        fileRunnerThread.start();
        fileRunner.run();
    }

    public static void main(String[] args) throws IOException {
        long startTime = System.nanoTime();

        try (Stream<Path> paths = Files.walk(Paths.get("/Users/vtex/faculdade/concorrente/concorrente-levenshtein/dataset"))) {
//            Stream<Thread> fileThread = paths
//                    .filter(Files::isRegularFile)
//                    .limit(10)
//                    .map(Main::readFile);
//
//            waitFor(fileThread.collect(Collectors.toSet()));
            paths
                    .filter(Files::isRegularFile)
                    .limit(1)
                    .forEach(Main::readFile);
        } catch (Exception e) {
            System.out.println("Sucks, right?");
        }

        long endTime = System.nanoTime();
        long duration = (endTime - startTime);
        System.out.println(duration);

        System.out.println("Menor distância: " + smallerDistance + "\nPalavra de menor distância: " + smallerDistanceWord);
    }
}
