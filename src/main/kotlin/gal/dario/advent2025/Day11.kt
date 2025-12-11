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

    private fun countPathsToOut(current: String): Int {
        if (current == "out") return 1

        return inputMap[current]?.sumOf { next ->
            countPathsToOut(next)
        } ?: 0
    }
}

fun main() {
    Day11().part1().also(::println)
}
