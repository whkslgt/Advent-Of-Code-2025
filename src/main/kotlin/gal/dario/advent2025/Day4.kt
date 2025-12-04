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
    private data class Cell(var hasRoll: Boolean)

    fun calculateAccessibleRolls(): Int {
        val matrix = mutableMapOf<Coordinate, Cell>()
        var maxRow = 0
        var maxCol = 0

        readInput().forEachIndexed { row, line ->
            maxRow = maxOf(maxRow, row)
            line.forEachIndexed { col, char ->
                maxCol = maxOf(maxCol, col)
                matrix[Coordinate(row, col)] = Cell(char == '@')
            }
        }

        var iterationCount = 0
        var totalCount = 0
        do {
            iterationCount = 0
            for (row in 0..maxRow) {
                for (col in 0..maxCol) {
                    val coord = Coordinate(row, col)
                    val currentCell = matrix[coord]
                    if (currentCell!!.hasRoll && isAccessible(matrix, currentCell, coord)) {
                        currentCell.hasRoll = false
                        iterationCount++
                    }
                }
            }
            totalCount += iterationCount
        } while (iterationCount > 0)

        return totalCount
    }

    private fun isAccessible(matrix: Map<Coordinate, Cell>, currentCell: Cell, coord: Coordinate): Boolean {
        if (!currentCell.hasRoll) return false

        val neighbors = listOfNotNull(
            coord.copy(row = coord.row - 1),
            coord.copy(row = coord.row - 1, col = coord.col + 1),
            coord.copy(row = coord.row - 1, col = coord.col - 1),
            coord.copy(row = coord.row + 1),
            coord.copy(row = coord.row + 1, col = coord.col + 1),
            coord.copy(row = coord.row + 1, col = coord.col - 1),
            coord.copy(col = coord.col - 1),
            coord.copy(col = coord.col + 1)
        )

        return neighbors.filter { neighbor ->
            matrix[neighbor]?.hasRoll ?: false
        }.size < 4
    }
}

fun main() {
    println("Accessible rolls: ${Day4().calculateAccessibleRolls()}")
}
