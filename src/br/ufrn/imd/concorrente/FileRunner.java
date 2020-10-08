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
//                System.out.println("Checking word: " + word);
                WordRunner wordRunner = new WordRunner(word);
                Thread wordThread = new Thread(wordRunner);
                wordThread.start();
            }
        } catch (IOException exception) {
            throw new RuntimeException("Erro ao ler arquivo", exception);
        }
    }
}
