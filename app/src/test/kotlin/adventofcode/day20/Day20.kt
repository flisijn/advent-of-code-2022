package adventofcode.day20

import adventofcode.common.getResourceAsText
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day20 {
    private fun load(path: String) = getResourceAsText(path)
        .split("\n")
        .filter { it.isNotBlank() }
        .map { it.toInt() }

    class WrappedItem(val value: Long)

    private fun solve(input: List<Int>, multiplier: Long, mixCount: Int): Long {
        val startItems = input.map { WrappedItem(multiplier * it) }
        val zeroItem = startItems.first { it.value == 0L }
        val list = startItems.toMutableList()
        repeat(mixCount) {
            startItems.forEach { item ->
                val idx = list.indexOf(item)
                list.removeAt(idx)
                val newIdx = (idx + item.value - 1).mod(list.size) + 1
                list.add(newIdx, item)
            }
        }

        val zeroIdx = list.indexOf(zeroItem)
        return listOf(1000L, 2000L, 3000L).sumOf {
            val wrappedIdx = (it + zeroIdx).mod(list.size)
            list[wrappedIdx].value
        }
    }

    private fun part1(input: List<Int>) = solve(input, 1L, 1)
    private fun part2(input: List<Int>) = solve(input, 811589153L, 10)

    @Test
    fun test() {
        val testData1 = load("test.txt")
        assertEquals(3L, part1(testData1))
        val testData2 = load("test.txt")
        assertEquals(1623178306L, part2(testData2))
    }

    @Test
    fun run() {
        val inputData1 = load("input.txt")
        println("part 1: ${part1(inputData1)}")
        val inputData2 = load("input.txt")
        println("part 2: ${part2(inputData2)}")
    }
}
