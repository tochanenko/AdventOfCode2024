package Day2

import java.io.File
import java.io.InputStream

fun main(args: Array<String>) {
    val input: InputStream = File("./src/Day2/input.txt").inputStream()
    val lines = mutableListOf<String>()
    var safeSequences = 0

    input.bufferedReader().forEachLine { lines.add(it) }
    println("Lines scanned: ${lines.size}")
    lines.forEach { line ->
        run {
            val values = line.split(" ").map { it.toInt() }
            if (checkSequence(values)) safeSequences++
            println("${if (checkSequence(values)) "SAFE" else "UNSAFE"}: $values")
        }
    }

    println("Res: $safeSequences")
}

fun checkSequence(numbers: List<Int>): Boolean {
    if (numbers.size < 2) return true

    val isDescending = numbers[0] > numbers[1]
    for (i in 1..<numbers.size) {
        if (isDescending) {
            if (numbers[i - 1] - numbers[i] !in 1..3) return false
        } else {
            if (numbers[i] - numbers[i - 1] !in 1..3) return false
        }
    }

    return true
}