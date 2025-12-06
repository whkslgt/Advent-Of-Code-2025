package gal.dario.advent2025

import org.openjdk.jmh.annotations.*
import java.util.concurrent.TimeUnit

@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 3, time = 1)
@Measurement(iterations = 5, time = 1)
@Fork(2)
open class Day5Benchmark {

    private lateinit var day5: Day5

    @Setup
    fun setup() {
        day5 = Day5()
    }

//    @Benchmark
    fun benchmarkCalculatePossibleFreshIngredients(): Long {
        return day5.calculatePossibleFreshIngredients()
    }

//    @Benchmark
    fun benchmarkCalculateFreshIngredients(): Int {
        return day5.calculateFreshIngredients()
    }
}
