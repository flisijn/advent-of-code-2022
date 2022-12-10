package adventofcode.common

import adventofcode.common.getResourceAsText
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day00 {
    private fun load(path: String) = getResourceAsText(path)
        .split("\n")
        .filter { it.isNotBlank() }


    private fun part1(input: List<String>): Int {
        TODO()
    }

    private fun part2(input: List<String>): Int {
        TODO()
    }

    @Test
    fun test() {
        val testData = load("test.txt")
        assertEquals(0, part1(testData))
        assertEquals(0, part2(testData))
    }

    @Test
    fun run() {
        val inputData = load("input.txt")
        println("part 1: ${part1(inputData)}")
        println("part 2: ${part2(inputData)}")
    }
}
