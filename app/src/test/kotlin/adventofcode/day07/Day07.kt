package adventofcode.day07

import adventofcode.common.getResourceAsText
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day07 {

    private fun load(path: String) = getResourceAsText(path)
        .split("\n")
        .filter { it.isNotBlank() }

    sealed interface Command {
        data class Cd(val dir: String) : Command
        data class Ls(val result: MutableList<Entry> = mutableListOf()) : Command {
            fun addDir(dir: String) = result.add(Entry.Dir(dir))
            fun addFile(file: String, size: Int) = result.add(Entry.File(file, size))
            val files: List<Entry.File>
                get() = result.filterIsInstance<Entry.File>()
        }
    }

    sealed interface Entry {
        data class Dir(val dir: String) : Entry
        data class File(val file: String, val size: Int) : Entry
    }

    class WorkingDir {
        private val path = mutableListOf<String>()

        fun cd(dir: String) {
            when (dir) {
                ".." -> path.removeLast()
                "/" -> path += "/"
                else -> path += "${dir}/"
            }
        }

        val allParentDirs: List<String>
            get() = (path.size downTo 1)
                .map { i -> path.subList(0, i).joinToString(separator = "") }
    }

    private fun readCommands(lines: List<String>): MutableList<Command> {
        return lines.fold(mutableListOf()) { commands, line ->
            if (line.startsWith("$ cd")) {
                val dir = line.substring(5)
                commands += Command.Cd(dir)
            } else if (line.startsWith("$ ls")) {
                commands += Command.Ls()
            } else {
                val ls = commands.last() as Command.Ls
                if (line.startsWith("dir")) {
                    ls.addDir(line.substring(4))
                } else {
                    val split = line.split(" ")
                    ls.addFile(split[1], split[0].toInt())
                }
            }
            commands
        }
    }

    private fun calculateDirSizes(commands: MutableList<Command>): MutableMap<String, Int> {
        val workingDir = WorkingDir()
        val dirSizes = mutableMapOf<String, Int>()
        commands.forEach { cmd ->
            when (cmd) {
                is Command.Cd -> workingDir.cd(cmd.dir)
                is Command.Ls -> {
                    val size = cmd.files.sumOf { it.size }
                    workingDir.allParentDirs.forEach { dir ->
                        val old = dirSizes[dir] ?: 0
                        dirSizes[dir] = old + size
                    }
                }
            }
        }
        return dirSizes
    }

    private fun part1(lines: List<String>): Int {
        val commands = readCommands(lines)
        val dirSizes = calculateDirSizes(commands)
        return dirSizes
            .filterValues { it <= 100000 }
            .values
            .sum()
    }


    private fun part2(lines: List<String>): Int {
        val commands = readCommands(lines)
        val dirSizes = calculateDirSizes(commands)
        val totalUnused = 70000000 - dirSizes.getValue("/")
        val toBeFreed = 30000000 - totalUnused
        return dirSizes.filterValues { it >= toBeFreed }
            .values
            .min()
    }


    @Test
    fun test() {
        val testData = load("test.txt")

        val part1 = part1(testData)
        assertEquals(95437, part1)

        val part2 = part2(testData)
        assertEquals(24933642, part2)
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
