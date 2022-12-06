package adventofcode.day06

import adventofcode.common.getResourceAsText
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day06 {
    private fun load(path: String) = getResourceAsText(path)

    private fun findMarker(input: String, markerSize: Int): Int {
        return (markerSize..input.length)
            .withIndex()
            .first { (start, end) ->
                input.toCharArray(start, end).toSet().size == markerSize
            }.value
    }

    private fun part1(testData: String): Int {
        return findMarker(testData, 4)
    }

    private fun part2(testData: String): Int {
        return findMarker(testData, 14)
    }

    @Test
    fun test() {
        val testData = load("/day06/test.txt")

        val part1 = part1(testData)
        assertEquals(7, part1)

        val part2 = part2(testData)
        assertEquals(19, part2)
    }

    @Test
    fun run() {
        val inputData = load("/day06/input.txt")

        val part1 = part1(inputData)
        println("part 1: $part1")

        val part2 = part2(inputData)
        println("part 2: $part2")
    }
}
