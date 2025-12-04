package gal.dario.advent2025


class Day4 {
    companion object {
        const val INPUT_FILE = "/day-4-input.txt"
    }

    private fun readInput(): Sequence<String> {
        return object {}.javaClass
            .getResourceAsStream(INPUT_FILE)!!
            .bufferedReader()
            .lineSequence()
    }

    private data class Coordinate(val row: Int, val col: Int)

    fun calculateAccessibleRolls(): Int {
        val rolls = parseMatrix().toMutableSet()
        var total = 0

        while (rolls.isNotEmpty()) {
            val toRemove = rolls.filter { coord ->
                val row = coord.row
                val col = coord.col
                var count = 0
                for (diffRow in -1..1) {
                    for (diffCol in -1..1) {
                        if ((diffRow or diffCol) != 0 && Coordinate(row + diffRow, col + diffCol) in rolls) {
                            if (++count >= 4) break
                        }
                    }
                    if (count >= 4) break
                }
                count < 4
            }
            if (toRemove.isEmpty()) break
            rolls.removeAll(toRemove)
            total += toRemove.size
        }

        return total
    }

    private fun parseMatrix(): Set<Coordinate> =
        readInput().flatMapIndexed { row, line ->
            line.mapIndexedNotNull { col, char ->
                Coordinate(row, col).takeIf { char == '@' }
            }
        }.toSet()
}

fun main() {
    println("Accessible rolls: ${Day4().calculateAccessibleRolls()}")
}
