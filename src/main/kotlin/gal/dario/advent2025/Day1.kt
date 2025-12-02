package gal.dario.advent2025

class Day1 {
    private companion object {
        const val INPUT_FILE = "/day-1-input.txt"
        const val INITIAL_POSITION = 50
        const val MODULO = 100
    }

    private fun readInput(): Sequence<String> {
        return object {}.javaClass
            .getResourceAsStream(INPUT_FILE)
            ?.bufferedReader()
            ?.lineSequence()
            ?: emptySequence()
    }

    private fun countZeroCrossings(position: Int, signedDistance: Int): Int {
        return if (signedDistance >= 0) {
            Math.floorDiv(position + signedDistance, MODULO) - Math.floorDiv(position, MODULO)
        } else {
            Math.floorDiv(position - 1, MODULO) - Math.floorDiv(position + signedDistance - 1, MODULO)
        }
    }

    fun calculateZeroCount(): Int {
        var position = INITIAL_POSITION
        var totalCrossings = 0

        readInput().forEach { line ->
            val value = line.substring(1).toInt()
            val signedDistance = if (line[0] == 'R') value else -value

            totalCrossings += countZeroCrossings(position, signedDistance)
            position = Math.floorMod(position + signedDistance, MODULO)
        }

        return totalCrossings
    }
}

fun main(args: Array<String>) {
    val result = Day1().calculateZeroCount()
    println("Zero count: $result")
}
