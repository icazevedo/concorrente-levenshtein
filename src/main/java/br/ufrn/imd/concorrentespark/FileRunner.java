package br.ufrn.imd.concorrentespark;

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
                // Recebe próxima palavra ao invocar sc.next()
                // Rodar run() faz com que o código do runner seja executado
                //
                // Está como runnable só pra facilitar o entendimento, e por que antes eu estava
                // prototipando com threads nesse setor.
                new WordRunner(sc.next()).run();
            }
        } catch (IOException exception) {
            throw new RuntimeException("Erro ao ler arquivo", exception);
        }
    }
}
