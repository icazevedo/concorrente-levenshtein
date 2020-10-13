package br.ufrn.imd.sequencial;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.stream.Stream;

import br.ufrn.imd.LevenshteinExecutor;

public class Main {
    public static String chosenWord;
    //Não é necessário AtomicInteger dado que as operações são feitas de maneira serial
    public static int smallerDistance = Integer.MAX_VALUE;

    public static void readFile(Path path) {
        try(Scanner sc = new Scanner(path);) {
            while(sc.hasNext()) {
                //Executa a versão iterativa do algoritmo de levenshtein para cada palavra
                int distance = new LevenshteinExecutor(chosenWord, sc.next()).run();

                if (distance < smallerDistance) {
                    smallerDistance = distance;
                }
            }
        } catch (IOException exception) {
            throw new RuntimeException("Erro ao ler arquivo", exception);
        }
    }

    public static void main(String[] args) {
        long startTime = System.nanoTime();

        //Define palavra a ser comparada com o dataset
        chosenWord = args[0];

        //Recebe todos os paths dos arquivos do dataset (args[1])
        try (Stream<Path> paths = Files.walk(Paths.get(args[1]))) {
            paths
                    .filter(Files::isRegularFile)
                    .forEach(Main::readFile);
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
