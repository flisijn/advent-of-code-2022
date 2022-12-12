package adventofcode.day12

import adventofcode.common.getResourceAsText
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day12 {
    private fun load(path: String) = getResourceAsText(path)
        .split("\n")
        .filter { it.isNotBlank() }
        .let(::createElevationMap)

    private fun createElevationMap(input: List<String>): ElevationMap {
        val elevations = mutableMapOf<Coordinate, Int>()
        lateinit var start: Coordinate
        lateinit var end: Coordinate
        input.forEachIndexed { y, rows ->
            rows.forEachIndexed { x, c ->
                val coordinate = Coordinate(x, y)
                val elevation = when (c) {
                    'S' -> 'a'.also { start = coordinate }
                    'E' -> 'z'.also { end = coordinate }
                    else -> c
                }
                elevations[coordinate] = elevation - 'a'
            }
        }
        return ElevationMap(start, end, elevations)
    }

    data class Coordinate(val x: Int, val y: Int) {
        val allDirections: List<Coordinate>
            get() = listOf(copy(x = x + 1), copy(x = x - 1), copy(y = y + 1), copy(y = y - 1))
    }

    class ElevationMap(val start: Coordinate, val end: Coordinate, val elevations: Map<Coordinate, Int>) {
        fun allowedDirections(coordinate: Coordinate): List<Coordinate> {
            val maxElevation = elevations.getValue(coordinate) + 1
            return coordinate.allDirections.filter {
                val elevation = elevations[it]
                elevation != null && elevation <= maxElevation
            }
        }
    }

    private fun solve(map: ElevationMap, startingCoordinates: Collection<Coordinate>): Int {
        val distances = mutableMapOf<Coordinate, Int>()
        startingCoordinates.forEach { distances[it] = 0 }

        val todo = startingCoordinates.toMutableList()
        while (todo.isNotEmpty()) {
            val current = todo.minBy { distances.getValue(it) }
            todo -= current
            val currentDistance = distances.getValue(current) + 1

            map.allowedDirections(current).forEach { c ->
                val previousDistance = distances[c] ?: Int.MAX_VALUE
                if (currentDistance < previousDistance) {
                    distances[c] = currentDistance
                    todo += c
                }
            }
        }
        return distances.getValue(map.end)
    }

    private fun part1(map: ElevationMap): Int {
        return solve(map, listOf(map.start))
    }

    private fun part2(map: ElevationMap): Int {
        return solve(map, map.elevations.filterValues { it == 0 }.keys)
    }

    @Test
    fun test() {
        val testData1 = load("test.txt")
        assertEquals(31, part1(testData1))
        val testData2 = load("test.txt")
        assertEquals(29, part2(testData2))
    }

    @Test
    fun run() {
        val inputData1 = load("input.txt")
        println("part 1: ${part1(inputData1)}")
        val inputData2 = load("input.txt")
        println("part 2: ${part2(inputData2)}")
    }
}
