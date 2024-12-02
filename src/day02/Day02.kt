package day02

import readLines

fun main(args: Array<String>) {
    val lines = readLines("./src/day02/input.txt")
    var safeSequences = 0
    println("Lines scanned: ${lines.size}")
    lines.forEach { line ->
        run {
            val values = line.split(" ").map { it.toInt() }
            val isSafe = checkSequence(values)
            if (isSafe) safeSequences++
            println("${if (isSafe) "SAFE" else "UNSAFE"}: $values")
        }
    }

    println("Res: $safeSequences")
}

fun isPairOk(numbers: List<Int>, i: Int, isDescending: Boolean): Boolean {
    if (isDescending) {
        if (numbers[i - 1] - numbers[i] !in 1..3) return false
    } else {
        if (numbers[i] - numbers[i - 1] !in 1..3) return false
    }
    return true
}

fun isArrayOk(numbers: List<Int>): Boolean {
    val isDescending = numbers[0] > numbers[1]
    for (i in 1..<numbers.size) {
        if (!isPairOk(numbers, i, isDescending)) {
            return false
        }
    }
    return true
}

fun checkSequence(numbers: List<Int>): Boolean {
    if (numbers.size < 2) return true

    // Test original sequence
    if (isArrayOk(numbers)) {
        println("OK Sequence1: $numbers")
        return true
    }

    // Test sequences with allowed 1 error
    for (j in numbers.indices) {
        val shortNumbers = numbers.toMutableList()
        shortNumbers.removeAt(j)

        if (isArrayOk(shortNumbers)) {
            println("OK Sequence: $shortNumbers")
            return true
        }
    }

    return false
}
