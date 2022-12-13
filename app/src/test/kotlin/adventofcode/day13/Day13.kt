package adventofcode.day13

import adventofcode.common.getResourceAsText
import org.junit.jupiter.api.Test
import java.util.Stack
import kotlin.test.assertEquals

class Day13 {
    private val tokenRegex = """(\[|]|,|\d+)""".toRegex()

    private fun load(path: String) = getResourceAsText(path)
        .split("\n\n")
        .map { it.split("\n").filter { line -> line.isNotBlank() }.map { parse(it) } }

    private fun parse(string: String): Content {
        val wrapper = Array()
        val stack = Stack<Content>()
        stack.push(wrapper)

        tokenRegex.findAll(string)
            .map { match -> match.value }
            .forEach { token ->
                when (token) {
                    "[" -> stack.push(Array())
                    "]" -> {
                        val inner = stack.pop()
                        val outer = stack.peek() as Array
                        outer.items += inner
                    }

                    "," -> {}
                    else -> {
                        val array = stack.peek() as Array
                        array.items += Value(token.toInt())
                    }
                }
            }
        return wrapper.items[0]
    }


    sealed class Content : Comparable<Content>

    data class Array(val items: MutableList<Content> = mutableListOf()) : Content() {
        override fun toString() = items.joinToString(prefix = "[", separator = ",", postfix = "]")
        override fun compareTo(other: Content): Int = when (other) {
            is Array -> {
                items.zip(other.items)
                    .map { (left, right) -> left.compareTo(right) }
                    .firstOrNull { it != 0 }
                    ?: items.size.compareTo(other.items.size)
            }

            is Value -> compareTo(other.toArray())
        }
    }

    data class Value(val value: Int) : Content() {
        fun toArray() = Array(items = mutableListOf(this))
        override fun toString() = value.toString()
        override fun compareTo(other: Content): Int = when (other) {
            is Value -> value.compareTo(other.value)
            is Array -> toArray().compareTo(other)
        }
    }

    private fun part1(input: List<List<Content>>): Int {
        return input.asSequence()
            .withIndex()
            .filter { it.value.first() < it.value.last() }
            .sumOf { it.index + 1 }
    }

    private fun part2(input: List<List<Content>>): Int {
        val dividers = listOf("[[2]]", "[[6]]").map { parse(it) }
        return input.asSequence()
            .flatten()
            .plus(dividers)
            .sorted()
            .withIndex()
            .filter { it.value in dividers }
            .map { it.index + 1 }
            .reduce(Int::times)
    }

    @Test
    fun test() {
        val testData1 = load("test.txt")
        assertEquals(13, part1(testData1))
        val testData2 = load("test.txt")
        assertEquals(140, part2(testData2))
    }

    @Test
    fun run() {
        val inputData1 = load("input.txt")
        println("part 1: ${part1(inputData1)}")
        val inputData2 = load("input.txt")
        println("part 2: ${part2(inputData2)}")
    }
}
