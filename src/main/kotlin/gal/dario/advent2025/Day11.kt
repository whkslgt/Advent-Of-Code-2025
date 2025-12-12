package gal.dario.advent2025

class Day11 {
    companion object {
        const val INPUT_FILE = "/day-11-input.txt"
    }

    val inputMap = readInput().associate {
        val keyValuePair = it.split(": ")
        keyValuePair[0] to keyValuePair[1].split(' ')
    }

    fun readInput(): List<String> {
        return object {}.javaClass
            .getResourceAsStream(INPUT_FILE)!!
            .bufferedReader()
            .readLines()
    }

    fun part1(): Long {
        val first = inputMap["you"]!!
        return first.sumOf { you ->
            countPathsToOut(you).toLong()
        }
    }

    private val memo = mutableMapOf<Triple<String, Boolean, Boolean>, Long>()

    fun part2(): Long {
        memo.clear()
        val first = inputMap["svr"]!!
        return first.distinct().sumOf { svr ->
            val visitedDac = svr == "dac"
            val visitedFft = svr == "fft"
            countPathsFromSvrToOut(svr, visitedDac, visitedFft)
        }
    }

    private fun countPathsFromSvrToOut(current: String, visitedDac: Boolean, visitedFft: Boolean): Long {
        if (current == "out") {
            return if (visitedDac && visitedFft) 1L else 0L
        }

        val state = Triple(current, visitedDac, visitedFft)
        if (memo.containsKey(state)) {
            return memo[state]!!
        }

        val count = inputMap[current]!!.distinct().sumOf { next ->
            countPathsFromSvrToOut(
                next,
                visitedDac || next == "dac",
                visitedFft || next == "fft"
            )
        } ?: 0

        memo[state] = count
        return count
    }

    private fun countPathsToOut(current: String): Int {
        if (current == "out") return 1

        return inputMap[current]?.sumOf { next ->
            countPathsToOut(next)
        } ?: 0
    }
}

fun main() {
    Day11().part1().also(::println)
    Day11().part2().also(::println)
}
