package gal.dario.advent2025

import org.openjdk.jmh.annotations.*
import java.math.BigInteger
import java.util.concurrent.TimeUnit

@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 3, time = 1)
@Measurement(iterations = 5, time = 1)
@Fork(2)
open class Day7Benchmark {

    private lateinit var day7: Day7

    @Setup
    fun setup() {
        day7 = Day7()
    }

    @Benchmark
    fun benchmarkPart1(): Int {
        return day7.part1()
    }

    @Benchmark
    fun benchmarkPart2(): BigInteger {
        return day7.part2()
    }
}
