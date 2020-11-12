package br.ufrn.imd.tests.JMH;

import br.ufrn.imd.callable.CallableLevenshteinExecutor;
import br.ufrn.imd.LevenshteinExecutor;
import br.ufrn.imd.concorrente.WordRunner;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class BenchTest {
    @Benchmark
    @Fork(value = 1, warmups = 1)
    @BenchmarkMode(Mode.All)
    @Warmup(iterations = 3)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void distanceUpdateBenchmark() {
        AtomicInteger smallerDistance = new AtomicInteger(Integer.MAX_VALUE);
        WordRunner.checkAndUpdateDistance(smallerDistance, new Random().nextInt(Integer.MAX_VALUE));
    }

    @Benchmark
    @Fork(value = 1, warmups = 1)
    @BenchmarkMode(Mode.All)
    @Warmup(iterations = 3)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void levenshteinCalculationBenchmark(Blackhole blackhole) {
        String foo = "testedebenchmark";
        String boo = "benchmarktest";

        blackhole.consume(new LevenshteinExecutor(foo, boo).run());
    }

    @Benchmark
    @Fork(value = 1, warmups = 1)
    @BenchmarkMode(Mode.All)
    @Warmup(iterations = 3)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public Integer distanceUpdateBenchmarkCallable() {
        Integer smallerDistance = Integer.MAX_VALUE;
        return br.ufrn.imd.callable.WordRunner.getSmallerDistance(smallerDistance, new Random().nextInt(Integer.MAX_VALUE));
    }

    @Benchmark
    @Fork(value = 1, warmups = 1)
    @BenchmarkMode(Mode.All)
    @Warmup(iterations = 3)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void levenshteinCalculationBenchmarkCallable(Blackhole blackhole) {
        String foo = "testedebenchmark";
        String boo = "benchmarktest";

        blackhole.consume(new CallableLevenshteinExecutor(foo, boo).run());
    }
}