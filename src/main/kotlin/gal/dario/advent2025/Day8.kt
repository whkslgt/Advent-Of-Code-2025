package gal.dario.advent2025

class Day8 {
    companion object {
        const val INPUT_FILE = "/day-8-input.txt"
    }

    data class Box(val x: Int, val y: Int, val z: Int) {
        fun distanceSquaredTo(other: Box): Long {
            val dx = (this.x - other.x).toLong()
            val dy = (this.y - other.y).toLong()
            val dz = (this.z - other.z).toLong()
            return dx * dx + dy * dy + dz * dz
        }
    }

    data class Connection(val boxIndexA: Int, val boxIndexB: Int, val distance: Long)

    class CircuitTracker(val numberOfBoxes: Int) {
        private val circuitIds = IntArray(numberOfBoxes) { it }
        private val circuitSizes = IntArray(numberOfBoxes) { 1 }

        private fun getCircuitId(boxIndex: Int): Int {
            if (circuitIds[boxIndex] != boxIndex) {
                circuitIds[boxIndex] = getCircuitId(circuitIds[boxIndex]) // Optimization: flatten the path
            }
            return circuitIds[boxIndex]
        }

        fun connect(boxIndexA: Int, boxIndexB: Int): Boolean {
            val circuitA = getCircuitId(boxIndexA)
            val circuitB = getCircuitId(boxIndexB)

            if (circuitA != circuitB) {
                if (circuitSizes[circuitA] < circuitSizes[circuitB]) {
                    circuitIds[circuitA] = circuitB
                    circuitSizes[circuitB] += circuitSizes[circuitA]
                } else {
                    circuitIds[circuitB] = circuitA
                    circuitSizes[circuitA] += circuitSizes[circuitB]
                }
                return true
            }
            return false
        }

        fun getAllCircuitSizes(): List<Int> {
            val distinctSizes = mutableMapOf<Int, Int>()
            for (i in circuitIds.indices) {
                val circuitId = getCircuitId(i)
                distinctSizes[circuitId] = circuitSizes[circuitId]
            }
            return distinctSizes.values.toList()
        }
    }

    private fun readInput(): Sequence<String> {
        return object {}.javaClass
            .getResourceAsStream(INPUT_FILE)!!
            .bufferedReader()
            .lineSequence()
    }

    fun part2(): Long {
        val (boxes, sortedConnections) = getBoxConnections()
        val circuits = CircuitTracker(boxes.size)
        var clustersCount = boxes.size

        for (connection in sortedConnections) {
            if (circuits.connect(connection.boxIndexA, connection.boxIndexB)) {
                clustersCount--
                if (clustersCount == 1) {
                    return boxes[connection.boxIndexA].x.toLong() * boxes[connection.boxIndexB].x.toLong()
                }
            }
        }
        return 0L
    }

    fun part1(): Long {
        val (boxes, sortedConnections) = getBoxConnections()

        val circuits = CircuitTracker(boxes.size)
        val limit = minOf(1000, sortedConnections.size)
        for (i in 0 until limit) {
            circuits.connect(sortedConnections[i].boxIndexA, sortedConnections[i].boxIndexB)
        }

        return circuits.getAllCircuitSizes()
            .sortedDescending()
            .take(3)
            .reduce { acc, i -> acc * i }.toLong()
    }

    private fun getBoxConnections(): Pair<List<Box>, List<Connection>> {
        val boxes = readInput()
            .filter { it.isNotBlank() }
            .map {
                val (x, y, z) = it.trim().split(",").map { pos -> pos.toInt() }
                Box(x, y, z)
            }
            .toList()

        val allConnections = ArrayList<Connection>((boxes.size * (boxes.size - 1)) / 2)
        for (i in boxes.indices) {
            for (j in (i + 1) until boxes.size) {
                val dist = boxes[i].distanceSquaredTo(boxes[j])
                allConnections.add(Connection(i, j, dist))
            }
        }

        allConnections.sortBy { it.distance }

        return Pair(boxes, allConnections)
    }
}

fun main() {
    Day8().part1().also(::println)
    Day8().part2().also(::println)
}
