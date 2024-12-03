package day03

import readLines

fun main(args: Array<String>) {
    val lines = readLines("./src/day03/input.txt")
    val commands = getAllMultiplicationCommands(lines)

    println(multiply(commands))
}

fun getAllMultiplicationCommands(lines: List<String>): List<String> {
    val regex = Regex("""mul\(-?\d+,-?\d+\)""")
    val commands = mutableListOf<String>()
    lines.forEach { line ->
        run {
            commands.addAll(regex.findAll(line).map { it.value })
        }
    }
    return commands
}

fun getMultiplicationForCommand(command: String): Int {
    val regex = Regex("""mul\((-?\d+),(-?\d+)\)""")
    val numbers = regex.matchEntire(command)
    return numbers!!.groups[1]?.value?.toInt()!! * numbers.groups[2]?.value?.toInt()!!
}

fun multiply(commands: List<String>): Int {
    var res = 0
    commands.forEach { command ->
        run {
            res += getMultiplicationForCommand(command)
        }
    }
    return res
}