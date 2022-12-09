package adventofcode.day04

import adventofcode.common.getResourceAsText
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day04 {
    private fun load(path: String) = getResourceAsText(path)
        .split("\n")
        .filter { it.isNotBlank() }
        .map { line ->
            line.split(",")
                .map { part -> part.split("-").map { it.toInt() } }
                .map { it[0]..it[1] }
        }.map { it.first() to it.last() }

    private fun IntRange.containsAll(other: IntRange) = first <= other.first && last >= other.last
    private fun IntRange.containsAny(other: IntRange) = contains(other.first) || contains(other.last)

    private fun part1(lines: List<Pair<IntRange, IntRange>>): Int {
        return lines.count { (first, second) ->
            first.containsAll(second) || second.containsAll(first)
        }
    }

    private fun part2(lines: List<Pair<IntRange, IntRange>>): Int {
        return lines.count { (first, second) ->
            first.containsAny(second) || second.containsAny(first)
        }
    }

    @Test
    fun test() {
        val testData = load("test.txt")

        val part1 = part1(testData)
        assertEquals(2, part1)

        val part2 = part2(testData)
        assertEquals(4, part2)
    }

    @Test
    fun run() {
        val inputData = load("input.txt")

        val part1 = part1(inputData)
        println("part 1: $part1")

        val part2 = part2(inputData)
        println("part 2: $part2")
    }
}
