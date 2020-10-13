package br.ufrn.imd.concorrente;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Scanner;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
    public static String chosenWord = "officially";
    public static AtomicInteger smallerDistance = new AtomicInteger(Integer.MAX_VALUE);
    public static String smallerDistanceWord = "";

    public static void waitFor(Collection<Future<?>> c) throws InterruptedException, ExecutionException {
        for(Future<?> f : c) f.get();
    }

    public static Future<?> readFile(Path path, ThreadPoolExecutor executor) {
        FileRunner fileRunner = new FileRunner(path);
        return executor.submit(fileRunner);
    }

    public static void main(String[] args) {
        long startTime = System.nanoTime();

        System.out.println(Runtime.getRuntime().availableProcessors() + " cores available");
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        try (Stream<Path> paths = Files.walk(Paths.get(args[0]))) {
            Stream<Future<?>> fileThread = paths
                    .filter(Files::isRegularFile)
                    .map((path) -> readFile(path, executor));

            waitFor(fileThread.collect(Collectors.toSet()));
            executor.shutdownNow();
        } catch (IOException | InterruptedException | ExecutionException e) {
            throw new RuntimeException("Too bad, has been an error!", e);
        }

        long endTime = System.nanoTime();
        long duration = (endTime - startTime) / 1000000000;
        System.out.println(duration + " seconds");

        System.out.println("Menor distância: " + smallerDistance + "\nPalavra de menor distância: " + smallerDistanceWord);
    }
}
