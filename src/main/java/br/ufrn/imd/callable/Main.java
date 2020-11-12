package br.ufrn.imd.callable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
    public static String chosenWord;
    //smallerDistance é inicializada com o maior inteiro para que qualquer palavra seja inicialmente menos distante
    public static Integer smallerDistance = Integer.MAX_VALUE;

    public static void waitFor(Collection<Future<Integer>> c) throws InterruptedException, ExecutionException {
        //Equivalente a executar .join() nas threads
        for(Future<Integer> f : c) {
            smallerDistance = WordRunner.getSmallerDistance(smallerDistance, f.get());
        }
    }

    public static Future<Integer> readFile(Path path, ExecutorService executorService) {
        //Adiciona thread do arquivo à Thread Pool
        return executorService.submit(new FileRunner(path));
    }

    public static void main(String[] args) {
        //Inicia a contagem da duração - recebe a hora atual do sistema
        long startTime = System.nanoTime();

        //Define palavra a ser comparada com o dataset
        chosenWord = args[0];

        //Instancia uma Thread Pool com a quantidade máxima equivalente ao número de processadores (núcleos) disponívels
        System.out.println(Runtime.getRuntime().availableProcessors() + " cores available");
        ExecutorService executorService = new ScheduledThreadPoolExecutor(Runtime.getRuntime().availableProcessors());

        //Recebe todos os paths dos arquivos do dataset (args[1])
        try (Stream<Path> paths = Files.walk(Paths.get(args[1]))) {
            Stream<Future<Integer>> fileThread = paths
                    .filter(Files::isRegularFile)
                    //Executa o método readFile para todos
                    .map((path) -> readFile(path, executorService));

            //Chama o método waitFor para esperar a finalização da execução das threads
            waitFor(fileThread.collect(Collectors.toSet()));

            executorService.shutdownNow();
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
