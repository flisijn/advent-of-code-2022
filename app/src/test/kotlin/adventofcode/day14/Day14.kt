package adventofcode.day14

import adventofcode.common.getResourceAsText
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day14 {
    private val regex = """(\d+),(\d+)""".toRegex()
    private fun load(path: String) = getResourceAsText(path)
        .split("\n")
        .filter { it.isNotBlank() }
        .map { line ->
            regex.findAll(line).map { result -> Coordinate(result.groupValues[1].toInt(), result.groupValues[2].toInt()) }
        }

    data class Coordinate(val x: Int, val y: Int) {
        val fallCoordinates get() = listOf(copy(y = y + 1), copy(y = y + 1, x = x - 1), copy(y = y + 1, x = x + 1))
    }

    private fun vertical(first: Coordinate, second: Coordinate) = first.x == second.x
    private fun horizontal(first: Coordinate, second: Coordinate) = first.y == second.y

    private fun coordinatesOfLinePart(coordinates: List<Coordinate>): List<Coordinate> {
        val (first, second) = coordinates
        return if (vertical(first, second)) {
            (coordinates.minOf { it.y }..coordinates.maxOf { it.y }).map { y -> Coordinate(first.x, y) }
        } else if (horizontal(first, second)) {
            (coordinates.minOf { it.x }..coordinates.maxOf { it.x }).map { x -> Coordinate(x, first.y) }
        } else {
            error("invalid line part: $first $second")
        }
    }

    private fun solve(input: List<Sequence<Coordinate>>, restCoordinate: (Coordinate, Int, Set<Coordinate>) -> Coordinate?): Int {
        val occupied = input.flatMap { line ->
            line.windowed(2).flatMap(::coordinatesOfLinePart)
        }.toMutableSet()

        val lowestY = occupied.maxOf { it.y }
        val start = Coordinate(500, 0)
        generateSequence(0) { it + 1 }.forEach {
            val coordinate = restCoordinate(start, lowestY, occupied)
            println("sand $it falls down to $coordinate")
            if (coordinate != null) {
                occupied += coordinate
            } else {
                return it
            }
        }
        error("not possible")
    }

    private fun restCoordinate1(start: Coordinate, lowestY: Int, occupied: Set<Coordinate>): Coordinate? {
        var lastPosition = start
        generateSequence(start) { it.fallCoordinates.firstOrNull { next -> next !in occupied } }.forEach { next ->
            if (next.y > lowestY) return null
            lastPosition = next
        }
        return lastPosition
    }

    private fun restCoordinate2(start: Coordinate, lowestY: Int, occupied: Set<Coordinate>): Coordinate? {
        if (start in occupied) return null
        var lastPosition = start
        generateSequence(start) { it.fallCoordinates.firstOrNull { next -> next !in occupied && next.y != lowestY + 2 } }.forEach { next ->
            lastPosition = next
        }
        return lastPosition
    }

    private fun part1(input: List<Sequence<Coordinate>>) = solve(input, ::restCoordinate1)

    private fun part2(input: List<Sequence<Coordinate>>) = solve(input, ::restCoordinate2)


    @Test
    fun test() {
        val testData1 = load("test.txt")
        assertEquals(24, part1(testData1))
        val testData2 = load("test.txt")
        assertEquals(93, part2(testData2))
    }

    @Test
    fun run() {
        val inputData1 = load("input.txt")
        println("part 1: ${part1(inputData1)}")
        val inputData2 = load("input.txt")
        println("part 2: ${part2(inputData2)}")
    }
}
