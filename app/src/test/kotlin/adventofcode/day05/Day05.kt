package adventofcode.day05

import adventofcode.common.getResourceAsText
import org.junit.jupiter.api.Test
import java.util.Stack
import kotlin.test.assertEquals

class Day05 {
    class Procedure(val stacks: List<Stack<Char>>, val actions: List<Triple<Int, Int, Int>>)

    private fun load(path: String): Procedure {
        val parts = getResourceAsText(path)
            .split("\n\n")
            .filter { it.isNotBlank() }
            .map { part ->
                part.split("\n")
                    .filter { it.isNotBlank() }
            }

        val stackData = parts[0]
            .dropLast(1)
            .map { line ->
                line.chunked(4)
                    .map { it[1] }
            }

        val stackCount = stackData.maxOf { it.count() }
        val stacks = arrayOfNulls<Stack<Char>>(stackCount)
        for (row in stackData.count() - 1 downTo 0) {
            stackData[row].forEachIndexed { idx, c ->
                if (c != ' ') {
                    val s = stacks[idx] ?: Stack()
                    s.push(c)
                    stacks[idx] = s
                }
            }
        }
        val filledStacks = stacks.map { it!! }

        val actions = parts[1].map { line ->
            val s = line.split(" ")
            Triple(s[1].toInt(), s[3].toInt(), s[5].toInt())
        }
        return Procedure(filledStacks, actions)
    }


    private fun part1(testData: Procedure): String {
        val stacks = testData.stacks
        testData.actions.forEach { action ->
            val (count, from, to) = action
            for (run in 1..count) {
                val item = stacks[from - 1].pop()
                stacks[to - 1].push(item)
            }
        }

        return getResult(stacks)
    }

    private fun part2(testData: Procedure): String {
        val stacks = testData.stacks
        testData.actions.forEach { action ->
            val (count, from, to) = action
            val temp = Stack<Char>()
            for (run in 1..count) {
                val item = stacks[from - 1].pop()
                temp.push(item)
            }
            for (run in 1..count) {
                val item = temp.pop()
                stacks[to - 1].push(item)
            }
        }

        return getResult(stacks)
    }

    private fun getResult(stacks: List<Stack<Char>>): String {
        val chars = stacks.map { stack ->
            stack.peek()!!
        }.toCharArray()
        return String(chars)
    }

    @Test
    fun test() {
        val testData1 = load("test.txt")
        val part1 = part1(testData1)
        assertEquals("CMZ", part1)

        val testData2 = load("test.txt")
        val part2 = part2(testData2)
        assertEquals("MCD", part2)
    }

    @Test
    fun run() {
        val inputData1 = load("input.txt")

        val part1 = part1(inputData1)
        println("part 1: $part1")

        val inputData2 = load("input.txt")
        val part2 = part2(inputData2)
        println("part 2: $part2")
    }
}
