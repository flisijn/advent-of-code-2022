package adventofcode.day08

import adventofcode.common.getResourceAsText
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day08 {
    private fun load(path: String) = getResourceAsText(path)
        .split("\n")
        .filter { it.isNotBlank() }
        .map { line ->
            line.chunked(1).map { it.toInt() }
        }

    private fun <T> List<List<T>>.transpose() = List(first().size) { i -> this.map { it[i] } }

    private fun getViews(row: List<Int>, x: Int, col: List<Int>, y: Int): List<List<Int>> {
        val left = row.subList(0, x).reversed()
        val right = row.subList(x + 1, row.size)
        val up = col.subList(0, y).reversed()
        val down = col.subList(y + 1, col.size)
        return listOf(left, right, up, down)
    }

    private fun part1(rows: List<List<Int>>): Int {
        val columns = rows.transpose()

        return rows.indices.sumOf { y ->
            columns.indices.count { x ->
                val views = getViews(rows[y], x, columns[x], y)
                val value = rows[y][x]
                views.any { view -> view.all { it < value } }
            }
        }
    }

    private fun getScore(view: List<Int>, value: Int): Int {
        var count = 0;
        for (tree in view) {
            count++;
            if (tree >= value) {
                break;
            }
        }
        return count
    }

    private fun part2(rows: List<List<Int>>): Int {
        val columns = rows.transpose()

        return rows.indices.maxOf { y ->
            columns.indices.maxOf { x ->
                val views = getViews(rows[y], x, columns[x], y)
                val value = rows[y][x]
                views.map { view -> getScore(view, value) }
                    .fold(1) { acc, it -> acc * it }
            }
        }
    }

    @Test
    fun test() {
        val testData = load("/day08/test.txt")

        val part1 = part1(testData)
        assertEquals(21, part1)

        val part2 = part2(testData)
        assertEquals(8, part2)
    }

    @Test
    fun run() {
        val inputData = load("/day08/input.txt")

        val part1 = part1(inputData)
        println("part 1: $part1")

        val part2 = part2(inputData)
        println("part 2: $part2")
    }
}
