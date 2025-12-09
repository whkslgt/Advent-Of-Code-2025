package gal.dario.advent2025

import java.util.stream.Stream

class Day9 {
    companion object {
        const val INPUT_FILE = "/day-9-input.txt"
    }

    fun readInput(): Stream<String> {
        return object {}.javaClass
            .getResourceAsStream(INPUT_FILE)!!
            .bufferedReader()
            .lines()
    }

    private val inputTiles: List<Tile> = readInput().map { it.toTile() }.toList()

    fun part1(): Long {
        val sortedTiles = inputTiles.sorted()

        var maxArea = 0L
        for (i in 0 until sortedTiles.size / 2) {
            for (j in sortedTiles.size - 1 downTo sortedTiles.size / 2) {
                val (a, b) = sortedTiles[i] to sortedTiles[j]
                if (a.y < b.y) {
                    maxArea = maxOf(maxArea, rectangleFrom(a, b).area)
                }
            }
        }

        return maxArea
    }

    fun part2(): Long {
        val polygon = inputTiles

        val candidates = buildList {
            for (i in polygon.indices) {
                for (j in i + 1 until polygon.size) {
                    add(rectangleFrom(polygon[i], polygon[j]))
                }
            }
        }

        return candidates.sortedByDescending { it.area }
            .firstOrNull { isValidRectangle(it.minX, it.maxX, it.minY, it.maxY, polygon) }
            ?.area ?: 0L
    }

    private fun getBounds(a: Int, b: Int) = if (a < b) Bounds(a, b) else Bounds(b, a)

    private fun edgeIntersectsRectangle(edgePos: Int, edgeRectMin: Int, edgeRectMax: Int, edgeBounds: Bounds, boundsRectMin: Int, boundsRectMax: Int): Boolean {
        if (edgePos in (edgeRectMin + 1)..<edgeRectMax) {
            if (maxOf(edgeBounds.min, boundsRectMin) < minOf(edgeBounds.max, boundsRectMax)) return true
        }
        return false
    }

    private fun rectangleFrom(p1: Tile, p2: Tile): Rectangle {
        val minX = minOf(p1.x, p2.x)
        val maxX = maxOf(p1.x, p2.x)
        val minY = minOf(p1.y, p2.y)
        val maxY = maxOf(p1.y, p2.y)
        val area = ((maxX - minX) + 1L) * ((maxY - minY) + 1L)
        return Rectangle(minX, maxX, minY, maxY, area)
    }

    private fun isValidRectangle(
        minX: Int,
        maxX: Int,
        minY: Int,
        maxY: Int,
        polygon: List<Tile>
    ): Boolean {
        val centerX = (minX + maxX) / 2.0
        val centerY = (minY + maxY) / 2.0
        if (!isPointInPolygon(centerX, centerY, polygon)) return false

        val n = polygon.size
        for (i in 0 until n) {
            val v1 = polygon[i]
            val v2 = polygon[(i + 1) % n]

            if (v1.x == v2.x) {
                if (edgeIntersectsRectangle(v1.x, minX, maxX, getBounds(v1.y, v2.y), minY, maxY)) return false
            } else {
                if (edgeIntersectsRectangle(v1.y, minY, maxY, getBounds(v1.x, v2.x), minX, maxX)) return false
            }
        }
        return true
    }

    private fun isPointInPolygon(x: Double, y: Double, polygon: List<Tile>): Boolean {
        var inside = false
        var j = polygon.size - 1
        for (i in polygon.indices) {
            val xi = polygon[i].x.toDouble()
            val yi = polygon[i].y.toDouble()
            val xj = polygon[j].x.toDouble()
            val yj = polygon[j].y.toDouble()

            val intersect = ((yi > y) != (yj > y)) &&
                    (x < (xj - xi) * (y - yi) / (yj - yi) + xi)
            if (intersect) inside = !inside
            j = i
        }
        return inside
    }

    private fun String.toTile(): Tile{
        val (x, y) = split(",")
        return Tile(x.toInt(), y.toInt())
    }

    private data class Tile(val x: Int, val y: Int): Comparable<Tile>{
        override fun compareTo(other: Tile): Int {
            return x.compareTo(other.x).takeIf { it != 0 } ?: y.compareTo(other.y)
        }
    }

    private data class Bounds(val min: Int, val max: Int)

    private data class Rectangle(val minX: Int, val maxX: Int, val minY: Int, val maxY: Int, val area: Long)
}

fun main() {
    val day9 = Day9()
    day9.part1().also(::println)
    day9.part2().also(::println)
}
