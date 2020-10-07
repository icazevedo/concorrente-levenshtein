package br.ufrn.imd.concorrente;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Scanner;

public class FileRunner implements Runnable {
    final Path filePath;

    public FileRunner(Path filePath) {
        this.filePath = filePath;
    }

    @Override
    public void run() {
        try (Scanner sc = new Scanner(filePath)) {
            while (sc.hasNext()) {
                String word = sc.next();
                System.out.println("Checking word: " + word);
                LevenshteinExecutor levenshteinExecutor = new LevenshteinExecutor(Main.chosenWord.length(), word.length(), new LevenshteinData(Main.chosenWord, word));
                levenshteinExecutor.run();
                int distance = levenshteinExecutor.value;

                if(distance == -1) {
                    System.out.println("Bad small for word: " + word);
                    continue;
                }

                if (distance < Main.smallerDistance) {
                    System.out.println("Found new better word: " + word + " with distance " + distance);
                    Main.smallerDistance = distance;
                    Main.smallerDistanceWord = word;
                }
            }
        } catch (IOException exception) {
            throw new RuntimeException("Erro ao ler arquivo", exception);
        }
//        catch(InterruptedException exception) {
//            throw new RuntimeException("Erro na thread do levenshtein", exception);
//        }
    }
}
