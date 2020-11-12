package br.ufrn.imd.parallelstream;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
    public static String chosenWord;

    public static void main(String[] args) {
        int smallerDistance;
        //Inicia a contagem da duração - recebe a hora atual do sistema
        long startTime = System.nanoTime();

        //Define palavra a ser comparada com o dataset
        chosenWord = args[0];

        //Recebe todos os paths dos arquivos do dataset (args[1])
        try (Stream<Path> paths = Files.walk(Paths.get(args[1]))) {
            List<Path> pathList = paths.collect(Collectors.toList());

            smallerDistance = pathList.parallelStream()
                    .filter(Files::isRegularFile)
                    //Executa o método readFile para todos
                    .mapToInt((path) -> new FileRunner(path).call())
                    .min()
                    .orElse(-1);
        } catch (IOException e) {
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
