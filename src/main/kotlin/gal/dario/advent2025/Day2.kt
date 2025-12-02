package gal.dario.advent2025

class Day2() {
    private companion object {
        const val INPUT_FILE = "/day-2-input.txt"
    }

    private fun readInput(): List<String> {
        return object {}.javaClass
            .getResourceAsStream(INPUT_FILE)!!
            .bufferedReader()
            .readLine()
            .split(',')
    }

    private fun isInvalidId(s: String): Boolean {
        for (patternLength in 1..s.length / 2) {
            if (s.length % patternLength == 0) {
                val pattern = s.take(patternLength)
                val repetitions = s.length / patternLength

                if (repetitions >= 2 && pattern.repeat(repetitions) == s) {
                    return true
                }
            }
        }
        return false
    }

    private fun sumInvalidIdsInRange(start: Long, end: Long) =
        (start..end)
            .filter { num -> isInvalidId(num.toString()) }
            .sumOf { it }

    fun calculateBadIDsSum(): Long {
        return readInput().sumOf { range ->
            val (start, end) = range.split('-').map { it.toLong() }
            sumInvalidIdsInRange(start, end)
        }
    }
}

fun main() {
    val sum = Day2().calculateBadIDsSum()
    println("Sum: $sum")
}
