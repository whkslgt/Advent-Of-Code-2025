package gal.dario.advent2025

import org.openjdk.jmh.annotations.*
import java.util.concurrent.TimeUnit

@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 3, time = 1)
@Measurement(iterations = 5, time = 1)
@Fork(2)
open class Day11Benchmark {

    private lateinit var day11: Day11

    @Setup
    fun setup() {
        day11 = Day11()
    }

    @Benchmark
    fun benchmarkPart1(): Long {
        return day11.part1()
    }

    @Benchmark
    fun benchmarkPart2(): Long {
        return day11.part2()
    }
}
