package adventofcode.day10

import adventofcode.common.getResourceAsText
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day10 {
    private fun load(path: String) = getResourceAsText(path)
        .split("\n")
        .filter { it.isNotBlank() }


    private fun getCycles(input: List<String>) = input.flatMap { cmd ->
        when {
            cmd.startsWith("noop") -> listOf(0)
            cmd.startsWith("addx") -> listOf(0, cmd.split(" ").last().toInt())
            else -> error("invalid input")
        }
    }

    private fun part1(input: List<String>): Int {
        var x = 1
        var result = 0
        val interesting = listOf(20, 60, 100, 140, 180, 220)
        getCycles(input).forEachIndexed { index, delta ->
            val cycleNumber = index + 1
            if (cycleNumber in interesting) {
                result += cycleNumber * x
            }
            x += delta
        }
        return result
    }

    private fun part2(input: List<String>): String {
        var x = 1
        val output = MutableList(240) { "." }
        getCycles(input).forEachIndexed { index, delta ->
            val pixel = index % 40
            if (x in pixel-1..pixel+1) {
                output[index] = "#"
            }
            x += delta
        }

        return output.chunked(40).joinToString(separator = "\n") { line ->
            line.joinToString(separator = "")
        }
    }

    @Test
    fun test() {
        val testData = load("test.txt")
        assertEquals(13140, part1(testData))

        val expected = """
            ##..##..##..##..##..##..##..##..##..##..
            ###...###...###...###...###...###...###.
            ####....####....####....####....####....
            #####.....#####.....#####.....#####.....
            ######......######......######......####
            #######.......#######.......#######.....
        """.trimIndent()
        assertEquals(expected, part2(testData))
    }

    @Test
    fun run() {
        val inputData = load("input.txt")
        println("part 1: ${part1(inputData)}")
        println("part 2: \n\n${part2(inputData)}")
    }
}
