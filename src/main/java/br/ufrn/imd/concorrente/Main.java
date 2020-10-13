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
    public static String chosenWord;
    //smallerDistance é inicializada com o maior inteiro para que qualquer palavra seja inicialmente menos distante
    public static AtomicInteger smallerDistance = new AtomicInteger(Integer.MAX_VALUE);

    public static void waitFor(Collection<Future<?>> c) throws InterruptedException, ExecutionException {
        //Equivalente a executar .join() nas threads
        for(Future<?> f : c) f.get();
    }

    public static Future<?> readFile(Path path, ThreadPoolExecutor executor) {
        //Adiciona thread do arquivo à Thread Pool
        return executor.submit(new FileRunner(path));
    }

    public static void main(String[] args) {
        //Inicia a contagem da duração - recebe a hora atual do sistema
        long startTime = System.nanoTime();

        //Define palavra a ser comparada com o dataset
        chosenWord = args[0];

        //Instancia uma Thread Pool com a quantidade máxima equivalente ao número de processadores (núcleos) disponívels
        System.out.println(Runtime.getRuntime().availableProcessors() + " cores available");
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        //Recebe todos os paths dos arquivos do dataset (args[1])
        try (Stream<Path> paths = Files.walk(Paths.get(args[1]))) {
            Stream<Future<?>> fileThread = paths
                    .filter(Files::isRegularFile)
                    //Executa o método readFile para todos
                    .map((path) -> readFile(path, executor));

            //Chama o método waitFor para esperar a finalização da execução das threads
            waitFor(fileThread.collect(Collectors.toSet()));

            //Termina a Thread Pool
            executor.shutdownNow();
        } catch (IOException | InterruptedException | ExecutionException e) {
            throw new RuntimeException("Too bad, has been an error!", e);
        }

        //Finaliza a contagem da duração - recebe a hora atual do sistema ao fim da execução
        long endTime = System.nanoTime();

        //Exibe a duração final de execução
        long duration = (endTime - startTime) / 1000000000;
        System.out.println(duration + " seconds");

        //Mostra a menor distância de levenshtein para a palavra escolhida em relação ao dataset fornecido
        System.out.println("Menor distância: " + smallerDistance);
    }
}
