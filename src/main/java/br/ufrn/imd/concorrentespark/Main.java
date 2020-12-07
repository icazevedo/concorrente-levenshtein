package br.ufrn.imd.concorrentespark;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

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
        String filePath = args[1];
        int datasetSize = Integer.parseInt(args[2]); //3035

        //Instancia uma Thread Pool com a quantidade máxima equivalente ao número de processadores (núcleos) disponívels
        SparkConf conf = new SparkConf().setAppName("bestmatching").setMaster("local[*]");
        conf.set("fs.hdfs.impl",org.apache.hadoop.hdfs.DistributedFileSystem.class.getName());
        conf.set("fs.file.impl",org.apache.hadoop.fs.LocalFileSystem.class.getName());
        JavaSparkContext sc = new JavaSparkContext(conf);

        System.out.println(Runtime.getRuntime().availableProcessors() + " cores available");
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        List<Future<?>> fileThread = new ArrayList<>();

        for(int fileId = 0; fileId <= datasetSize; fileId++) {
            int finalFileId = fileId;
            fileThread.add(executor.submit(() -> {
                JavaRDD<String> lines = sc.textFile(filePath + finalFileId + ".txt");
                JavaRDD<String> words = lines.flatMap(line -> Arrays.asList(line.split(" ")).iterator());

                words.foreach(word -> new WordRunner(word).run());
            }));
        }

        try {
            waitFor(fileThread);

            //Termina a Thread Pool
            executor.shutdownNow();
        } catch (InterruptedException | ExecutionException e) {
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
