package adventofcode.common

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
        val testData1 = load("test.txt")
        assertEquals(0, part1(testData1))
        val testData2 = load("test.txt")
        assertEquals(0, part2(testData2))
    }

    @Test
    fun run() {
        val inputData1 = load("input.txt")
        println("part 1: ${part1(inputData1)}")
        val inputData2 = load("input.txt")
        println("part 2: ${part2(inputData2)}")
    }
}
