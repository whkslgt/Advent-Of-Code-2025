package gal.dario.advent2025

import java.io.BufferedReader

class Day7 {
    companion object {
        const val INPUT_FILE = "/day-7-input.txt"
    }

    val input = readInput().readLines()

    fun readInput(): BufferedReader {
        return object {}.javaClass
            .getResourceAsStream(INPUT_FILE)!!
            .bufferedReader()
    }

    fun part1(): Long {
        var splits = 0L
        var beams = mutableSetOf<Int>()

        val firstLine = input.firstOrNull() ?: return 0
        val startX = firstLine.indexOf('S')
        if (startX != -1) {
            beams.add(startX)
        }

        // Process the rest of the lines
        input.drop(1).forEach { line ->
            val nextBeams = mutableSetOf<Int>()

            for (x in beams) {
                if (x in line.indices) {
                    when (line[x]) {
                        '.' -> {
                            nextBeams.add(x)
                        }
                        '^' -> {
                            splits += 1
                            nextBeams.add(x - 1)
                            nextBeams.add(x + 1)
                        }
                    }
                }
            }
            beams = nextBeams
        }
        return splits
    }
}

fun main() {
    Day7().part1().also(::println)
}
