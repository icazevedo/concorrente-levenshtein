package br.ufrn.imd.callable;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Scanner;
import java.util.concurrent.Callable;

public class FileRunner implements Callable<Integer> {
    final Path filePath;

    public FileRunner(Path filePath) {
        this.filePath = filePath;
    }

    @Override
    public Integer call() {
        try (Scanner sc = new Scanner(filePath)) {
            Integer minDistance = Integer.MAX_VALUE;

            while (sc.hasNext()) {
                // Recebe próxima palavra ao invocar sc.next()
                // Rodar run() faz com que o código do runner seja executado
                Integer distanceOfWord = new WordRunner(sc.next()).call();
                minDistance = WordRunner.getSmallerDistance(minDistance, distanceOfWord);
            }

            return minDistance;
        } catch (IOException exception) {
            throw new RuntimeException("Erro ao ler arquivo", exception);
        }
    }
}
