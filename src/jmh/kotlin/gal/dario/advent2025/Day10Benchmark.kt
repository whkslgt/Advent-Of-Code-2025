package gal.dario.advent2025

import org.openjdk.jmh.annotations.*
import java.util.concurrent.TimeUnit

@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 3, time = 1)
@Measurement(iterations = 5, time = 1)
@Fork(2)
open class Day10Benchmark {

    private lateinit var day10: Day10

    @Setup
    fun setup() {
        day10 = Day10()
    }

    @Benchmark
    fun benchmarkPart1(): Int {
        return day10.part1()
    }

    @Benchmark
    fun benchmarkPart2(): Int {
        return day10.part2()
    }
}
