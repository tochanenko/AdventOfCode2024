package day04

import readLines

fun main(args: Array<String>) {
    val lines = readLines("./src/day04/input.txt")

//    println(calculateXMAS(lines))
    println(calculateMAS(lines))
}

// XMAS

fun calculateXMAS(matrix: List<String>): Int = calculateTopToBottom(matrix) +
            calculateBottomToTop(matrix) +
            calculateLeftToRight(matrix) +
            calculateRightToLeft(matrix) +
            calculateTopLeftToBottomRight(matrix) +
            calculateTopRightToBottomLeft(matrix) +
            calculateBottomLeftToTopRight(matrix) +
            calculateBottomRightToTopLeft(matrix)


fun calculateLine(line: String): Int = Regex("""XMAS""").findAll(line).count()

fun calculateLines(lines: Array<String>): Int {
    var res = 0
    lines.forEach { res += calculateLine(it) }
    return res
}

fun getDescendingDiagonals(matrix: List<String>): Array<String> {
    val size = matrix.size
    val strings: Array<String> = Array(matrix.size * 2 - 1) { "" }

    for (i in 0..<size) {
        for (j in (size - 1) downTo i) {
            val firstHalf = size - 1 - j + i
            val secondHalf = size - 1 + j - i
            strings[firstHalf] = strings[firstHalf] + matrix[i][j]
            if (i != j) strings[secondHalf] = strings[secondHalf] + matrix[j][i]

            println("For [$i; $j] adding ${matrix[i][j]} to $firstHalf")
            println("For [$i; $j] adding ${matrix[j][i]} to $secondHalf")
        }
        println()
    }

    return strings
}

fun getAscendingDiagonals(matrix: List<String>): Array<String> {
    val reversedMatrix = matrix.map { it.reversed() }
    return getDescendingDiagonals(reversedMatrix)
}

// ⬇️
fun calculateTopToBottom(matrix: List<String>): Int {
    val strings: Array<String> = Array(matrix.size) { "" }

    for (i in matrix.indices) {
        for (j in matrix[i].indices) {
            strings[j] = strings[j] + matrix[i][j]
        }
    }

    return calculateLines(strings)
}

// ⬆️
fun calculateBottomToTop(matrix: List<String>): Int {
    val strings: Array<String> = Array(matrix.size) { "" }
    val size = matrix.size

    for (i in matrix.indices) {
        for (j in matrix[i].indices) {
            strings[j] = strings[j] + matrix[size - i - 1][j]
        }
    }

    return calculateLines(strings)
}

// ➡️
fun calculateLeftToRight(matrix: List<String>): Int {
    return calculateLines(matrix.toTypedArray())
}

// ⬅️
fun calculateRightToLeft(matrix: List<String>): Int {
    val strings: Array<String> = Array(matrix.size) { "" }

    for (i in matrix.indices) {
        strings[i] = matrix[i].reversed()
    }

    return calculateLines(strings)
}

// ↘️
fun calculateTopLeftToBottomRight(matrix: List<String>): Int {
    val strings = getDescendingDiagonals(matrix)
    strings.forEach { println(it) }
    return calculateLines(strings)
}

// ↙️
fun calculateTopRightToBottomLeft(matrix: List<String>): Int {
    val strings = getAscendingDiagonals(matrix)
    strings.forEach { println(it) }
    return calculateLines(strings)
}

// ↗️
fun calculateBottomLeftToTopRight(matrix: List<String>): Int {
    val strings = getAscendingDiagonals(matrix).map { it.reversed() }.toTypedArray()
    strings.forEach { println(it) }
    return calculateLines(strings)
}

// ↖️
fun calculateBottomRightToTopLeft(matrix: List<String>): Int {
    val strings = getDescendingDiagonals(matrix).map { it.reversed() }.toTypedArray()
    strings.forEach { println(it) }
    return calculateLines(strings)
}

// X-MAS

fun calculateMAS(matrix: List<String>): Int {
    val size = matrix.size - 3
    var res = 0

    for (i in 0..size) {
        for (j in 0..size) {
            println("$i; $j")
            val square = listOf(
                matrix[i].substring(j, j + 3),
                matrix[i + 1].substring(j, j + 3),
                matrix[i + 2].substring(j, j + 3),
            )

            println(square[0])
            println(square[1])
            println(square[2])

            if (checkSquare(square)) {
                res++
                println("OK")
            }
            println()
        }
    }

    return res
}

fun checkSquare(square: List<String>): Boolean {
    val (first, second, third) = square

    val forward = first[0] == 'M' && third[0] == 'M' &&
            second[1] == 'A' &&
            first[2] == 'S' && third[2] == 'S'

    val backward = first[0] == 'S' && third[0] == 'S' &&
            second[1] == 'A' &&
            first[2] == 'M' && third[2] == 'M'

    val top = first[0] == 'M' && third[0] == 'S' &&
            second[1] == 'A' &&
            first[2] == 'M' && third[2] == 'S'

    val down = first[0] == 'S' && third[0] == 'M' &&
            second[1] == 'A' &&
            first[2] == 'S' && third[2] == 'M'

    return forward || backward || top || down
}