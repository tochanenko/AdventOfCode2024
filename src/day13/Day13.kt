package day13

import readLines
import kotlin.math.round
import kotlin.math.abs

data class Machine(
    val buttonA: Button,
    val buttonB: Button,
    val x: Double,
    val y: Double
)

data class Button(
    val cost: Int,
    val x: Double,
    val y: Double
)

fun main(args: Array<String>) {
    val lines = readLines("./src/day13/input.txt")
    val machines: List<Machine> = readMachines(lines)
    println(machines.sumOf { canWin(it) })
}

fun readMachines(lines: List<String>): List<Machine> {
    fun parseCoords(line: String): Pair<Long, Long> {
        val regex = Regex("""\d+""")
        val numbers = regex.findAll(line).map { it.value.toLong() }.toList()
        return Pair(numbers[0], numbers[1])
    }

    fun parseButton(line: String): Button {
        val coords = parseCoords(line)
        return Button(
            if (line.contains("Button A")) 3 else 1,
            coords.first.toDouble(),
            coords.second.toDouble()
        )
    }
    // ====================================

    val machines = mutableListOf<Machine>()
    var i = 0
    while (i < lines.size) {
        val coords = parseCoords(lines[i + 2])
        machines.add(
            Machine(
                parseButton(lines[i]),
                parseButton(lines[i + 1]),
                (coords.first + 10000000000000L).toDouble(),
                (coords.second + 10000000000000L).toDouble()
            )
        )

        i += 4
    }

    return machines
}

// Returns 0 if can't win
fun canWin(machine: Machine): Long {
    fun isWholeNumber(number: Double, e: Double = 1e-9): Boolean {
        return abs(number - round(number)) < e
    }
//    if (machine.x > (machine.buttonA.x * 100 + machine.buttonB.x * 100))
//        return 0
//    if (machine.y > (machine.buttonA.y * 100 + machine.buttonB.y * 100))
//        return 0

    val det = machine.buttonA.x * machine.buttonB.y - machine.buttonA.y * machine.buttonB.x

    if (det == 0.toDouble()) return 0

    val i = (machine.x * machine.buttonB.y - machine.y * machine.buttonB.x) / det
    val j = (machine.buttonA.x * machine.y - machine.buttonA.y * machine.x) / det

    return (if (isWholeNumber(i) && isWholeNumber(j)) (round(i) * 3 + round(j)).toLong() else 0)
}